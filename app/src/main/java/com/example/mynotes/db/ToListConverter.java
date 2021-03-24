package com.example.mynotes.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

public class ToListConverter implements Serializable {

    Gson gson = new Gson();
    @TypeConverter
    public List<String> stringToList(String string){
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(string,type);
    }

    @TypeConverter
    public String listToString(List<String> list){
        return gson.toJson(list);
    }

    @TypeConverter
    public List<Boolean> booleanToList(String string){
        Type type = new TypeToken<List<Boolean>>() {}.getType();
        return gson.fromJson(string,type);
    }

    @TypeConverter
    public String listToBoolean(List<Boolean> list){
        return gson.toJson(list);
    }
}
