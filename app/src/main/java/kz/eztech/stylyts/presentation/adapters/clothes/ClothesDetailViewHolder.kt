package kz.eztech.stylyts.presentation.adapters.clothes

import android.graphics.Paint
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_clothes_detail.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ClothesDetailViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        with(item as ClothesModel) {
            with(itemView) {
                item_clothes_detail_title_text_view.text = clothesBrand.title
                item_clothes_detail_sub_title_text_view.text = title

                processPrice(clothesModel = item)
                loadCoverPhoto(coverImages)

                item_clothes_detail_linear_layout.setOnClickListener {
                    adapter.itemClickListener?.onViewClicked(it, position, item)
                }
            }
        }
    }

    private fun loadCoverPhoto(coverImages: List<String>) {
        with (itemView) {
            coverImages.map {
                Glide.with(item_clothes_detail_image_view.context)
                    .load(it)
                    .centerCrop()
                    .into(item_clothes_detail_image_view)
            }
        }
    }

    private fun processPrice(clothesModel: ClothesModel) {
        with (itemView) {
            item_clothes_detail_price_text_view.apply {
                text = context.getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(clothesModel.cost),
                )
            }

            if (clothesModel.salePrice != 0) {
                item_clothes_detail_sale_price_text_view.show()
                item_clothes_detail_sale_price_text_view.text = context.getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(clothesModel.salePrice),
                )
                item_clothes_detail_price_text_view.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
    }
}