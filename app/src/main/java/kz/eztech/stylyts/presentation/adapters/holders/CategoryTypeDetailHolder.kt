package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_category_type_detail.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesImageModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class CategoryTypeDetailHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(
        item: Any,
        position: Int
    ) {
        with(item as ClothesModel) {
            with(itemView) {
                text_view_item_category_type_detail_title.text = title
                text_view_item_category_type_detail_sub_title.text = description ?: title
                text_view_item_category_type_detail_cost.text = context.getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(cost)
                )

                loadCoverPhoto(imageModels)
                linear_layout_item_category_type_detail.setOnClickListener {
                    adapter.itemClickListener?.onViewClicked(it, position, item)
                }
            }
        }
    }

    private fun loadCoverPhoto(imageModels: List<ClothesImageModel>?) {
        with (itemView) {
            imageModels?.map {
                if (it.isCoverPhoto) {
                    Glide.with(image_view_item_category_type_detail.context)
                        .load(it.image)
                        .centerCrop()
                        .into(image_view_item_category_type_detail)
                }
            } ?: run {
                Glide.with(this)
                    .load(resources.getIdentifier("jacket", "drawable", context.packageName))
                    .into(this.image_view_item_category_type_detail)
            }
        }
    }
}