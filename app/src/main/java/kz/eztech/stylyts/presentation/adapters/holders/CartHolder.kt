package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cart_item.view.*
import kz.eztech.stylyts.data.db.entities.CartEntity
import kz.eztech.stylyts.data.db.entities.CartMapper
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
class CartHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
    override fun bindData(item: Any, position: Int) {
        item as CartEntity
        itemView.apply {
            val model = CartMapper.mapToClotheMainModel(item)
            Glide.with(this).load(model.cover_photo).into(image_view_item_cart_item)
            text_view_item_cart_item_name.text = model.title
            text_view_item_cart_item_brand_name.text = "${model.brand?.first_name} ${model.brand?.last_name}"
            text_view_item_cart_item_description.text = "ID изделия ${item.id}"
            text_view_item_cart_item_size.text = "Размер ${model.currentSize?.size}"
            text_view_item_cart_item_price.text = "${NumberFormat.getInstance().format(model.cost)} ${model.currency}"
            image_view_item_cart_close.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it,position,item)
            }

        }
    }
}