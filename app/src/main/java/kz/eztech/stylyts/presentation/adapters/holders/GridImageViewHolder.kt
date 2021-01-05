package kz.eztech.stylyts.presentation.adapters.holders

import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kotlinx.android.synthetic.main.item_main_image.view.*
import kotlinx.android.synthetic.main.item_main_image_detail.view.*
import kotlinx.android.synthetic.main.item_main_image_detail.view.image_view_image_detail_imageholder
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageViewHolder(itemView: View,adapter:BaseAdapter) : BaseViewHolder(itemView,adapter){
    override fun bindData(item: Any, position: Int) {
        with(itemView){
            Glide.with(this).load(
                resources.getIdentifier("auth_bg", "drawable", context.packageName)
            ).into(this.shapeable_image_view_item_collection_image)
            setOnClickListener { view ->
                Log.wtf("GridImageViewHolder","CLICKED")
                adapter.itemClickListener?.let {
                    it.onViewClicked(view,position,"")
                }
            }
            
        }
    }
}