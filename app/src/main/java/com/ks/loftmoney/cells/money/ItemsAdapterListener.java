package com.ks.loftmoney.cells.money;

public interface ItemsAdapterListener {
    void onItemClick(ItemModel itemModel, int position);
    void onItemLongClick(ItemModel itemModel, int position);
}
