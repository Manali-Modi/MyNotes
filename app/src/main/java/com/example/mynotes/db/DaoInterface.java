package com.example.mynotes.db;

import com.example.mynotes.model.Notes;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoInterface {

    @Query("select * from allNotes")
    List<Notes> fetchAllNotes();

    @Insert
    void insertNote(Notes note);

    @Update
    void updateNote(Notes note);

    @Delete
    void deleteNote(Notes note);
}
