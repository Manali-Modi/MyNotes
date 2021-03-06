package com.example.mynotes.model;

import android.annotation.SuppressLint;

import com.example.mynotes.db.ToListConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "allNotes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private final String description;

    @ColumnInfo(name = "created_date_time")
    private final String createdDateTime;

    @ColumnInfo(name = "modified_date_time")
    private final String modifiedDateTime;

    @TypeConverters(ToListConverter.class)
    @ColumnInfo(name = "item_text")
    private final List<String> itemText;

    @TypeConverters(ToListConverter.class)
    @ColumnInfo(name = "is_checked")
    private final List<Boolean> checkBoxValue;

    public Notes(String title, String description, String createdDateTime, String modifiedDateTime, List<String> itemText, List<Boolean> checkBoxValue) {
        this.title = title;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
        this.itemText = itemText;
        this.checkBoxValue = checkBoxValue;
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

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public String getModifiedDateTime() {
        return modifiedDateTime;
    }

    public List<String> getItemText() {
        return itemText;
    }

    public List<Boolean> getCheckBoxValue() {
        return checkBoxValue;
    }

    public String getDateTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(modifiedDateTime));
        return formatter.format(calendar.getTime());
    }
}
