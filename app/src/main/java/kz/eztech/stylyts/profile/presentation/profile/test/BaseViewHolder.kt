package kz.eztech.stylyts.profile.presentation.profile.test

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun <T> onBind(data: T)
}