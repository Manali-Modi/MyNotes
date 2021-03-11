package com.example.mynotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mynotes.R;
import com.example.mynotes.adapter.NotesAdapter;
import com.example.mynotes.model.Notes;
import com.example.mynotes.viewmodel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class MainActivity extends AppCompatActivity {

    RecyclerView recNotes;
    FloatingActionButton fabAddNotes;
    NotesAdapter adapter;
    NotesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        recNotes = findViewById(R.id.rec_notes);
        recNotes.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        fabAddNotes = findViewById(R.id.fab_add_notes);
        fabAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNotes.class));
            }
        });

        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        viewModel.getAllNotesData().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                adapter = new NotesAdapter(MainActivity.this, notes);
                recNotes.setAdapter(adapter);
            }
        });
    }
}