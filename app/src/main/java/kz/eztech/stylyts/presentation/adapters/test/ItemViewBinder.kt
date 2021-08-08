package kz.eztech.stylyts.presentation.adapters.test

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class ItemViewBinder<T, VH : RecyclerView.ViewHolder> : ItemViewDelegate<T, VH>(), IKDispatcher
{

    final override fun onCreateViewHolder(context: Context, parent: ViewGroup): VH {
        return onCreateViewHolder(context, parent)
    }

    abstract override fun onBindViewHolder(holder: VH, item: T)
}