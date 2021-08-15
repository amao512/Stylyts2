package kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_shop_order_clothes.view.*
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.loadImage

class ShopOrderClothesViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var brandTitleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var clothesImageView: ImageView
    private lateinit var clothesTitleTextView: TextView
    private lateinit var secondPriceTextView: TextView
    private lateinit var countsTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as ClothesModel

        initializeViews()
        processClothes(item, position)
    }

    private fun initializeViews() {
        with(itemView) {
            brandTitleTextView = item_shop_order_clothes_brand_name_text_view
            priceTextView = item_shop_order_clothes_price_text_view
            clothesImageView = item_shop_order_clothes_image_view
            clothesTitleTextView = item_shop_order_clothes_title_text_view
            secondPriceTextView = item_shop_order_clothes_price_second_text_view
            countsTextView = item_shop_order_clothes_count_text_view
        }
    }

    private fun processClothes(
        clothes: ClothesModel,
        position: Int
    ) = with(clothes) {
        brandTitleTextView.text = clothesBrand.title
        clothesTitleTextView.text = title

        val price = if (salePrice != 0) {
            displaySalePrice
        } else {
            displayPrice
        }

        priceTextView.text = price
        secondPriceTextView.text = price

        if (constructorImage.isBlank()) {
            coverImages[0].loadImage(target = clothesImageView)
        } else {
            constructorImage.loadImage(target = clothesImageView)
        }

        clothesImageView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, clothes)
        }
    }
}