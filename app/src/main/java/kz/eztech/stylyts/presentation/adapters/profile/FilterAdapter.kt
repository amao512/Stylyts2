package kz.eztech.stylyts.presentation.adapters.profile

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.profile.FilterModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.profile.holders.FilterViewHolder

class FilterAdapter : BaseAdapter() {

    override fun getLayoutId(): Int = R.layout.item_filter_group

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as FilterModel).hashCode() ==
                        (list[newItemPosition] as FilterModel).hashCode()
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