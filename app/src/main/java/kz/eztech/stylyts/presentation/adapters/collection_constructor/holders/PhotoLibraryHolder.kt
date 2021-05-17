package kz.eztech.stylyts.presentation.adapters.collection_constructor.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_photo_library.view.*
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

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
        item as String

        Glide.with(itemView.context)
            .load(item)
            .into(itemView.image_view_photo_library)

        itemView.image_view_photo_library.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(
                itemView.image_view_photo_library,
                position,
                item
            )
        }
    }

    fun bindPayloadData(
        item: Any,
        position: Int,
        payLoad: Int
    ) {
        item as String

        if (payLoad != -1) {
            itemView.item_photo_library_count_text_view.show()
            itemView.item_photo_library_count_text_view.text = payLoad.toString()
        } else {
            itemView.item_photo_library_count_text_view.hide()
        }

        Glide.with(itemView.context)
            .load(item)
            .into(itemView.image_view_photo_library)

        itemView.image_view_photo_library.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(
                itemView.image_view_photo_library,
                position,
                item
            )
        }
    }
}