package kz.eztech.stylyts.presentation.adapters.clothes

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_clothes_detail.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
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
                item_clothes_detail_title_text_view.text = title
                item_clothes_detail_sub_title_text_view.text = description
                item_clothes_detail_cost_text_view.text = context.getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(cost)
                )

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
}