package kz.eztech.stylyts.presentation.adapters.collection_constructor.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

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
            Glide.with(shapeable_image_view_item_collection_image.context)
                .load(item.constructorImage)
                .into(shapeable_image_view_item_collection_image)

            shapeable_image_view_item_collection_image.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }
        }
    }
}