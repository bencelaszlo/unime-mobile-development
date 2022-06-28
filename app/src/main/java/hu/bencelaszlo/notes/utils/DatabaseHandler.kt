package hu.bencelaszlo.notes.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hu.bencelaszlo.notes.model.NoteModel
import java.util.ArrayList

class DatabaseHandler(context: Context?) : SQLiteOpenHelper(context, NAME, null, VERSION) {
    private lateinit var db: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_NOTE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS $NOTES_TABLE")
        onCreate(db)
    }

    fun openDatabase() {
        db = this.writableDatabase
    }

    fun insertNote(note: NoteModel) {
        val cv = ContentValues()
        cv.put(NOTE, note.note)
        db.insert(NOTES_TABLE, null, cv)
    }

    val notes: MutableList<NoteModel>
        @SuppressLint("Range")
        get() {
            val notes: MutableList<NoteModel> = ArrayList<NoteModel>()
            var cursor: Cursor? = null
            db.beginTransaction()
            try {
                cursor = db.query(NOTES_TABLE, null, null, null, null, null, null, null)
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            val note = NoteModel()
                            note.id = cursor.getInt(cursor.getColumnIndex(ID))
                            note.note = cursor.getString(cursor.getColumnIndex(NOTE))
                            notes.add(note)
                        } while (cursor.moveToNext())
                    }
                }
            } finally {
                db.endTransaction()
                assert(cursor != null)
                cursor!!.close()
            }
            return notes
        }

    fun updateNote(id: Int, text: String?) {
        val values = ContentValues()
        values.put(NOTE, text)
        db.update(NOTES_TABLE, values, "$ID= ?", arrayOf(id.toString()))
    }

    fun deleteNote(id: Int) {
        db.delete(NOTES_TABLE, "$ID= ?", arrayOf(id.toString()))
    }

    companion object {
        private const val VERSION = 1
        private const val NAME = "notesDb"
        private const val NOTES_TABLE = "notes"
        private const val ID = "id"
        private const val NOTE = "note"
        private const val CREATE_NOTE_TABLE =
            "CREATE TABLE $NOTES_TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, $NOTE TEXT)"
    }
}