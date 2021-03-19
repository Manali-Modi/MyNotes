package com.example.mynotes.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.databinding.ActivityAddNotesBinding;
import com.example.mynotes.model.Notes;
import com.example.mynotes.viewmodel.NotesViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class AddNotes extends AppCompatActivity {

    ActivityAddNotesBinding binding;
    NotesViewModel viewModel;
    int flagSize, textSize;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(AddNotes.this, R.layout.activity_add_notes);
        binding.etTitle.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        flagSize = getIntent().getExtras().getInt("size");
        if (flagSize == 0)
            textSize = MainActivity.SMALL_SIZE;
        else if (flagSize == 1)
            textSize = MainActivity.MEDIUM_SIZE;
        else
            textSize = MainActivity.LARGE_SIZE;

        binding.etTitle.setTextSize(textSize);
        binding.etDesc.setTextSize(textSize);
        viewModel = new NotesViewModel(getApplication());

        binding.toolbarAdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    @Override
    public void onBackPressed() {
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
            Notes note = new Notes(title, desc, dateTime, dateTime);
            viewModel.insertNotesData(note);
            startActivity(new Intent(AddNotes.this, MainActivity.class));
        }
    }
}