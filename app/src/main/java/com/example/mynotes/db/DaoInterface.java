package com.example.mynotes.db;

import com.example.mynotes.model.Notes;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoInterface {

    @Query("select * from allNotes order by _id desc")
    List<Notes> fetchAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNote(Notes note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(Notes note);

    @Delete
    void deleteNote(Notes note);
}
