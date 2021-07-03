package com.example.mynotes.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.mynotes.R;
import com.example.mynotes.databinding.NotesRowBinding;
import com.example.mynotes.interfaces.RecViewClickInterface;
import com.example.mynotes.model.Notes;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    public List<Notes> notes;
    List<Notes> notesList;
    private final Filter noteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Notes> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(notesList);
            } else {
                String searchText = charSequence.toString().toLowerCase().trim();
                for (Notes eachNote : notesList) {
                    if (eachNote.getTitle().toLowerCase().contains(searchText) && eachNote.getDescription().toLowerCase().contains(searchText))
                        filteredList.add(eachNote);
                    else if (eachNote.getTitle().toLowerCase().contains(searchText))
                        filteredList.add(eachNote);
                    else if (eachNote.getDescription().toLowerCase().contains(searchText))
                        filteredList.add(eachNote);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notes.clear();
            notes.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
    Context ctx;
    RecViewClickInterface clickInterface;
    int textSize = 16;
    private NotesHolder notesHolder;

    public NotesAdapter(Context ctx, List<Notes> notes, RecViewClickInterface clickInterface) {
        this.notes = notes;
        this.ctx = ctx;
        this.clickInterface = clickInterface;
        notesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public NotesAdapter.NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        notesHolder = new NotesHolder(LayoutInflater.from(ctx).inflate(R.layout.notes_row, parent, false));
        return notesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesHolder holder, int position) {
        changeView(holder.getBinding());
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.getBinding().swipeLayout, String.valueOf(notes.get(position).get_id()));
        viewBinderHelper.closeLayout(String.valueOf(notes.get(position).get_id()));
        holder.getBinding().setVariable(com.example.mynotes.BR.my_note, notes.get(position));
        holder.getBinding().executePendingBindings();
        holder.getBinding().txtTitle.setTextSize(textSize);
        holder.getBinding().txtDesc.setTextSize(textSize);
        holder.getBinding().crdNotes.setRotation(0f);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setData(List<Notes> note) {
        notes.addAll(note);
        notesList.addAll(note);
        notifyDataSetChanged();
    }

    public Notes getData(int pos) {
        return notes.get(pos);
    }

    public void removeData(int pos) {
        notes.remove(pos);
        notesList.remove(pos);
        notifyDataSetChanged();
    }

    public Filter getNoteFilter() {
        return noteFilter;
    }

    public void setFontSize(int textSize) {
        this.textSize = textSize;
        notifyDataSetChanged();
    }

    public NotesHolder getNotesHolder() {
        return notesHolder;
    }

    public void changeView(NotesRowBinding binding) {
        SharedPreferences preferences = ctx.getSharedPreferences("saveSpanCount", MODE_PRIVATE);
        int spanCount = preferences.getInt("spanCount", 1);
        if (spanCount == 2) {
            binding.crdNotes.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            binding.crdNotes.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public class NotesHolder extends RecyclerView.ViewHolder {

        private final NotesRowBinding binding;

        public NotesHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            assert binding != null;
            binding.crdNotes.setOnLongClickListener(view -> {
                clickInterface.setOnItemLongClick();
                return true;
            });

            binding.txtEdit.setOnClickListener(view -> clickInterface.setOnEditClick(getAdapterPosition(), notes.get(getAdapterPosition())));

            binding.txtDelete.setOnClickListener(view -> clickInterface.setOnDeleteClick(getAdapterPosition()));

        }

        public NotesRowBinding getBinding() {
            return binding;
        }
    }
}
