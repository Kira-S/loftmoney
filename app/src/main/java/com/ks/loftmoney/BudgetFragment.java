package com.ks.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ks.loftmoney.cells.money.ItemModel;
import com.ks.loftmoney.cells.money.ItemsAdapter;
import com.ks.loftmoney.cells.money.ItemsAdapterClick;

import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    private static final int REQUEST_CODE = 100;
    RecyclerView recyclerView;
    ItemsAdapter itemsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_budget, null);

        recyclerView = view.findViewById(R.id.costsRecyclerView);
        itemsAdapter = new ItemsAdapter();
        itemsAdapter.setItemsAdapterClick(new ItemsAdapterClick() {
            @Override
            public void onMoneyClick(ItemModel itemModel) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        itemsAdapter.addData(generateExpenses());

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                itemsAdapter.addData(generateIncomes());
//            }
//        }, 3000);

        view.findViewById(R.id.call_add_item_activity_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivity = new Intent(getActivity(), AddItemActivity.class);
                startActivityForResult(newActivity, REQUEST_CODE);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int color = R.color.expenseColor;

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            itemsAdapter.addItem(new ItemModel(data.getStringExtra("name"), data.getStringExtra("price"), color));
        }
    }

    private List<ItemModel> generateExpenses() {
        List<ItemModel> itemModels = new ArrayList<>();
        itemModels.add(new ItemModel("Молоко", "70 ₽", R.color.expenseColor));
        itemModels.add(new ItemModel("Зубная щётка", "70 ₽", R.color.expenseColor));
        itemModels.add(new ItemModel("Сковородка с антипригарным покрытием",
                "1670 ₽", R.color.expenseColor));

        return itemModels;
    }

    private List<ItemModel> generateIncomes() {
        List<ItemModel> itemModels = new ArrayList<>();
        itemModels.add(new ItemModel("Зарплата.Июнь", "70000 ₽", R.color.incomeColor));
        itemModels.add(new ItemModel("Премия", "7000 ₽", R.color.incomeColor));
        itemModels.add(new ItemModel("Олег наконец-то вернул долг",
                "300000 ₽", R.color.incomeColor));

        return itemModels;
    }

}
