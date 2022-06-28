package hu.bencelaszlo.notes

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.widget.EditText
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import android.text.TextWatcher
import android.text.Editable
import android.content.DialogInterface
import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.Button
import hu.bencelaszlo.notes.model.NoteModel
import hu.bencelaszlo.notes.utils.DatabaseHandler
import java.util.*

class AddNote : BottomSheetDialogFragment() {
    private var noteText: EditText? = null
    private var noteSaveButton: Button? = null
    private lateinit var db: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_note, container, false)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteText = Objects.requireNonNull(getView())?.findViewById(R.id.newNoteLabel)
        noteSaveButton = getView()!!.findViewById(R.id.newNoteButton)
        var isUpdate = false
        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val note = bundle.getString("note")
            noteText?.setText(note)
            assert(note != null)
            if (note!!.isNotEmpty()) noteSaveButton?.setTextColor(
                ContextCompat.getColor(
                    Objects.requireNonNull(
                        context
                    )!!, R.color.colorPrimaryDark
                )
            )
        }
        db = DatabaseHandler(activity)
        db.openDatabase()
        noteText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    noteSaveButton?.isEnabled = false
                    noteSaveButton?.setTextColor(Color.GRAY)
                } else {
                    noteSaveButton?.isEnabled = true
                    noteSaveButton?.setTextColor(
                        ContextCompat.getColor(
                            Objects.requireNonNull(
                                context
                            )!!, R.color.colorPrimaryDark
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val finalIsUpdate = isUpdate
        noteSaveButton?.setOnClickListener(View.OnClickListener {
            val text = noteText?.text.toString()
            if (finalIsUpdate) {
                db.updateNote(bundle!!.getInt("id"), text)
            } else {
                val note = NoteModel()
                note.note = text
                db.insertNote(note)
            }
            dismiss()
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        val activity: Activity? = activity
        if (activity is DialogCloseListener) (activity as DialogCloseListener).handleDialogClose(
            dialog
        )
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): AddNote {
            return AddNote()
        }
    }
}