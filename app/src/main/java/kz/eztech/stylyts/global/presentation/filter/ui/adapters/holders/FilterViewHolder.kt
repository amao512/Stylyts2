package kz.eztech.stylyts.global.presentation.filter.ui.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter.view.*
import kz.eztech.stylyts.global.domain.models.filter.FilterItemModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder

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