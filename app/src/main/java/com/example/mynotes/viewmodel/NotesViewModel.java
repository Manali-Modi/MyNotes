package com.example.mynotes.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;

import com.example.mynotes.db.AppRoomDatabase;
import com.example.mynotes.model.Notes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@SuppressLint("StaticFieldLeak")
public class NotesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Notes>> notesMutableLiveData = new MutableLiveData<>();
    private final AppRoomDatabase appRoomDatabase;
    public LiveData<List<Notes>> noteLiveData = notesMutableLiveData;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        appRoomDatabase = AppRoomDatabase.getInstance(application);
    }

    public void getAllNotesData() {
        new SelectAllTask().execute();
    }

    public void insertNotesData(Notes note) {
        new InsertTask().execute(note);
    }

    public void updateNotesData(Notes note) {
        new UpdateTask().execute(note);
    }

    public void deleteNotesData(Notes note) {
        new DeleteTask().execute(note);
    }

    private class SelectAllTask extends AsyncTask<Void, Void, List<Notes>> {

        @Override
        protected List<Notes> doInBackground(Void... voids) {
            return appRoomDatabase.daoInterface().fetchAllNotes();
        }

        @Override
        protected void onPostExecute(List<Notes> notes) {
            notesMutableLiveData.postValue(notes);
        }
    }

    private class InsertTask extends AsyncTask<Notes, Void, Notes> {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        @Override
        protected Notes doInBackground(Notes... notes) {
            Notes note = notes[0];
            Callable<Long> insertCallable = () -> appRoomDatabase.daoInterface().insertNote(note);
            Future<Long> future = executorService.submit(insertCallable);
            try {
                long rowId = future.get();
                note.set_id((int) rowId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return note;
        }

        @Override
        protected void onPostExecute(Notes notes) {
            List<Notes> notesList = notesMutableLiveData.getValue();
            if (notesList == null) {
                notesList = new ArrayList<>();
            }
            notesList.add(notes);
            notesMutableLiveData.postValue(notesList);
        }
    }

    private class UpdateTask extends AsyncTask<Notes, Void, Notes> {

        @Override
        protected Notes doInBackground(Notes... notes) {
            appRoomDatabase.daoInterface().updateNote(notes[0]);
            return notes[0];
        }
    }

    private class DeleteTask extends AsyncTask<Notes, Void, Void> {

        @Override
        protected Void doInBackground(Notes... notes) {
            appRoomDatabase.daoInterface().deleteNote(notes[0]);
            return null;
        }
    }

}