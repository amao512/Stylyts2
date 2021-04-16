package kz.eztech.stylyts.presentation.adapters.filter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
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
                return when (list[0]) {
                    is ClothesCategoryModel -> compareCategoryList(list, oldItemPosition, newItemPosition)
                    else -> compareBrandList(list, oldItemPosition, newItemPosition)
                }
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return FilterItemCheckViewHolder(view, this)
    }

    fun onCheckPosition(position: Int) {
        when (val item = currentList[position]) {
            is ClothesCategoryModel -> checkCategory(item, position)
            is ClothesBrandModel -> checkBrand(item, position)
        }

        val diffCallback = getDiffUtilCallBack(currentList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        notifyDataSetChanged()
    }

    fun onResetChecking() {
        currentList.forEach {
            (it as ClothesCategoryModel).isChecked = false
        }

        notifyDataSetChanged()
    }

    fun getCheckedCategoryList(): List<Int> {
        val checkedList: MutableList<Int> = mutableListOf()
        val copyCurrentList: MutableList<ClothesCategoryModel> = mutableListOf()

        currentList.map {
            copyCurrentList.add(it as ClothesCategoryModel)
        }

        copyCurrentList.removeAt(0)

        copyCurrentList.map {
            if (it.isChecked) {
                checkedList.add(it.id)
            }
        }

        return checkedList
    }

    fun getCheckedBrandList(): List<Int> {
        val checkedList: MutableList<Int> = mutableListOf()
        val copyCurrentList: MutableList<ClothesBrandModel> = mutableListOf()

        currentList.map {
            copyCurrentList.add(it as ClothesBrandModel)
        }

        copyCurrentList.removeAt(0)

        copyCurrentList.map {
            if (it.isChecked) {
                checkedList.add(it.id)
            }
        }

        return checkedList
    }

    private fun compareCategoryList(
        list: List<Any>,
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = (currentList[oldItemPosition] as ClothesCategoryModel).id ==
            (list[newItemPosition] as ClothesCategoryModel).id

    private fun compareBrandList(
        list: List<Any>,
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = (currentList[oldItemPosition] as ClothesBrandModel).id ==
            (list[newItemPosition] as ClothesBrandModel).id

    private fun checkCategory(
        category: ClothesCategoryModel,
        position: Int
    ) {
        val firstCategory = currentList[0] as ClothesCategoryModel

        if (position == 0) {
            firstCategory.isChecked = !firstCategory.isChecked

            currentList.forEach {
                (it as ClothesCategoryModel).isChecked = firstCategory.isChecked
            }
        } else {
            if (firstCategory.isChecked) {
                firstCategory.isChecked = false
            }

            category.isChecked = !category.isChecked
        }
    }

    private fun checkBrand(
        brand: ClothesBrandModel,
        position: Int
    ) {
        val firstBrand = currentList[0] as ClothesBrandModel

        if (position == 0) {
            firstBrand.isChecked = !firstBrand.isChecked

            currentList.forEach {
                (it as ClothesBrandModel).isChecked = firstBrand.isChecked
            }
        } else {
            if (firstBrand.isChecked) {
                firstBrand.isChecked = false
            }

            brand.isChecked = !brand.isChecked
        }
    }
}