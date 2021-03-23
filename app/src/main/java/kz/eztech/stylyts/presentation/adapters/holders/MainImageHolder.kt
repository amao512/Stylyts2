package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image.view.*
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.presentation.adapters.MainImagesAdditionalAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.DateFormatterHelper
import kz.eztech.stylyts.presentation.utils.DateFormatterHelper.FORMAT_DATE_DD_MMMM
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImageHolder(
    itemView: View,adapter: BaseAdapter
): BaseViewHolder(itemView,adapter){
    override fun bindData(item: Any, position: Int) {
        with(itemView){
            val additionalAdapter = MainImagesAdditionalAdapter()
            additionalAdapter.itemClickListener = adapter.itemClickListener
            this.recycler_view_item_main_image_additionals_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            this.recycler_view_item_main_image_additionals_list.adapter = additionalAdapter

            with(item as MainResult) {
                clothes?.let {
                    additionalAdapter.updateList(it)
                }
                text_view_item_main_image_partner_name.text ="${author?.first_name } ${author?.last_name }"

                author?.avatar?.let {
                    text_view_item_main_image_short_name.visibility = View.GONE
                    Glide.with(itemView).load(it).into(shapeable_image_view_item_main_image_profile_avatar)
                } ?: run{
                    shapeable_image_view_item_main_image_profile_avatar.visibility = View.GONE
                    text_view_item_main_image_short_name.visibility = View.VISIBLE
                    text_view_item_main_image_short_name.text =
                        "${author?.first_name?.toUpperCase()?.get(0)}${author?.last_name?.toUpperCase()?.get(0)}"

                }
                constraint_layout_fragment_item_main_image_profile_container.setOnClickListener{thisView ->
                    adapter.itemClickListener?.onViewClicked(thisView,position,item)
                }
                button_item_main_image_change_collection.setOnClickListener{thisView ->
                    adapter.itemClickListener?.onViewClicked(thisView,position,item)
                }
    
                text_view_item_main_image_comments_cost.text = "${NumberFormat.getInstance().format(total_price)} $total_price_currency"
                text_view_item_main_image_comments_count.text = "Показать $comments_count коммент."

                text_view_item_main_image_comments_count.setOnClickListener {thisView ->
                    adapter.itemClickListener?.onViewClicked(thisView,position,item)
                }
                text_view_item_main_image_date.text = "${DateFormatterHelper.formatISO_8601(created_at,FORMAT_DATE_DD_MMMM)}"
            }

            Glide.with(this).load(item.cover_photo).into(this.image_view_item_main_image_imageholder)
            image_view_item_main_image_imageholder.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(image_view_item_main_image_imageholder,position,item)
            }

        }
    }
}