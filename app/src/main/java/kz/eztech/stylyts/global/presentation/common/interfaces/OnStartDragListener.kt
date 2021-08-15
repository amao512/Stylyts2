package kz.eztech.stylyts.global.presentation.common.interfaces

import androidx.recyclerview.widget.RecyclerView

interface OnStartDragListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

    fun onStopDrag()
}