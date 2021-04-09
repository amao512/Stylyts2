package kz.eztech.stylyts.presentation.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.holders.ShopItemListHolder

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ShopItemListAdapter : BaseAdapter() {

    override fun getLayoutId(): Int = R.layout.item_shop_item_list

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as ClothesCategoryModel).id ==
                        (list[newItemPosition] as ClothesCategoryModel).id
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return ShopItemListHolder(view, this)
    }

    fun onCheckPosition(position: Int) {
        val category = currentList[position] as ClothesCategoryModel
        val firstCategory = currentList[0] as ClothesCategoryModel

        if (position != 0 && firstCategory.isChecked && category.isChecked) {
            category.isChecked = true
        } else {
            category.isChecked = !category.isChecked
        }

        currentList.forEach {
            it as ClothesCategoryModel

            if (position == 0) {
                it.isChecked = category.isChecked
            } else {
                it.isChecked = it.id == category.id && category.isChecked
            }
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
}