package kz.eztech.stylyts.common.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_category_type_detail.view.*
import kz.eztech.stylyts.constructor.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import java.text.NumberFormat

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class CategoryTypeDetailHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
	override fun bindData(item: Any, position: Int) {
		with(item as ClothesTypeDataModel){
			with(itemView){
				text_view_item_category_type_detail_title.text = title
				text_view_item_category_type_detail_sub_title.text = clothes_types?.title ?: title
				text_view_item_category_type_detail_cost.text = NumberFormat.getInstance().format(cost) + " тг."
				cover_photo?.let {
					Glide.with(this).load("http://178.170.221.31:8000$cover_photo").into(this.image_view_item_category_type_detail)
				}?:run{
					Glide.with(this).load(resources.getIdentifier("jacket", "drawable", context.packageName)).into(this.image_view_item_category_type_detail)
				}
				linear_layout_item_category_type_detail.setOnClickListener {
					adapter.itemClickListener?.onViewClicked(it,position,item)
				}
			}
		}
	}
}