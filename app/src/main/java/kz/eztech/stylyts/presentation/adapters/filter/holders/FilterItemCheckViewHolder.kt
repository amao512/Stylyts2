package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter_check.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class FilterItemCheckViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        when (item) {
            is ClothesCategoryModel -> processCategoryModel(item, position)
            is ClothesBrandModel -> processBrandModel(item, position)
        }
    }

    private fun processCategoryModel(
        clothesCategory: ClothesCategoryModel,
        position: Int
    ) {
        with(itemView) {
            fillTitle(
                title = clothesCategory.title,
                isChecked = clothesCategory.isChecked
            )
            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, clothesCategory)
            }
        }
    }

    private fun processBrandModel(
        clothesBrand: ClothesBrandModel,
        position: Int
    ) {
        with(itemView) {
            fillTitle(
                title = clothesBrand.title,
                isChecked = clothesBrand.isChecked
            )
            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, clothesBrand)
            }
        }
    }

    private fun fillTitle(title: String?, isChecked: Boolean) {
        with(itemView) {
            item_filter_check_title_checked_text_view.text = title
            item_filter_check_title_checked_text_view.isChecked = isChecked
        }
    }
}