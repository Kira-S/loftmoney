package com.ks.loftmoney.cells.money;

import com.ks.loftmoney.R;
import com.ks.loftmoney.remote.Item;

public class ItemModel {
    private String name;
    private String value;
    private Integer color;

    public ItemModel(String name, String value, Integer color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public static ItemModel getInstance(Item item) {
        return new ItemModel(item.getItemName(), item.getItemPrice() + "₽",
                item.getItemType().equals("expense") ? R.color.expenseColor : R.color.incomeColor);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Integer getColor() {
        return color;
    }

}
