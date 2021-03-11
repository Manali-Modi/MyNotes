package com.example.mynotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.mynotes.R;
import com.example.mynotes.model.Notes;
import com.example.mynotes.viewmodel.NotesViewModel;

import androidx.appcompat.app.AppCompatActivity;

public class AddNotes extends AppCompatActivity {

    Toolbar toolbarAdd;
    EditText etTitle, etDesc;
    NotesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        getSupportActionBar().hide();
        toolbarAdd = findViewById(R.id.toolbar_add);
        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_desc);
        viewModel = new NotesViewModel(getApplication());

        toolbarAdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveData();
    }

    private void saveData() {
        viewModel.insertNotesData(new Notes(etTitle.getText().toString(), etDesc.getText().toString()));
        startActivity(new Intent(AddNotes.this,MainActivity.class));
    }
}