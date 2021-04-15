package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cart_item.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
class CartHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as CartEntity

        itemView.apply {
            Glide.with(image_view_item_cart_item.context)
                .load(item.coverImage)
                .into(image_view_item_cart_item)

            text_view_item_cart_item_name.text = item.title
            text_view_item_cart_item_brand_name.text = item.brandTitle

            text_view_item_cart_item_description.text = "ID изделия ${item.id}"
//            text_view_item_cart_item_size.text = "Размер ${item.currentSize?.size}"
            text_view_item_cart_item_price.text = context.getString(
                R.string.price_tenge_text_format,
                NumberFormat.getInstance().format(item.price)
            )

            image_view_item_cart_close.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }

        }
    }
}