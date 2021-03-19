package com.example.mynotes.db;

import android.content.Context;

import com.example.mynotes.model.Notes;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Notes.class, exportSchema = false, version = 3)
public abstract class AppRoomDatabase extends RoomDatabase {

    private static final String DB_NAME = "AppNotes";
    private static AppRoomDatabase roomDatabase;

    public static synchronized AppRoomDatabase getInstance(Context ctx) {
        if (roomDatabase == null) {
            roomDatabase = Room.databaseBuilder(ctx, AppRoomDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return roomDatabase;
    }

    public abstract DaoInterface daoInterface();
}
