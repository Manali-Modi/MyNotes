package com.example.mynotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mynotes.R;
import com.example.mynotes.adapter.ItemsAdapter;
import com.example.mynotes.databinding.ActivityAddNotesBinding;
import com.example.mynotes.interfaces.ItemClickInterface;
import com.example.mynotes.model.Notes;
import com.example.mynotes.viewmodel.NotesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditNotes extends AppCompatActivity implements ItemClickInterface {

    NotesViewModel viewModel;
    ActivityAddNotesBinding binding;
    int pos, id;
    String oldTitle, oldDesc, oldDateTime;
    String newTitle, newDesc, newDateTime;
    int flagSize, textSize;
    List<String> itemList;
    List<Boolean> checkList;
    ItemsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(EditNotes.this, R.layout.activity_add_notes);
        getSupportActionBar().hide();
        flagSize = getIntent().getExtras().getInt("size");
        if (flagSize == 2)
            textSize = MainActivity.LARGE_SIZE;
        else if (flagSize == 1)
            textSize = MainActivity.MEDIUM_SIZE;
        else
            textSize = MainActivity.SMALL_SIZE;

        binding.etTitle.setTextSize(textSize);
        binding.etDesc.setTextSize(textSize);

        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        displayExistingData();
        manageToolbar();
        manageListItems();
    }

    private void manageListItems() {
        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList.add("");
                checkList.add(false);
                adapter.notifyItemInserted(itemList.size()-1);
            }
        });
    }

    private void displayExistingData() {
        pos = getIntent().getExtras().getInt("position");
        oldTitle = getIntent().getExtras().getString("title");
        oldDesc = getIntent().getExtras().getString("desc");
        oldDateTime = getIntent().getExtras().getString("dateTime");
        itemList = getIntent().getExtras().getStringArrayList("list");
        checkList =(List<Boolean>) getIntent().getExtras().getSerializable("check");
        adapter = new ItemsAdapter(this, itemList, checkList, this);

        Notes existingNote = new Notes(oldTitle, oldDesc, oldDateTime, oldDateTime, itemList, checkList);
        binding.setVariable(com.example.mynotes.BR.one_note, existingNote);
        binding.recItems.setLayoutManager(new LinearLayoutManager(this));
        binding.recItems.setAdapter(adapter);
    }

    private void manageToolbar() {
        binding.toolbarAdd.setTitle(getResources().getString(R.string.edit));
        binding.toolbarAdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        binding.toolbarAdd.inflateMenu(R.menu.app_menu);
        binding.toolbarAdd.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.mnu_share) {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, oldTitle + ": " + oldDesc)
                            .putExtra(Intent.EXTRA_SUBJECT, "Share Note");
                    startActivity(sendIntent);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        updateData();
    }

    private void updateData() {
        id = getIntent().getExtras().getInt("id");
        newTitle = binding.etTitle.getText().toString();
        newDesc = binding.etDesc.getText().toString();
        newDateTime = String.valueOf(System.currentTimeMillis());
        if (newTitle.length() == 0) {
            Toast.makeText(EditNotes.this, "Set the note title first", Toast.LENGTH_SHORT).show();
        } else if (newDesc.length() == 0) {
            Toast.makeText(EditNotes.this, "Set the note description", Toast.LENGTH_SHORT).show();
        } else {
            Notes newNote = new Notes(newTitle, newDesc, oldDateTime, newDateTime, adapter.itemsList, adapter.checkBoxValue);
            newNote.set_id(id);
            viewModel.updateNotesData(newNote);
            startActivity(new Intent(EditNotes.this, MainActivity.class));
        }
    }

    @Override
    public void setOnDragClick() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(adapter.itemsList,viewHolder.getAdapterPosition(),target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(binding.recItems);
    }
}
