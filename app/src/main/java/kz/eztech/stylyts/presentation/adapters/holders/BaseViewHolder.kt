package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
abstract class BaseViewHolder(
    itemView: View,
    val adapter: BaseAdapter
) : RecyclerView.ViewHolder(itemView) {

    var itemListener: UniversalViewClickListener? = null

    abstract fun bindData(item: Any, position: Int)
}