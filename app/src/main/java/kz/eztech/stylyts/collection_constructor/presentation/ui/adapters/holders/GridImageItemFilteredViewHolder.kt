package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_collection.view.*
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.loadImage

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageItemFilteredViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as ClothesModel

        with(itemView) {
            if (item.constructorImage.isNotEmpty()) {
                item.constructorImage.loadImage(target = shapeable_image_view_item_collection_image)
            } else {
                item.coverImages[0].loadImage(target = shapeable_image_view_item_collection_image)
            }

            shapeable_image_view_item_collection_image.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }
        }
    }
}