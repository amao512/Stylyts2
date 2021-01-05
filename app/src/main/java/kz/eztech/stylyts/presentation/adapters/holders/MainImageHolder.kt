package kz.eztech.stylyts.presentation.adapters.holders

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image.view.*
import kz.eztech.stylyts.domain.models.MainImageAdditionalModel
import kz.eztech.stylyts.domain.models.MainImageModel
import kz.eztech.stylyts.presentation.adapters.MainImagesAdapter
import kz.eztech.stylyts.presentation.adapters.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImageHolder(
    itemView: View,adapter: BaseAdapter): BaseViewHolder(itemView,adapter){
    override fun bindData(item: Any, position: Int) {
        with(itemView){
            val additionalAdapter = MainImagesAdditionalAdapter()
            this.recycler_view_item_main_image_additionals_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            this.recycler_view_item_main_image_additionals_list.adapter = additionalAdapter

            with(item as MainImageModel) {
                additionals?.let {
                    additionalAdapter.updateList(additionals)
                }
                text_view_item_main_image_partner_name.text = item.name
                constraint_layout_fragment_item_main_image_profile_container.setOnClickListener{thisView ->
                    adapter.itemClickListener?.onViewClicked(thisView,position,item)
                }
                button_item_main_image_change_collection.setOnClickListener{thisView ->
                    adapter.itemClickListener?.onViewClicked(thisView,position,item)
                }
            }

            Glide.with(this).load(
                resources.getIdentifier("auth_bg", "drawable", context.packageName)
            ).into(this.image_view_item_main_image_imageholder)

        }
    }
}