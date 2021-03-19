package com.example.mynotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mynotes.R;
import com.example.mynotes.databinding.ActivityAddNotesBinding;
import com.example.mynotes.model.Notes;
import com.example.mynotes.viewmodel.NotesViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class EditNotes extends AppCompatActivity {

    NotesViewModel viewModel;
    ActivityAddNotesBinding binding;
    int pos, id;
    String oldTitle, oldDesc, oldDateTime;
    String newTitle, newDesc, newDateTime;
    int flagSize, textSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(EditNotes.this, R.layout.activity_add_notes);
        getSupportActionBar().hide();

        flagSize = getIntent().getExtras().getInt("size");
        if (flagSize == 0)
            textSize = MainActivity.SMALL_SIZE;
        else if (flagSize == 1)
            textSize = MainActivity.MEDIUM_SIZE;
        else
            textSize = MainActivity.LARGE_SIZE;

        binding.etTitle.setTextSize(textSize);
        binding.etDesc.setTextSize(textSize);

        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);

        pos = getIntent().getExtras().getInt("position");
        oldTitle = getIntent().getExtras().getString("title");
        oldDesc = getIntent().getExtras().getString("desc");
        oldDateTime = getIntent().getExtras().getString("dateTime");
        Notes existingNote = new Notes(oldTitle, oldDesc, oldDateTime, oldDateTime);
        binding.setVariable(com.example.mynotes.BR.one_note, existingNote);

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

        if (newTitle.equals(oldTitle) && newDesc.equals(oldDesc)) {
            startActivity(new Intent(EditNotes.this, MainActivity.class));
        } else {
            newDateTime = String.valueOf(System.currentTimeMillis());
            if (newTitle.length() == 0) {
                Toast.makeText(EditNotes.this, "Set the note title first", Toast.LENGTH_SHORT).show();
            } else if (newDesc.length() == 0) {
                Toast.makeText(EditNotes.this, "Set the note description", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditNotes.this, "Note updated", Toast.LENGTH_SHORT).show();
                Notes newNote = new Notes(newTitle, newDesc, oldDateTime, newDateTime);
                newNote.set_id(id);
                viewModel.updateNotesData(newNote);
                startActivity(new Intent(EditNotes.this, MainActivity.class));
            }
        }
    }
}
