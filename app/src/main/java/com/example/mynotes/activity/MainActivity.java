package com.example.mynotes.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.example.mynotes.R;
import com.example.mynotes.adapter.NotesAdapter;
import com.example.mynotes.databinding.ActivityMainBinding;
import com.example.mynotes.interfaces.RecViewClickInterface;
import com.example.mynotes.model.Notes;
import com.example.mynotes.viewmodel.NotesViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class MainActivity extends AppCompatActivity implements RecViewClickInterface{

    public static final int SMALL_SIZE = 16, MEDIUM_SIZE = 20, LARGE_SIZE = 24;
    NotesAdapter adapter;
    NotesViewModel viewModel;
    ActivityMainBinding binding;
    List<CardView> cardViews;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    MenuItem menuItemLayout;
    List<Notes> allNotes;
    int spanCount, flag_sort, flag_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        preferences = getSharedPreferences("saveSpanCount", MODE_PRIVATE);
        editor = preferences.edit();
        cardViews = new ArrayList<>();

        binding.fabAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddNotes.class);
                i.putExtra("size", flag_size);
                startActivity(i);
            }
        });

        setMenuAndRecView();
        fillData();
        swipeToDelete();
    }

    private void setMenuAndRecView() {
        binding.toolbarMain.inflateMenu(R.menu.main_menu);
        menuItemLayout = binding.toolbarMain.getMenu().findItem(R.id.mnu_layout);
        spanCount = preferences.getInt("spanCount", 1);
        if (spanCount == 2) {
            menuItemLayout.setIcon(R.drawable.ic_list);
        }
        binding.recNotes.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));

        adapter = new NotesAdapter(MainActivity.this, new ArrayList<>(), this);
        binding.recNotes.setAdapter(adapter);

        SubMenu subMenuSort = binding.toolbarMain.getMenu().addSubMenu(0, Menu.FIRST, Menu.NONE, "Sort By");
        subMenuSort.add(0, 10, Menu.NONE, "Created Date");
        subMenuSort.add(0, 11, Menu.NONE, "Modified Date");

        SubMenu subMenuSize = binding.toolbarMain.getMenu().addSubMenu(1, 2, Menu.NONE, "Font Size");
        subMenuSize.add(1, 15, Menu.NONE, "Small");
        subMenuSize.add(1, 16, Menu.NONE, "Medium");
        subMenuSize.add(1, 17, Menu.NONE, "Large");
        binding.toolbarMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint({"UseCompatLoadingForDrawables", "NonConstantResourceId"})
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("search", String.valueOf(item.getItemId()));
                switch (item.getItemId()) {
                    case R.id.mnu_search:
                        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) binding.toolbarMain.getMenu().findItem(R.id.mnu_search).getActionView();
                        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                adapter.getNoteFilter().filter(newText);
                                return false;
                            }
                        });
                        break;
                    case R.id.mnu_layout:
                        if (item.getIcon().getConstantState().equals(getDrawable(R.drawable.ic_grid).getConstantState())) {
                            spanCount = 2;
                            item.setIcon(R.drawable.ic_list);
                        } else {
                            spanCount = 1;
                            item.setIcon(R.drawable.ic_grid);
                        }
                        binding.recNotes.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
                        adapter.notifyDataSetChanged();
                        break;
                    case 10:
                        allNotes = adapter.notes;
                        flag_sort = 0;
                        Toast.makeText(MainActivity.this, "Sort by created Date", Toast.LENGTH_SHORT).show();
                        Collections.sort(allNotes, new Comparator<Notes>() {
                            @Override
                            public int compare(Notes n1, Notes n2) {
                                return n2.getCreatedDateTime().compareTo(n1.getCreatedDateTime());
                            }
                        });
                        adapter.notifyDataSetChanged();
                        break;
                    case 11:
                        allNotes = adapter.notes;
                        flag_sort = 1;
                        Toast.makeText(MainActivity.this, "Sort by modified Date", Toast.LENGTH_SHORT).show();
                        Collections.sort(allNotes, new Comparator<Notes>() {
                            @Override
                            public int compare(Notes n1, Notes n2) {
                                return n2.getModifiedDateTime().compareTo(n1.getModifiedDateTime());
                            }
                        });
                        adapter.notifyDataSetChanged();
                        break;
                    case 15:
                        flag_size = 0;
                        adapter.setFontSize(SMALL_SIZE);
                        break;
                    case 16:
                        flag_size = 1;
                        adapter.setFontSize(MEDIUM_SIZE);
                        break;
                    case 17:
                        flag_size = 2;
                        adapter.setFontSize(LARGE_SIZE);
                        break;
                }
                return true;
            }
        });
    }

    private void fillData() {
        flag_sort = preferences.getInt("sort", 0);
        flag_size = preferences.getInt("size", 16);
        if (flag_size == 1) {
            adapter.setFontSize(MEDIUM_SIZE);
        } else if (flag_size == 2) {
            adapter.setFontSize(LARGE_SIZE);
        }
        viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
        viewModel.getAllNotesData();
        viewModel.noteLiveData.observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                if (notes == null) {
                    Toast.makeText(MainActivity.this, "No notes", Toast.LENGTH_SHORT).show();
                } else {
                    if (flag_sort == 1) {
                        Collections.sort(notes, new Comparator<Notes>() {
                            @Override
                            public int compare(Notes n1, Notes n2) {
                                return n2.getModifiedDateTime().compareTo(n1.getModifiedDateTime());
                            }
                        });
                    }
                    adapter.setData(notes);
                }
            }
        });
    }

    private void swipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(MainActivity.this,"Move",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Are you sure to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int position = viewHolder.getAdapterPosition();
                                viewModel.deleteNotesData(adapter.getData(position));
                                adapter.removeData(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                builder.create().show();
            }
        }).attachToRecyclerView(binding.recNotes);
    }

    @Override
    public void setOnItemClick(int pos, Notes note, CardView view) {
        Intent intent = new Intent(MainActivity.this, EditNotes.class);
        intent.putExtra("position", pos);
        intent.putExtra("id", note.get_id());
        intent.putExtra("title", note.getTitle());
        intent.putExtra("desc", note.getDescription());
        intent.putExtra("dateTime", note.getCreatedDateTime());
        intent.putExtra("list", (Serializable) note.getItemText());
        intent.putExtra("check", (Serializable) note.getCheckBoxValue());
        intent.putExtra("size", flag_size);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putInt("spanCount", spanCount);
        editor.putInt("sort", flag_sort);
        editor.putInt("size", flag_size);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}