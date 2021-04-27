package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.view.View
import kotlinx.android.synthetic.main.item_filter_check.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
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
        item as FilterCheckModel

        when (item.item) {
            is ClothesCategoryModel -> processCategoryModel(item, position)
            is ClothesBrandModel -> processBrandModel(item, position)
            is ClothesTypeModel -> processTypeModel(item, position)
            is ClothesStyleModel -> processStyleModel(item, position)
        }
    }

    private fun processCategoryModel(
        filterCheckModel: FilterCheckModel,
        position: Int
    ) {
        val clothesCategory = filterCheckModel.item as ClothesCategoryModel

        with(itemView) {
            fillTitle(
                title = clothesCategory.title,
                isChecked = filterCheckModel.isChecked
            )
            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, filterCheckModel)
            }
        }
    }

    private fun processBrandModel(
        filterCheckModel: FilterCheckModel,
        position: Int
    ) {
        val clothesBrand = filterCheckModel.item as ClothesBrandModel

        with(itemView) {
            fillTitle(
                title = clothesBrand.title,
                isChecked = filterCheckModel.isChecked
            )
            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, filterCheckModel)
            }
        }
    }

    private fun processTypeModel(
        filterCheckModel: FilterCheckModel,
        position: Int
    ) {
        val clothesType = filterCheckModel.item as ClothesTypeModel

        with(itemView) {
            fillTitle(
                title = clothesType.title,
                isChecked = filterCheckModel.isChecked
            )
            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, filterCheckModel)
            }
        }
    }

    private fun processStyleModel(
        filterCheckModel: FilterCheckModel,
        position: Int
    ) {
        val clothesStyle = filterCheckModel.item as ClothesStyleModel

        with(itemView) {
            fillTitle(
                title = clothesStyle.title,
                isChecked = filterCheckModel.isChecked
            )
            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, filterCheckModel)
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