package hu.bencelaszlo.notes

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hu.bencelaszlo.notes.adapters.NoteAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import android.graphics.drawable.Drawable
import android.graphics.drawable.ColorDrawable
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AlertDialog

class RecyclerItemTouchHelper(private val adapter: NoteAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            val builder = AlertDialog.Builder(
                adapter.context
            )
            builder.setTitle("Delete Note")
            builder.setMessage("Are you want to delete this note?")
            builder.setPositiveButton(
                "Confirm"
            ) { dialog, which -> adapter.deleteItem(position) }
            builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
                adapter.notifyItemChanged(
                    viewHolder.adapterPosition
                )
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val icon: Drawable?
        val background: ColorDrawable
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20
        if (dX > 0) {
            icon = ContextCompat.getDrawable(adapter.context, R.drawable.ic_baseline_edit)
            background =
                ColorDrawable(ContextCompat.getColor(adapter.context, R.color.colorPrimaryDark))
        } else {
            icon = ContextCompat.getDrawable(adapter.context, R.drawable.ic_baseline_delete)
            background = ColorDrawable(Color.RED)
        }
        assert(icon != null)
        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)
        icon.draw(c)
    }
}