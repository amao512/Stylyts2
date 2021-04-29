package kz.eztech.stylyts.presentation.adapters.filter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.filter.holders.FilterItemCheckViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class FilterCheckAdapter : BaseAdapter() {

    override fun getLayoutId(): Int = R.layout.item_filter_check

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as FilterCheckModel).id ==
                        (list[newItemPosition] as FilterCheckModel).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return FilterItemCheckViewHolder(view, this)
    }

    fun onMultipleCheckItem(position: Int) {
        val firstFilterCheck = currentList[0] as FilterCheckModel
        val filterCheck = currentList[position] as FilterCheckModel

        if (position == 0 && firstFilterCheck.isCustom) {
            firstFilterCheck.isChecked = !firstFilterCheck.isChecked

            currentList.forEach {
                (it as FilterCheckModel).isChecked = firstFilterCheck.isChecked
            }
        } else {
            if (firstFilterCheck.isChecked && firstFilterCheck.isCustom) {
                firstFilterCheck.isChecked = false
            }

            filterCheck.isChecked = !filterCheck.isChecked
        }

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    fun onSingleCheckItem(position: Int) {
        val current = currentList[position] as FilterCheckModel

        currentList.forEach {
            it as FilterCheckModel

            it.isChecked = it.id == current.id
        }

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    fun onResetCheckedItems() {
        currentList.forEach {
            (it as FilterCheckModel).isChecked = false
        }

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }


    fun getCheckedItemIdListByRemoveFirst(): List<Int> {
        val checkedList: MutableList<Int> = mutableListOf()
        val copyCurrentList: MutableList<FilterCheckModel> = mutableListOf()

        currentList.map {
            copyCurrentList.add(it as FilterCheckModel)
        }

        copyCurrentList.removeAt(0)

        copyCurrentList.map {
            if (it.isChecked) {
                checkedList.add(it.id)
            }
        }

        return checkedList
    }

    fun getCheckedItemListByRemoveFirst(): List<FilterCheckModel> {
        val checkedList: MutableList<FilterCheckModel> = mutableListOf()
        val copyCurrentList: MutableList<FilterCheckModel> = mutableListOf()

        currentList.map {
            copyCurrentList.add(it as FilterCheckModel)
        }

        copyCurrentList.removeAt(0)

        copyCurrentList.map {
            if (it.isChecked) {
                checkedList.add(it)
            }
        }

        return checkedList
    }

    fun getCheckedItemIdList(): List<Int> {
        val checkedList: MutableList<Int> = mutableListOf()
        val copyCurrentList: MutableList<FilterCheckModel> = mutableListOf()

        currentList.map {
            copyCurrentList.add(it as FilterCheckModel)
        }

        copyCurrentList.map {
            if (it.isChecked) {
                checkedList.add(it.id)
            }
        }

        return checkedList
    }
}