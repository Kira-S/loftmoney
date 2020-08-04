package com.ks.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.ks.loftmoney.cells.money.ItemsAdapterClick;
import com.ks.loftmoney.remote.Item;
import com.ks.loftmoney.remote.ItemsApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment extends Fragment {

    public static final int REQUEST_CODE = 100;
    private RecyclerView recyclerView;
    private ItemsAdapter itemsAdapter;
    private FloatingActionButton fab;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout swipeRefreshLayout;

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
//        itemsAdapter.setItemsAdapterClick(new ItemsAdapterClick() {
//            @Override
//            public void onMoneyClick(ItemModel itemModel) {
//                Intent intent = new Intent(getActivity(), AddItemActivity.class);
//                startActivity(intent);
//            }
//        });

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

        String token = (getActivity().getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");

        String name = data.getStringExtra("name");
        String price = data.getStringExtra("price");
        String type;
        Integer color;

        if (getArguments().getSerializable("type") == BudgetFragmentTags.EXPENSES) {
            color = R.color.expenseColor;
            type = "expense";
        } else {
            color = R.color.incomeColor;
            type = "income";
        }

        compositeDisposable.add(((LoftApp) getActivity().getApplication()).getItemsApi().addBudget(token, name, price, type)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        //setUI(false);
                        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                            itemsAdapter.addItem(new ItemModel(data.getStringExtra("name"), data.getStringExtra("price"), color));
                        }
                        Toast.makeText(getActivity().getApplicationContext(), R.string.toast_added_success,
                                Toast.LENGTH_SHORT).show();
                        //getActivity().finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //setUI(false);
                        Toast.makeText(getActivity().getApplicationContext(), R.string.toast_added_fail,
                                Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }));
    }

    private void loadItems() {
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
}
