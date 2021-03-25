package com.example.mynotes.interfaces;

import com.example.mynotes.model.Notes;

import androidx.cardview.widget.CardView;

public interface RecViewClickInterface {

    void setOnItemLongClick();

    void setOnEditClick(int pos, Notes notes);

    void setOnDeleteClick(int pos);
}
