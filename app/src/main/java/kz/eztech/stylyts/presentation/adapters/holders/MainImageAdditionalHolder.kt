package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image.view.*
import kotlinx.android.synthetic.main.item_main_image.view.image_view_item_main_image_imageholder
import kotlinx.android.synthetic.main.item_main_image_detail.view.*
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
        with(itemView){
            Glide.with(this).load(
                resources.getIdentifier("jacket", "drawable", context.packageName)
            ).into(this.image_view_image_detail_imageholder)
        }
    }
}