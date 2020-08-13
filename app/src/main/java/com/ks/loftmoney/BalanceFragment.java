package com.ks.loftmoney;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ks.loftmoney.remote.BalanceResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BalanceFragment extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView myExpenses;
    private TextView myIncome;
    private TextView totalFinances;
    private BalanceView balanceView;

    private RecyclerView recyclerView;

    public static Fragment newInstance() {
        return new BalanceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadBalance();

        balanceView = view.findViewById(R.id.balanceDiagram);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        myExpenses = view.findViewById(R.id.txtBalanceExpensesValue);
        myIncome = view.findViewById(R.id.txtBalanceIncomesValue);
        totalFinances = view.findViewById(R.id.txtBalanceTotalValue);
        balanceView = view.findViewById(R.id.balanceDiagram);

        recyclerView = view.findViewById(R.id.costsRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
//                LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBalance();
            }
        });
    }

    public void loadBalance() {

        String token = (getActivity().getApplication()).getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.TOKEN_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).getItemsApi().getBalance(token)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BalanceResponse>() {
                    @Override
                    public void accept(BalanceResponse balanceResponse) throws Exception {
                        final float totalExpenses = balanceResponse.getTotalExpenses();
                        final float totalIncome = balanceResponse.getTotalIncome();

                        myExpenses.setText(String.valueOf(totalExpenses));
                        myIncome.setText(String.valueOf(totalIncome));
                        totalFinances.setText(String.valueOf(totalIncome - totalExpenses));
                        balanceView.update(totalExpenses, totalIncome);
                        swipeRefreshLayout.setRefreshing(false);
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
