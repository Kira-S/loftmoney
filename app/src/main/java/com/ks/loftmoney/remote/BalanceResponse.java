package com.ks.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse {
    @SerializedName("total_expenses") private float totalExpenses;
    @SerializedName("total_income") private float totalIncome;

    public float getTotalExpenses() {
        return totalExpenses;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalExpenses(final float totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public void setTotalIncome(final float totalIncome) {
        this.totalIncome = totalIncome;
    }
}
