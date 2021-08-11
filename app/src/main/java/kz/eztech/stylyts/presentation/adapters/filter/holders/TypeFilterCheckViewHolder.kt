package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter_check.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.hide

class TypeFilterCheckViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(item: Any, position: Int) {
        item as FilterCheckModel

        processTypeModel(item, position)
    }

    private fun processTypeModel(
        filterCheckModel: FilterCheckModel,
        position: Int
    ) {
        val clothesType = filterCheckModel.item as ClothesTypeModel

        with(itemView) {
            item_filter_check_title_checked_text_view.text = clothesType.title
            item_filter_check_title_checked_text_view.isChecked = filterCheckModel.isChecked

            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, filterCheckModel)
            }
            item_filter_check_color_view.hide()
        }
    }
}