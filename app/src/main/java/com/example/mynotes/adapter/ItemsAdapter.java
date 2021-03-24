package com.example.mynotes.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.databinding.ListItemsBinding;
import com.example.mynotes.interfaces.ItemClickInterface;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsHolder> {

    Context ctx;
    public List<String> itemsList;
    ItemClickInterface clickInterface;
    public List<Boolean> checkBoxValue;

    public ItemsAdapter(Context ctx, List<String> itemsList, List<Boolean> checkBoxValue, ItemClickInterface clickInterface) {
        this.ctx = ctx;
        this.itemsList = itemsList;
        this.clickInterface = clickInterface;
        this.checkBoxValue = checkBoxValue;
    }

    @NonNull
    @Override
    public ItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsHolder(LayoutInflater.from(ctx).inflate(R.layout.list_items,null));
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
                itemsList.set(position,editable.toString());
            }
        });
        holder.getBinding().etList.setText(itemsList.get(position));
        holder.getBinding().imgDrag.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.getBinding().etList.clearFocus();
                Toast.makeText(ctx,"To drag items",Toast.LENGTH_SHORT).show();
                clickInterface.setOnDragClick();
                return false;
            }
        });
        holder.getBinding().imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemsList.remove(position);
                checkBoxValue.remove(position);
                Log.d("check",checkBoxValue.toString());
                notifyDataSetChanged();
            }
        });
        holder.getBinding().chkList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkBoxValue.set(position,b);
            }
        });
        boolean isChecked = checkBoxValue.get(position);
        if(isChecked)
            holder.getBinding().chkList.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ItemsHolder extends RecyclerView.ViewHolder {
        private ListItemsBinding binding;

        public ItemsHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ListItemsBinding getBinding(){
            return binding;
        }
    }
}
