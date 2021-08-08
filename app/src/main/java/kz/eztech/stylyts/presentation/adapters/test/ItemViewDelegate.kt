package kz.eztech.stylyts.presentation.adapters.test

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class ItemViewDelegate<T, VH : RecyclerView.ViewHolder> {

    internal var _adapter: MultiTypeAdapter? = null

    val adapter: MultiTypeAdapter
        get() {
            if (_adapter == null) {
                throw IllegalStateException(
                    "This $this has not been attached to MultiTypeAdapter yet."
                )
            }

            return _adapter!!
        }

    var adapterItems: List<Any>
        get() = adapter.items
        set(value) {
            adapter.items = value
        }

    abstract fun onCreateViewHolder(context: Context, parent: ViewGroup): VH

    abstract fun onBindViewHolder(holder: VH, item: T)

    open fun onBindViewHolder(holder: VH, item: T, payloads: List<Any>) {
        onBindViewHolder(holder, item)
    }

    fun getPosition(holder: RecyclerView.ViewHolder): Int {
        return holder.adapterPosition
    }

    fun getItemId(item: T): Long = RecyclerView.NO_ID

    open fun onViewRecycled(holder: VH) {}

    open fun onFailedRecycleView(holder: VH): Boolean {
        return false
    }

    open fun onViewAttached(holder: VH) {}

    open fun onViewAttachedToWindow(holder: VH) {}

    open fun onViewDetachedFromWindow(holder: VH) {}
}