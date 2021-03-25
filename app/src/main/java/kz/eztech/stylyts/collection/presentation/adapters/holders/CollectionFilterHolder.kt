package kz.eztech.stylyts.collection.presentation.adapters.holders

import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import kotlinx.android.synthetic.main.item_collection_filter.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.collection.domain.models.CollectionFilterModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.common.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class CollectionFilterHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as CollectionFilterModel

        with(itemView) {
            item_collection_filter_title_text_view.text = item.name

            if (item.isChosen) {
                frame_layout_item_collection_filter.background =
                    getDrawable(context, R.drawable.rounded_rectangle_gray)
            } else {
                frame_layout_item_collection_filter.background =
                    getDrawable(context, R.drawable.rounded_rectangle_white_gray_corners)
            }

            item.icon?.let {
                item_collection_filter_icon_image_view.setImageResource(it)
                item_collection_filter_icon_image_view.show()
            }

            frame_layout_item_collection_filter.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }
        }
    }
}