package kz.eztech.stylyts.presentation.adapters.profile.holders

import android.view.View
import android.widget.Checkable
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import kotlinx.android.synthetic.main.item_filter_item.view.*
import kz.eztech.stylyts.domain.models.profile.FilterItemModel

class FilterItemViewHolder(
    itemView: View
): CheckableChildViewHolder(itemView) {

    override fun getCheckable(): Checkable = itemView.item_filter_item_title_checked_text_view

    fun onBind(filterItemModel: FilterItemModel) {
        with (itemView) {
            item_filter_item_title_checked_text_view.text = filterItemModel.title
        }
    }
}