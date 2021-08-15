package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.holders.CollectionConstructorFilterHolder
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.search.domain.models.GenderCategory

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return when (list[0]) {
            is GenderCategory -> getGenderCategoryDiffUtil(list)
            else -> getDefaultDiffUtil(list)
        }
    }

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return CollectionConstructorFilterHolder(
            itemView = inflateView(parent, R.layout.item_constructor_filter_clothe_items),
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