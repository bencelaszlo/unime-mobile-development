package hu.bencelaszlo.notes.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hu.bencelaszlo.notes.Model.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "notesDb";
    private static final String NOTES_TABLE = "notes";
    private static final String ID = "id";
    private static final String NOTE = "note";
    private static final String CREATE_NOTE_TABLE = "CREATE TABLE " + NOTES_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE + " TEXT)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertNote(NoteModel note){
        ContentValues cv = new ContentValues();
        cv.put(NOTE, note.getNote());
        db.insert(NOTES_TABLE, null, cv);
    }

    public List<NoteModel> getNotes(){
        List<NoteModel> notes = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(NOTES_TABLE, null, null, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        NoteModel note = new NoteModel();
                        note.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        note.setNote(cursor.getString(cursor.getColumnIndex(NOTE)));
                        notes.add(note);
                    }
                    while(cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return notes;
    }

    public void updateNote(int id, String text) {
        ContentValues values = new ContentValues();
        values.put(NOTE, text);
        db.update(NOTES_TABLE, values, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteNote(int id){
        db.delete(NOTES_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
