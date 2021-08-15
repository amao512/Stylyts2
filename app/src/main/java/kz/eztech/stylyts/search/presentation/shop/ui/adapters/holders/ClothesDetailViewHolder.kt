package kz.eztech.stylyts.search.presentation.shop.ui.adapters.holders

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_clothes_detail.view.*
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImage
import kz.eztech.stylyts.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ClothesDetailViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var clothesDetailLinearLayout: LinearLayout
    private lateinit var clothesImageView: ImageView
    private lateinit var brandTitleTextView: TextView
    private lateinit var clothesTitleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var salePriceHolderLinearLayout: LinearLayout
    private lateinit var defaultPriceTextView: TextView
    private lateinit var salePriceTextView: TextView

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
            clothesDetailLinearLayout = item_clothes_detail_linear_layout
            clothesImageView = item_clothes_detail_image_view
            brandTitleTextView = item_clothes_detail_title_text_view
            clothesTitleTextView = item_clothes_detail_sub_title_text_view
            priceTextView = item_clothes_detail_price_text_view
            salePriceHolderLinearLayout = item_clothes_detail_sale_prices_linear_layout
            defaultPriceTextView = item_clothes_detail_default_price_text_view
            salePriceTextView = item_clothes_detail_sale_price_text_view
        }
    }

    private fun processClothes(
        clothesModel: ClothesModel,
        position: Int
    ) = with(clothesModel) {
        if (clothesBrand.title.isNotEmpty()) {
            brandTitleTextView.text = clothesBrand.title
        } else {
            brandTitleTextView.hide()
        }

        clothesTitleTextView.text = title

        processPrice(clothesModel = clothesModel)
        loadCoverPhoto(coverImages)

        clothesDetailLinearLayout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, clothesModel)
        }
    }

    private fun loadCoverPhoto(coverImages: List<String>) {
        coverImages.map {
            it.loadImage(target = clothesImageView)
        }
    }

    private fun processPrice(clothesModel: ClothesModel) = with(clothesModel) {
        if (clothesModel.cost != 0 && clothesModel.salePrice == 0) {
            priceTextView.text = displayPrice
            salePriceHolderLinearLayout.hide()
        } else if (salePrice != 0) {
            defaultPriceTextView.apply {
                text = displayPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            salePriceHolderLinearLayout.show()
            priceTextView.hide()
            salePriceTextView.text = displaySalePrice
        } else {
            priceTextView.hide()
            salePriceHolderLinearLayout.hide()
        }
    }
}