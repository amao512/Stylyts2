package kz.eztech.stylyts.presentation.adapters

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
abstract class BaseDiffUtilCallBack(
    private val oldList: List<Any?>,
    private val newList: List<Any?>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.count()
    }

    override fun getNewListSize(): Int {
        return newList.count()
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getAreContentsTheSame(oldItemPosition,newItemPosition)
    }

    abstract fun getAreContentsTheSame(oldItemPosition: Int, newItemPosition: Int):Boolean
}