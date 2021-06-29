package kz.eztech.stylyts.presentation.adapters.common.holders

import android.view.View
import kotlinx.android.synthetic.main.item_collection.view.*
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.utils.extensions.loadImageWithCenterCrop

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageViewHolder(itemView: View, adapter: BaseAdapter) :
    BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        when (item) {
            is PostModel -> processPost(item, position)
            is OutfitModel -> processOutfit(item, position)
        }
    }

    private fun processPost(
        postModel: PostModel,
        position: Int
    ) {
        if (postModel.images.isNotEmpty()) {
            loadImage(image = postModel.images[0])
        }

        with(itemView) {
            shapeable_image_view_item_collection_image.setOnClickListener { view ->
                adapter.itemClickListener?.onViewClicked(view, position, postModel)
            }
        }
    }

    private fun processOutfit(
        outfitModel: OutfitModel,
        position: Int
    ) {
        if (outfitModel.coverPhoto.isNotEmpty()) {
            loadImage(image = outfitModel.coverPhoto)
        }

        with(itemView) {
            shapeable_image_view_item_collection_image.setOnClickListener { view ->
                adapter.itemClickListener?.onViewClicked(view, position, outfitModel)
            }
        }
    }

    private fun loadImage(image: String) {
        with (itemView) {
            image.loadImageWithCenterCrop(
                target = shapeable_image_view_item_collection_image,
                withPlaceHolder = true
            )
        }
    }
}