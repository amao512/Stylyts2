package kz.eztech.stylyts.global.presentation.filter.ui.adapters.holders

import android.view.View
import android.widget.Checkable
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder
import kotlinx.android.synthetic.main.item_filter_single_check.view.*
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener

class FilterItemExpandableViewHolder(
    itemView: View,
    private val itemClickListener: UniversalViewClickListener
): CheckableChildViewHolder(itemView) {

    override fun getCheckable(): Checkable = itemView.item_filter_single_check_title_checked_text_view

    fun onBind(filterCheckModel: FilterCheckModel) {
        val category = filterCheckModel.item as ClothesCategoryModel

        with (itemView) {
            item_filter_single_check_title_checked_text_view.text = category.title
            item_filter_single_check_title_checked_text_view.isChecked = filterCheckModel.isChecked

            item_filter_single_check_title_checked_text_view.setOnClickListener {
                itemClickListener.onViewClicked(
                    view = item_filter_single_check_title_checked_text_view,
                    position = position,
                    item = filterCheckModel
                )
            }
        }
    }
}