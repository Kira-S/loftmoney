package com.ks.loftmoney.cells.money;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ks.loftmoney.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter <ItemsAdapter.ItemViewHolder> {

    private List<ItemModel> itemModels = new ArrayList<>();
    private ItemsAdapterClick itemsAdapterClick;

    public void setItemsAdapterClick(ItemsAdapterClick itemsAdapterClick) {
        this.itemsAdapterClick = itemsAdapterClick;
    }

    public void setData(List<ItemModel> itemModels) {
        this.itemModels.clear();
        this.itemModels.addAll(itemModels);
        notifyDataSetChanged();
    }

    public void addData(List<ItemModel> itemModels) {
        this.itemModels.addAll(itemModels);
        notifyDataSetChanged();
    }

    public void addItem(ItemModel item) {
        itemModels.add(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(itemsAdapterClick, LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(itemModels.get(position));
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView valueView;
        ItemsAdapterClick itemsAdapterClick;

        public ItemViewHolder(ItemsAdapterClick itemsAdapterClick, @NonNull View itemView) {
            super(itemView);
            this.itemsAdapterClick = itemsAdapterClick;

            nameView = itemView.findViewById(R.id.cellMoneyNameView);
            valueView = itemView.findViewById(R.id.cellMoneyValueView);
        }

        public void bind(final ItemModel itemModel) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemsAdapterClick != null) {
                        itemsAdapterClick.onMoneyClick(itemModel);
                    }
                }
            });

            nameView.setText(itemModel.getName());
            valueView.setText(itemModel.getValue());
            valueView.setTextColor(ContextCompat.getColor(valueView.getContext(), itemModel.getColor()));
        }
    }
}
