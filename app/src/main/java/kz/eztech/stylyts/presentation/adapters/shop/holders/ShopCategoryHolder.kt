package kz.eztech.stylyts.presentation.adapters.shop.holders

import android.view.View
import kotlinx.android.synthetic.main.item_shop_category.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.loadImage

/**
 * Created by Ruslan Erdenoff on 26.11.2020.
 */
class ShopCategoryHolder(
    itemView: View,
    adapter: BaseAdapter,
    private val gender: Int
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        with(item as ClothesTypeModel) {
            with(itemView) {
                getCoverPhoto(clothesTypeModel = item).loadImage(
                    target = image_view_item_shop_fragment
                )

                text_view_item_shop_fragment.text = title
                constraint_layout_item_shop_category.setOnClickListener {
                    adapter.itemClickListener?.onViewClicked(it, position, item)
                }
            }
        }
    }

    private fun getCoverPhoto(clothesTypeModel: ClothesTypeModel): String {
        with(clothesTypeModel) {
            return when (gender) {
                0 -> menCoverPhoto
                else -> womenCoverPhoto
            }
        }
    }
}