package hu.bencelaszlo.notes.adapters

import android.content.Context
import hu.bencelaszlo.notes.MainActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import hu.bencelaszlo.notes.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import hu.bencelaszlo.notes.AddNote
import android.widget.TextView
import hu.bencelaszlo.notes.model.NoteModel
import hu.bencelaszlo.notes.utils.DatabaseHandler

class NoteAdapter(private val db: DatabaseHandler, private val activity: MainActivity) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private lateinit var notes: MutableList<NoteModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        db.openDatabase()
        holder.text.text = notes!![position].note
        holder.editButton.setOnClickListener { editItem(holder.adapterPosition) }
        holder.deleteButton.setOnClickListener { deleteItem(holder.adapterPosition) }
    }

    override fun getItemCount(): Int {
        return notes!!.size
    }

    val context: Context
        get() = activity

    fun setNotes(notes: MutableList<NoteModel>?) {
        if (notes != null) {
            this.notes = notes
        }
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val item = notes!![position]
        db.deleteNote(item.id)
        notes!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = notes!![position]
        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("note", item.note)
        val fragment = AddNote()
        fragment.arguments = bundle
        fragment.show(activity.supportFragmentManager, AddNote.TAG)
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var text: TextView
        var editButton: Button
        var deleteButton: Button

        init {
            text = view.findViewById(R.id.noteText)
            editButton = view.findViewById(R.id.noteEditButton)
            deleteButton = view.findViewById(R.id.noteDeleteButton)
        }
    }
}