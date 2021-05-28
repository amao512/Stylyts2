package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_filter_character.view.*
import kotlinx.android.synthetic.main.item_filter_check.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.*
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.ColorEnum
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.util.*

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
            is ClothesColorModel -> processColorModel(item, position)
            is String -> processCharacter(item, position)
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

    private fun processColorModel(
        filterCheckModel: FilterCheckModel,
        position: Int
    ) {
        val colorModel = filterCheckModel.item as ClothesColorModel

        with(itemView) {
            fillTitle(
                title = colorModel.title.capitalize(Locale.getDefault()),
                isChecked = filterCheckModel.isChecked
            )

            item_filter_check_color_view.setBackgroundColor(Color.parseColor(colorModel.color))
            item_filter_check_color_view.show()

            if (colorModel.title == ColorEnum.WHITE.title) {
                item_filter_check_color_view.strokeWidth = 1f
                item_filter_check_color_view.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(
                    item_filter_check_color_view.context,
                    R.color.app_dark_blue_gray_half
                ))
            }

            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, filterCheckModel)
            }
        }
    }

    private fun processCharacter(
        item: FilterCheckModel,
        position: Int
    ) {
        with (itemView) {
            item_filter_character_text_view.text = item.item as String
        }
    }

    private fun fillTitle(title: String?, isChecked: Boolean) {
        with(itemView) {
            item_filter_check_title_checked_text_view.text = title
            item_filter_check_title_checked_text_view.isChecked = isChecked
        }
    }
}