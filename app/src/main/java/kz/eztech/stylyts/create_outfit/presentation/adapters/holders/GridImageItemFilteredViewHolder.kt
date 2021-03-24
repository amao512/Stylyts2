package kz.eztech.stylyts.create_outfit.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

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
        item as ClothesMainModel

        with(itemView) {
            Glide.with(this)
                .load(item.cover_photo)
                .into(this.shapeable_image_view_item_collection_image)

            shapeable_image_view_item_collection_image.setOnClickListener {
                adapter.itemClickListener?.let { listener ->
                    listener.onViewClicked(it, position, item)
                }
            }
        }
    }
}