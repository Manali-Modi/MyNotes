package com.example.mynotes.viewmodel;

import android.app.Application;
import android.util.Log;

import com.example.mynotes.db.AppRoomDatabase;
import com.example.mynotes.model.Notes;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NotesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Notes>> liveNoteList;
    AppRoomDatabase appRoomDatabase;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        appRoomDatabase = AppRoomDatabase.getInstance(application);
    }


    public LiveData<List<Notes>> getAllNotesData(){
        if(liveNoteList==null){
            liveNoteList = new MutableLiveData<>();
            liveNoteList.setValue(appRoomDatabase.daoInterface().fetchAllNotes());
        }
        return liveNoteList;
    }

    public void insertNotesData(Notes note){
        appRoomDatabase.daoInterface().insertNote(note);
        Log.d("data","Inserted");
    }

    public void updateNotesData(Notes note){
        appRoomDatabase.daoInterface().updateNote(note);
    }

    public void deleteNotesData(Notes note){
        appRoomDatabase.daoInterface().deleteNote(note);
    }
}
