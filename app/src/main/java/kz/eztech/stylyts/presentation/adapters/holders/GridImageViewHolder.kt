package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageViewHolder(itemView: View, adapter: BaseAdapter) :
    BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        with(itemView) {
            when (item) {
                is PostModel -> {
                    if (item.images.isNotEmpty()) {
                        Glide.with(shapeable_image_view_item_collection_image.context)
                            .load(item.images[0])
                            .centerCrop()
                            .into(shapeable_image_view_item_collection_image)
                    }

                    shapeable_image_view_item_collection_image.setOnClickListener { view ->
                        adapter.itemClickListener?.onViewClicked(view, position, item)
                    }
                }
            }
        }
    }
}