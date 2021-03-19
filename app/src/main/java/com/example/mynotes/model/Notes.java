package com.example.mynotes.model;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "allNotes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "created_date_time")
    private String createdDateTime;

    @ColumnInfo(name = "modified_date_time")
    private String modifiedDateTime;

    public Notes(String title, String description, String createdDateTime, String modifiedDateTime) {
        this.title = title;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String dateTime) {
        this.createdDateTime = dateTime;
    }

    public String getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(String modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public String getDateTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(modifiedDateTime));
        return formatter.format(calendar.getTime());
    }
}
