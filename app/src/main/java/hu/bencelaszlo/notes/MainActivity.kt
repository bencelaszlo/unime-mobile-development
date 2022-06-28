package hu.bencelaszlo.notes

import android.os.Bundle
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import hu.bencelaszlo.notes.adapters.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import hu.bencelaszlo.notes.model.NoteModel
import hu.bencelaszlo.notes.utils.DatabaseHandler
import java.util.*

class MainActivity : AppCompatActivity(), DialogCloseListener {
    private lateinit var db: DatabaseHandler
    private var recyclerView: RecyclerView? = null
    private var noteAdapter: NoteAdapter? = null
    private var fab: FloatingActionButton? = null
    private lateinit var noteList: MutableList<NoteModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Objects.requireNonNull(supportActionBar)?.hide()
        db = DatabaseHandler(this)
        db.openDatabase()
        recyclerView = findViewById(R.id.noteListRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(db!!, this@MainActivity)
        recyclerView?.adapter = noteAdapter
        val itemTouchHelper = ItemTouchHelper(
            RecyclerItemTouchHelper(
                noteAdapter!!
            )
        )
        itemTouchHelper.attachToRecyclerView(recyclerView)
        fab = findViewById(R.id.fab)
        noteList = db.notes
        noteList.reverse()
        noteAdapter!!.setNotes(noteList)
        fab?.setOnClickListener {
            AddNote.newInstance().show(
                supportFragmentManager, AddNote.TAG
            )
        }
    }

    override fun handleDialogClose(dialog: DialogInterface?) {
        noteList = db.notes
        noteList.reverse()
        noteAdapter!!.setNotes(noteList)
        noteAdapter!!.notifyDataSetChanged()
    }
}