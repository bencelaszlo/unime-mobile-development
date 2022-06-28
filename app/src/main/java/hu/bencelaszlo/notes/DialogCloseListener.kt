package hu.bencelaszlo.notes

import android.content.DialogInterface

interface DialogCloseListener {
    fun handleDialogClose(dialog: DialogInterface?)
}