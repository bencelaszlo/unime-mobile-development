package hu.bencelaszlo.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hu.bencelaszlo.notes.R;
import hu.bencelaszlo.notes.Adapters.NoteAdapter;
import hu.bencelaszlo.notes.Model.NoteModel;
import hu.bencelaszlo.notes.Utils.DatabaseHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private FloatingActionButton fab;
    private List<NoteModel> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        recyclerView = findViewById(R.id.noteListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(db,MainActivity.this);
        recyclerView.setAdapter(noteAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(noteAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fab = findViewById(R.id.fab);

        noteList = db.getNotes();
        Collections.reverse(noteList);

        noteAdapter.setNotes(noteList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNote.newInstance().show(getSupportFragmentManager(), AddNote.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        noteList = db.getNotes();
        Collections.reverse(noteList);
        noteAdapter.setNotes(noteList);
        noteAdapter.notifyDataSetChanged();
    }
}