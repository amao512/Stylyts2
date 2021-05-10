package kz.eztech.stylyts.presentation.adapters.collection.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection.view.*
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

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
            Glide.with(shapeable_image_view_item_collection_image.context)
                .load(item.coverPhoto)
                .into(shapeable_image_view_item_collection_image)

            shapeable_image_view_item_collection_image.setOnClickListener { view ->
                adapter.itemClickListener?.onViewClicked(view, position, item)
            }
        }
    }
}