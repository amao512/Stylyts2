package kz.eztech.stylyts.global.presentation.filter.ui.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter_check.view.*
import kz.eztech.stylyts.global.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.hide

class BrandFilterCheckViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as FilterCheckModel

        processBrandModel(item, position)
    }

    private fun processBrandModel(
        filterCheckModel: FilterCheckModel,
        position: Int
    ) {
        val clothesBrand = filterCheckModel.item as ClothesBrandModel

        with(itemView) {
            item_filter_check_title_checked_text_view.text = clothesBrand.title
            item_filter_check_title_checked_text_view.isChecked = filterCheckModel.isChecked

            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, filterCheckModel)
            }
            item_filter_check_color_view.hide()
        }
    }
}