package kz.eztech.stylyts.presentation.adapters.ordering.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_shop_order_clothes.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.loadImage

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
    ) {
        brandTitleTextView.text = clothes.clothesBrand.title
        clothesTitleTextView.text = clothes.title

        val price = priceTextView.context.getString(
            R.string.price_tenge_text_format,
            if (clothes.salePrice != 0) {
                clothes.salePrice.toString()
            } else {
                clothes.cost.toString()
            }
        )

        priceTextView.text = price
        secondPriceTextView.text = price

        if (clothes.constructorImage.isBlank()) {
            clothes.coverImages[0].loadImage(target = clothesImageView)
        } else {
            clothes.constructorImage.loadImage(target = clothesImageView)
        }

        clothesImageView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, clothes)
        }
    }
}