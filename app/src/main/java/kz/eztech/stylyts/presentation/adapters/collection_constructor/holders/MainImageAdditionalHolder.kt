package kz.eztech.stylyts.presentation.adapters.collection_constructor.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image_detail.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImageAdditionalHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as ClothesModel

        with(itemView) {
            frame_layout_item_main_image_holder_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(
                    frame_layout_item_main_image_holder_container,
                    position,
                    item
                )
            }

            Glide.with(image_view_image_detail_imageholder.context)
                .load(item.constructorImage)
                .into(image_view_image_detail_imageholder)
        }
    }
}