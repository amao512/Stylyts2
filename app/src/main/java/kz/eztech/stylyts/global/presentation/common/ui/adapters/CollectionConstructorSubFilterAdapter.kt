package kz.eztech.stylyts.global.presentation.common.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.CollectionConstructorSubFilterHolder

/**
 * Created by Ruslan Erdenoff on 09.02.2021.
 */

class CollectionConstructorSubFilterAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
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

    override fun getViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return CollectionConstructorSubFilterHolder(
            itemView = inflateView(parent, R.layout.item_constructor_filter_clothe_sub_items),
            adapter = this
        )
    }
}