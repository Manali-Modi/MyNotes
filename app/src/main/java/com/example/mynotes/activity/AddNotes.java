package com.example.mynotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.adapter.ItemsAdapter;
import com.example.mynotes.databinding.ActivityAddNotesBinding;
import com.example.mynotes.interfaces.ItemClickInterface;
import com.example.mynotes.model.Notes;
import com.example.mynotes.viewmodel.NotesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddNotes extends AppCompatActivity implements ItemClickInterface {

    ActivityAddNotesBinding binding;
    NotesViewModel viewModel;
    int flagSize, textSize;
    InputMethodManager imm;
    ItemsAdapter itemsAdapter;
    List<String> items = new ArrayList<>();
    List<Boolean> checkValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        binding = DataBindingUtil.setContentView(AddNotes.this, R.layout.activity_add_notes);
        binding.etTitle.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        itemsAdapter = new ItemsAdapter(this, items, checkValue, this);

        flagSize = getIntent().getIntExtra("size", 0);
        if (flagSize == 2)
            textSize = MainActivity.LARGE_SIZE;
        else if (flagSize == 1)
            textSize = MainActivity.MEDIUM_SIZE;
        else
            textSize = MainActivity.SMALL_SIZE;

        binding.etTitle.setTextSize(textSize);
        binding.etDesc.setTextSize(textSize);
        viewModel = new NotesViewModel(getApplication());

        binding.toolbarAdd.setNavigationOnClickListener(view -> {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            saveData();
        });

        setRecViewItems();
        addItems();
    }

    private void addItems() {
        binding.imgAdd.setOnClickListener(view -> {
            items.add("");
            checkValue.add(false);
            itemsAdapter.notifyItemInserted(items.size() - 1);
        });
    }

    private void setRecViewItems() {
        binding.recItems.setLayoutManager(new LinearLayoutManager(AddNotes.this));
        binding.recItems.setAdapter(itemsAdapter);
    }

    @Override
    public void onBackPressed() {
        binding.etDesc.requestFocus();
        saveData();
    }

    private void saveData() {
        String title = binding.etTitle.getText().toString();
        String desc = binding.etDesc.getText().toString();
        String dateTime = String.valueOf(System.currentTimeMillis());

        if (title.length() == 0 && desc.length() == 0) {
            Toast.makeText(AddNotes.this, "Empty Note not saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddNotes.this, MainActivity.class));
        } else if (title.length() == 0) {
            Toast.makeText(AddNotes.this, "Set the note title first", Toast.LENGTH_SHORT).show();
        } else if (desc.length() == 0) {
            Toast.makeText(AddNotes.this, "Set the note description", Toast.LENGTH_SHORT).show();
        } else {
            Notes note = new Notes(title, desc, dateTime, dateTime, itemsAdapter.itemsList, itemsAdapter.checkBoxValue);
            viewModel.insertNotesData(note);
            startActivity(new Intent(AddNotes.this, MainActivity.class));
        }
    }

    @Override
    public void setOnDragClick() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(itemsAdapter.itemsList, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Collections.swap(itemsAdapter.checkBoxValue, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                itemsAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(binding.recItems);
    }
}