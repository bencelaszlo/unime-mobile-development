package hu.bencelaszlo.notes;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import hu.bencelaszlo.notes.R;
import hu.bencelaszlo.notes.Model.NoteModel;
import hu.bencelaszlo.notes.Utils.DatabaseHandler;

import java.util.Objects;

public class AddNote extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText noteText;
    private Button noteSaveButton;

    private DatabaseHandler db;

    public static AddNote newInstance(){
        return new AddNote();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_note, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteText = Objects.requireNonNull(getView()).findViewById(R.id.newNoteLabel);
        noteSaveButton = getView().findViewById(R.id.newNoteButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String note = bundle.getString("note");
            noteText.setText(note);
            assert note != null;
            if(note.length()>0)
                noteSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        noteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    noteSaveButton.setEnabled(false);
                    noteSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    noteSaveButton.setEnabled(true);
                    noteSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        noteSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = noteText.getText().toString();
                if(finalIsUpdate){
                    db.updateNote(bundle.getInt("id"), text);
                }
                else {
                    NoteModel note = new NoteModel();
                    note.setNote(text);
                    db.insertNote(note);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
