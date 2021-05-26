package kz.eztech.stylyts.presentation.adapters.collection_constructor

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.shop.GenderCategory
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.CollectionConstructorFilterHolder
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_constructor_filter_clothe_items

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return when (list[0]) {
            is GenderCategory -> getGenderCategoryDiffUtil(list)
            else -> getDefaultDiffUtil(list)
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return CollectionConstructorFilterHolder(
            itemView = view,
            adapter = this
        )
    }

    private fun getGenderCategoryDiffUtil(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as GenderCategory).isChoosen ==
                        (list[newItemPosition] as GenderCategory).isChoosen
            }
        }
    }

    private fun getDefaultDiffUtil(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition]).hashCode() ==
                        (list[newItemPosition]).hashCode()
            }
        }
    }
}