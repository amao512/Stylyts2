package kz.eztech.stylyts.presentation.adapters.filter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.*
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.filter.holders.*

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class FilterCheckAdapter : BaseAdapter() {

    companion object {
        private const val CHARACTER_TYPE = 0
        private const val OTHER_TYPE = 1
        private const val CATEGORY = 2
        private const val BRAND = 3
        private const val TYPE = 4
        private const val STYLE = 5
        private const val COLOR = 6
    }

    override fun getItemViewType(position: Int): Int {
        return when ((currentList[position] as FilterCheckModel).item) {
            is String -> CHARACTER_TYPE
            is ClothesCategoryModel -> CATEGORY
            is ClothesBrandModel -> BRAND
            is ClothesTypeModel -> TYPE
            is ClothesStyleModel -> STYLE
            is ClothesColorModel -> COLOR
            else -> OTHER_TYPE
        }
    }

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

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val view = inflateView(parent, layoutId = R.layout.item_filter_check)

        return when (viewType) {
            CATEGORY -> CategoryFilterCheckViewHolder(view, adapter = this)
            BRAND -> BrandFilterCheckViewHolder(view, adapter = this)
            TYPE -> TypeFilterCheckViewHolder(view, adapter = this)
            STYLE -> StyleFilterCheckViewHolder(view, adapter = this)
            COLOR -> ColorFilterCheckViewHolder(view, adapter = this)
            CHARACTER_TYPE -> CharacterFilterCheckViewHolder(
                itemView = inflateView(parent, R.layout.item_filter_character),
                adapter = this
            )
            else -> FilterItemCheckViewHolder(view, adapter = this)
        }
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

    fun getCheckedItemList(): List<FilterCheckModel> {
        val checkedList: MutableList<FilterCheckModel> = mutableListOf()
        val copyCurrentList: MutableList<FilterCheckModel> = mutableListOf()

        currentList.map {
            copyCurrentList.add(it as FilterCheckModel)
        }

        copyCurrentList.map {
            if (it.isChecked) {
                checkedList.add(it)
            }
        }

        return checkedList
    }
}