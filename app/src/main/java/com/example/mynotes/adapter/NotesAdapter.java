package com.example.mynotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynotes.R;
import com.example.mynotes.databinding.NotesRowBinding;
import com.example.mynotes.model.Notes;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    List<Notes> notes;
    Context ctx;

    public NotesAdapter(Context ctx, List<Notes> notes) {
        this.notes = notes;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesHolder(LayoutInflater.from(ctx).inflate(R.layout.notes_row,null));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesHolder holder, int position) {
        holder.getBinding().setVariable(BR.my_note,notes.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NotesHolder extends RecyclerView.ViewHolder{

        private final NotesRowBinding binding;

        public NotesHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public NotesRowBinding getBinding(){
            return binding;
        }
    }
}
