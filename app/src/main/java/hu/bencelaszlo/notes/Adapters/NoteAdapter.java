package hu.bencelaszlo.notes.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import hu.bencelaszlo.notes.R;
import hu.bencelaszlo.notes.AddNote;
import hu.bencelaszlo.notes.MainActivity;
import hu.bencelaszlo.notes.Model.NoteModel;
import hu.bencelaszlo.notes.Utils.DatabaseHandler;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<NoteModel> notes;
    private DatabaseHandler db;
    private MainActivity activity;

    public NoteAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();
        holder.text.setText(notes.get(position).getNote());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                editItem(holder.getAdapterPosition());
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteItem(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setNotes(List<NoteModel> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        NoteModel item = notes.get(position);
        db.deleteNote(item.getId());
        notes.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        NoteModel item = notes.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("note", item.getNote());
        AddNote fragment = new AddNote();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNote.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        Button editButton;
        Button deleteButton;

        ViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.noteText);
            editButton = view.findViewById(R.id.noteEditButton);
            deleteButton = view.findViewById(R.id.noteDeleteButton);
        }
    }
}
