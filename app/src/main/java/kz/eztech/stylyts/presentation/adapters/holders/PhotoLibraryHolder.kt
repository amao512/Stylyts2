package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_photo_library.view.*
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 22.01.2021.
 */
class PhotoLibraryHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
    override fun bindData(item: Any, position: Int) {
        item as String
        itemView.text_view_item_photo_checked_text.text = position.toString()
        Glide.with(itemView.context).load(item).into(itemView.image_view_photo_library)
    }
}