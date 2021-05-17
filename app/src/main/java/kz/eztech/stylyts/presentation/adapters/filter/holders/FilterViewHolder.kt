package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter.view.*
import kz.eztech.stylyts.domain.models.filter.FilterItemModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

class FilterViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as FilterItemModel

        with(itemView) {
            item_filter_title.text = item.title

            item_filter_root_view_frame_layout.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }
        }
    }
}