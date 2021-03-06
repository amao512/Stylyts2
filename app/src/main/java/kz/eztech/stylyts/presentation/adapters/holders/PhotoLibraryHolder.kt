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
        Glide.with(itemView.context).load(item).into(itemView.image_view_photo_library)
        itemView.image_view_photo_library.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(itemView.image_view_photo_library,position,item)
        }
    }

    fun bindPayloadData(item:Any,position:Int,payLoad:Int){
        item as String
        if(payLoad != -1){
            itemView.frame_layout_item_photo_library.visibility = View.VISIBLE
        }else{
            itemView.frame_layout_item_photo_library.visibility = View.GONE
        }
        Glide.with(itemView.context).load(item).into(itemView.image_view_photo_library)
        itemView.image_view_photo_library.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(itemView.image_view_photo_library,position,item)
        }
    }
}