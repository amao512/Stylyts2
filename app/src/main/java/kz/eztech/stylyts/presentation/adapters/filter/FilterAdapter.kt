package kz.eztech.stylyts.presentation.adapters.filter

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.filter.FilterItemModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.filter.holders.FilterViewHolder

class FilterAdapter : BaseAdapter() {

    override fun getLayoutId(): Int = R.layout.item_filter

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as FilterItemModel).hashCode() ==
                        (list[newItemPosition] as FilterItemModel).hashCode()
            }
        }
    }

    override fun getViewHolder(view: View): BaseViewHolder {
        return FilterViewHolder(
            itemView = view,
            adapter = this
        )
    }
}