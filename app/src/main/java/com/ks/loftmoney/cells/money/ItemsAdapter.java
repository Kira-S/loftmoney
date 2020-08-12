package com.ks.loftmoney.cells.money;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ks.loftmoney.BudgetFragmentTags;
import com.ks.loftmoney.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter <ItemsAdapter.ItemViewHolder> {

    private List<ItemModel> itemModels = new ArrayList<>();
    private ItemsAdapterListener itemsAdapterListener;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(final int position) {
        selectedItems.put(position, !selectedItems.get(position));
        notifyDataSetChanged();
    }

    public void clearItem(final int position) {
        selectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        int result = 0;
        for (int i = 0; i < itemModels.size(); i++) {
            if(selectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    public List<String> getSelectedItemIds() {
        List<String> result = new ArrayList<>();
        int i = 0;
        for (ItemModel itemModel : itemModels) {
            if (selectedItems.get(i)) {
                result.add(itemModel.getId());
            }
            i++;
        }
        return result;
    }

    public void setListener(ItemsAdapterListener listener) {
        itemsAdapterListener = listener;
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
        return new ItemViewHolder(itemsAdapterListener, LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(itemModels.get(position), selectedItems.get(position));
        holder.setListener(itemsAdapterListener, itemModels.get(position), position);
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView valueView;

        ItemsAdapterListener itemsAdapterListener;

        public ItemViewHolder(ItemsAdapterListener itemsAdapterListener, @NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.cellMoneyNameView);
            valueView = itemView.findViewById(R.id.cellMoneyValueView);
        }

        public void bind(final ItemModel itemModel, final boolean isSelected) {
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (itemsAdapterListener != null) {
//                        itemsAdapterListener.onItemClick(itemModel);
//                    }
//                }
//            });

            itemView.setSelected(isSelected);
            nameView.setText(itemModel.getName());
            valueView.setText(itemModel.getValue());
            valueView.setTextColor(ContextCompat.getColor(valueView.getContext(), itemModel.getColor()));
        }

        public void setListener(final ItemsAdapterListener listener, final ItemModel itemModel, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemsAdapterListener != null) {
                        listener.onItemClick(itemModel, position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(itemModel, position);
                    return false;
                }
            });
        };
    }
}
