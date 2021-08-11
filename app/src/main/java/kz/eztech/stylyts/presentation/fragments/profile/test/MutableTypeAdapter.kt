package kz.eztech.stylyts.presentation.fragments.profile.test

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class MutableTypeAdapter @JvmOverloads constructor(
    open val initialCapacity: Int = 0,
    open var types: MutableTypes = MutableTypes(initialCapacity)
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val items: MutableList<Any> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return types.getType(viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        types.getType(position).onBind(items[position])
    }

    override fun getItemCount(): Int = types.size

    fun register(type: BaseViewHolder) {
        types.register(type)
    }

    fun updateList(list: List<Any>) {
        this.items.clear()
        this.items.addAll(list)

        notifyDataSetChanged()
    }
}