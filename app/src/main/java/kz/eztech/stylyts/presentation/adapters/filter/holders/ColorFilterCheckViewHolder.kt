package kz.eztech.stylyts.presentation.adapters.filter.holders

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_filter_check.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesColorModel
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.ColorEnum
import kz.eztech.stylyts.presentation.utils.extensions.show

class ColorFilterCheckViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(item: Any, position: Int) {
        item as FilterCheckModel

        processColorModel(item, position)
    }

    private fun processColorModel(
        filterCheckModel: FilterCheckModel,
        position: Int
    ) {
        val colorModel = filterCheckModel.item as ClothesColorModel

        with(itemView) {
            item_filter_check_title_checked_text_view.text = colorModel.title
            item_filter_check_title_checked_text_view.isChecked = filterCheckModel.isChecked

            item_filter_check_color_view.setBackgroundColor(Color.parseColor(colorModel.color))
            item_filter_check_color_view.show()

            if (colorModel.title == ColorEnum.WHITE.title) {
                item_filter_check_color_view.strokeWidth = 1f
                item_filter_check_color_view.strokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                    item_filter_check_color_view.context,
                    R.color.app_dark_blue_gray_half
                ))
            }

            item_filter_check_title_checked_text_view.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, filterCheckModel)
            }
        }
    }
}