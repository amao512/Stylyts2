package kz.eztech.stylyts.presentation.adapters.clothes

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_clothes_detail.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import java.text.NumberFormat

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
    ) {
        if (clothesModel.clothesBrand.title.isNotEmpty()) {
            brandTitleTextView.text = clothesModel.clothesBrand.title
        } else {
            brandTitleTextView.hide()
        }

        clothesTitleTextView.text = clothesModel.title

        processPrice(clothesModel = clothesModel)
        loadCoverPhoto(clothesModel.coverImages)

        clothesDetailLinearLayout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, clothesModel)
        }
    }

    private fun loadCoverPhoto(coverImages: List<String>) {
        coverImages.map {
            Glide.with(clothesImageView.context)
                .load(it)
                .into(clothesImageView)
        }
    }

    private fun processPrice(clothesModel: ClothesModel) {
        if (clothesModel.cost != 0 && clothesModel.salePrice == 0) {
            priceTextView.apply {
                text = context.getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(clothesModel.cost),
                )
            }
            salePriceHolderLinearLayout.hide()
        } else if (clothesModel.salePrice != 0) {
            defaultPriceTextView.apply {
                text = context.getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(clothesModel.cost),
                )
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            salePriceHolderLinearLayout.show()
            priceTextView.hide()
            salePriceTextView.text = salePriceTextView.context.getString(
                R.string.price_tenge_text_format,
                NumberFormat.getInstance().format(clothesModel.salePrice),
            )
        } else {
            priceTextView.hide()
            salePriceHolderLinearLayout.hide()
        }
    }
}