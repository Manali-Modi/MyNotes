package com.example.mynotes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.mynotes.R;
import com.example.mynotes.activity.MainActivity;
import com.example.mynotes.databinding.NotesRowBinding;
import com.example.mynotes.interfaces.RecViewClickInterface;
import com.example.mynotes.model.Notes;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    public List<Notes> notes;
    List<Notes> notesList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

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

    public NotesAdapter(Context ctx, List<Notes> notes, RecViewClickInterface clickInterface) {
        this.notes = notes;
        this.ctx = ctx;
        this.clickInterface = clickInterface;
        notesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public NotesAdapter.NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesHolder(LayoutInflater.from(ctx).inflate(R.layout.notes_row, null));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesHolder holder, int position) {
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

    public class NotesHolder extends RecyclerView.ViewHolder {

        private final NotesRowBinding binding;

        public NotesHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            assert binding != null;
            binding.crdNotes.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //binding.crdNotes.setBackgroundColor(ctx.getResources().getColor(R.color.orange_pink_dark));
                    clickInterface.setOnItemLongClick();
                    return true;
                }
            });

            binding.txtEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickInterface.setOnEditClick(getAdapterPosition(), notes.get(getAdapterPosition()));
                }
            });

            binding.txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickInterface.setOnDeleteClick(getAdapterPosition());
                }
            });
        }

        public NotesRowBinding getBinding() {
            return binding;
        }
    }
}
