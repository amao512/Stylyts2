package kz.eztech.stylyts.presentation.adapters.collection_constructor.holders

import android.view.View
import kotlinx.android.synthetic.main.item_photo_library.view.*
import kz.eztech.stylyts.domain.models.common.PhotoLibraryModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImage
import kz.eztech.stylyts.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
class PhotoLibraryHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as PhotoLibraryModel

        item.photo.loadImage(target = itemView.image_view_photo_library)

        if (item.isMultipleChoice) {
            itemView.frame_layout_item_photo_library.show()
        } else {
            itemView.frame_layout_item_photo_library.hide()
        }

        if (item.number != 0) {
            itemView.item_photo_library_count_text_view.show()
            itemView.item_photo_library_count_text_view.text = item.number.toString()
        } else {
            itemView.item_photo_library_count_text_view.hide()
        }

        itemView.image_view_photo_library.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, item)
        }
    }

    fun bindPayloadData(
        item: Any,
        position: Int,
        payLoad: Int
    ) {
        item as PhotoLibraryModel

        if (item.number != 0) {
            itemView.item_photo_library_count_text_view.show()
            itemView.item_photo_library_count_text_view.text = item.number.toString()
        } else {
            itemView.item_photo_library_count_text_view.hide()
        }

        if (item.isMultipleChoice) {
            itemView.frame_layout_item_photo_library.show()
        } else {
            itemView.frame_layout_item_photo_library.hide()
        }

        item.photo.loadImage(target = itemView.image_view_photo_library)

        itemView.image_view_photo_library.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, item)
        }
    }
}