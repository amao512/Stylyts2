package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter.view.*
import kz.eztech.stylyts.domain.models.profile.FilterModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

class FilterViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as FilterModel

        with(itemView) {
            item_filter_title.text = item.title
        }

        itemView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(itemView, position, item)
        }
    }
}