package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image.view.*
import kotlinx.android.synthetic.main.item_main_image.view.image_view_item_main_image_imageholder
import kotlinx.android.synthetic.main.item_main_image_detail.view.*
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.presentation.adapters.MainImagesAdapter
import kz.eztech.stylyts.presentation.adapters.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImageAdditionalHolder(
    itemView: View,adapter: BaseAdapter): BaseViewHolder(itemView,adapter){
    override fun bindData(item: Any, position: Int) {
        item as ClothesMainModel
        with(itemView){
            frame_layout_item_main_image_holder_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(frame_layout_item_main_image_holder_container,position,item)
            }
            Glide.with(this).load(item.cover_photo).into(this.image_view_image_detail_imageholder)
        }
    }
}