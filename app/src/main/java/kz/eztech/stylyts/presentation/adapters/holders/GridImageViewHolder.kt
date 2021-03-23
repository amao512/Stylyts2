package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
class GridImageViewHolder(itemView: View,adapter: BaseAdapter) : BaseViewHolder(itemView,adapter){
    override fun bindData(item: Any, position: Int) {
        with(itemView){
            when(item){
                is MainResult -> {
                    Glide.with(this).load(item.cover_photo).into(shapeable_image_view_item_collection_image)
                    shapeable_image_view_item_collection_image.setOnClickListener { view ->
                        adapter.itemClickListener?.let {
                            it.onViewClicked(view,position,item)
                        }
                    }
                }
            }
            
        }
    }
}