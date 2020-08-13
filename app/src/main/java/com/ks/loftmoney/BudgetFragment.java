package com.ks.loftmoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ks.loftmoney.cells.money.ItemModel;
import com.ks.loftmoney.cells.money.ItemsAdapter;
import com.ks.loftmoney.cells.money.ItemsAdapterListener;
import com.ks.loftmoney.remote.Item;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment extends Fragment implements ItemsAdapterListener, ActionMode.Callback {

    public static final int REQUEST_CODE = 100;
    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private FloatingActionButton fab;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode mActionMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_budget, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.costsRecyclerView);
        fab = view.findViewById(R.id.call_add_item_activity_button);

        loadItems();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        itemsAdapter = new ItemsAdapter();

        itemsAdapter.setListener(this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadItems();
    }

    public static BudgetFragment newInstance(BudgetFragmentTags tag) {
        BudgetFragment budgetFragment = new BudgetFragment();

        Bundle args = new Bundle();
        args.putSerializable("type", tag);
        budgetFragment.setArguments(args);

        return budgetFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadItems();

        Integer color;

        if (getArguments().getSerializable("type") == BudgetFragmentTags.EXPENSES) {
            color = R.color.expenseColor;
        } else {
            color = R.color.incomeColor;
        }

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            itemsAdapter.addItem(new ItemModel("", data.getStringExtra("name"), data.getStringExtra("price"), color));
            loadItems();
        }
    }

    public void loadItems() {
        final List<ItemModel> itemModels = new ArrayList<>();
        String token = (getActivity().getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");

        String type;
        if (getArguments().getSerializable("type") == BudgetFragmentTags.EXPENSES) {
            type = "expense";
        } else {
            type = "income";
        }

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getItemsApi().getItems(token, type)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Item>>() {
                    @Override
                    public void accept(List<Item> items) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        for (Item item : items) {
                            itemModels.add(ItemModel.getInstance(item));
                        }
                        itemsAdapter.setData(itemModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("TAG", "Error " + throwable);
                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void onItemClick(final ItemModel itemModel, final int position) {
        itemsAdapter.clearItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onItemLongClick(ItemModel itemModel, int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        itemsAdapter.toggleItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.remove) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removeItems();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
        return true;
    }

    private void removeItems() {
        String token = (getActivity().getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");
        List<String> selectedItems = itemsAdapter.getSelectedItemIds();
        for (String itemId : selectedItems) {
            compositeDisposable.delete(((LoftApp) getActivity().getApplication()).getItemsApi().removeItem(String.valueOf(itemId), token)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {
                            loadItems();
                            itemsAdapter.clearSelections();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    }));;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mActionMode = null;
        itemsAdapter.clearSelections();
    }
}
