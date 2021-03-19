package com.example.mynotes.interfaces;

import com.example.mynotes.model.Notes;

import androidx.cardview.widget.CardView;

public interface RecViewClickInterface {

    void setOnItemClick(int pos, Notes notes, CardView view);
}
