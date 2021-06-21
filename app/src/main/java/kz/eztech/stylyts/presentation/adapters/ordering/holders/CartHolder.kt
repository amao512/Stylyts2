package kz.eztech.stylyts.presentation.adapters.ordering.holders

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_cart_item.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.cart.CartEntity
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.loadImage
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
class CartHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var clothesImageView: ImageView
    private lateinit var clothesTitleTextView: TextView
    private lateinit var brandTitleTextView: TextView
    private lateinit var clothesIdTextView: TextView
    private lateinit var sizeHolderFrameLayout: FrameLayout
    private lateinit var countsHolderFrameLayout: FrameLayout
    private lateinit var sizeTextView: TextView
    private lateinit var countTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var clothesRemoveImageView: ImageView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as CartEntity

        initializeViews()
        processCart(cart = item, position)
    }

    private fun initializeViews() {
        with(itemView) {
            clothesImageView = item_cart_clothes_image_view
            clothesTitleTextView = item_cart_clothes_title_text_view
            brandTitleTextView = item_cart_brand_title_text_view
            clothesIdTextView = item_cart_clothes_id_text_view
            sizeHolderFrameLayout = frame_layout_item_cart_item_size
            countsHolderFrameLayout = frame_layout_item_cart_item_count
            sizeTextView = item_cart_clothes_size_text_view
            countTextView = text_view_item_cart_item_count
            priceTextView = item_cart_price_text_view
            clothesRemoveImageView = item_cart_clothes_remove_image_view
        }
    }

    private fun processCart(
        cart: CartEntity,
        position: Int
    ) {
        cart.coverImage?.loadImage(target = clothesImageView)

        clothesTitleTextView.text = cart.title
        brandTitleTextView.text = cart.brandTitle

        clothesIdTextView.text = "ID изделия ${cart.id}"
        sizeTextView.text = "Размер ${cart.size ?: ""}"
        countTextView.text = "Кол-во ${cart.count}"

        if (cart.salePrice != 0) {
            priceTextView.text = priceTextView.context.getString(
                R.string.price_tenge_text_format,
                NumberFormat.getInstance().format(cart.salePrice)
            )
        } else {
            priceTextView.text = priceTextView.context.getString(
                R.string.price_tenge_text_format,
                NumberFormat.getInstance().format(cart.price)
            )
        }

        clothesImageView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, cart)
        }

        clothesTitleTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, cart)
        }

        clothesRemoveImageView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, cart)
        }

        sizeHolderFrameLayout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, cart)
        }

        countsHolderFrameLayout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, cart)
        }
    }
}