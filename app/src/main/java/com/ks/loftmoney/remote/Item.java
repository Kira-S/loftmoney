package com.ks.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("id") private String itemId;
    @SerializedName("name") private String itemName;
    @SerializedName("price") private int itemPrice;
    @SerializedName("type") private String itemType;
    @SerializedName("date") private String itemDate;

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemDate() {
        return itemDate;
    }
}
