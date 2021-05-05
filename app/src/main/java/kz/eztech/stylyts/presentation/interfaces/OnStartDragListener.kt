package kz.eztech.stylyts.presentation.interfaces

import androidx.recyclerview.widget.RecyclerView

interface OnStartDragListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

    fun onStopDrag()
}