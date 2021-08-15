package kz.eztech.stylyts.global.presentation.collection.ui.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_collection.view.*
import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.loadImage

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class OutfitViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(item: Any, position: Int) {

        item as OutfitModel

        with(itemView) {
            item.coverPhoto.loadImage(target = shapeable_image_view_item_collection_image)

            shapeable_image_view_item_collection_image.setOnClickListener { view ->
                adapter.itemClickListener?.onViewClicked(view, position, item)
            }
        }
    }
}