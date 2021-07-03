package com.example.mynotes.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynotes.R;
import com.example.mynotes.databinding.ListItemsBinding;
import com.example.mynotes.interfaces.ItemClickInterface;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsHolder> {

    public List<String> itemsList;
    public List<Boolean> checkBoxValue;
    Context ctx;
    ItemClickInterface clickInterface;

    public ItemsAdapter(Context ctx, List<String> itemsList, List<Boolean> checkBoxValue, ItemClickInterface clickInterface) {
        this.ctx = ctx;
        this.itemsList = itemsList;
        this.clickInterface = clickInterface;
        this.checkBoxValue = checkBoxValue;
    }

    @NonNull
    @Override
    public ItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsHolder(LayoutInflater.from(ctx).inflate(R.layout.list_items, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsHolder holder, int position) {
        holder.getBinding().etList.requestFocus();
        holder.getBinding().etList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                itemsList.set(position, editable.toString());
            }
        });
        holder.getBinding().etList.setText(itemsList.get(position));
        holder.getBinding().imgDrag.setOnLongClickListener(view -> {
            holder.getBinding().etList.clearFocus();
            clickInterface.setOnDragClick();

            return false;
        });
        holder.getBinding().imgClose.setOnClickListener(view -> {
            itemsList.remove(position);
            checkBoxValue.remove(position);
            notifyDataSetChanged();
        });
        holder.getBinding().chkList.setOnCheckedChangeListener((compoundButton, b) -> checkBoxValue.set(position, b));
        boolean isChecked = checkBoxValue.get(position);
        if (isChecked)
            holder.getBinding().chkList.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public static class ItemsHolder extends RecyclerView.ViewHolder {
        private final ListItemsBinding binding;

        public ItemsHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ListItemsBinding getBinding() {
            return binding;
        }
    }
}
