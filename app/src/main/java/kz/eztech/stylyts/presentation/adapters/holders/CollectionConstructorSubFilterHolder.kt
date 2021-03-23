package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import kotlinx.android.synthetic.main.item_constructor_filter_clothe_sub_items.view.*
import kz.eztech.stylyts.domain.models.ClothesTypes
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 09.02.2021.
 */
class CollectionConstructorSubFilterHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
	override fun bindData(item: Any, position: Int) {
		item as ClothesTypes
		with(itemView){
			text_view_item_constructor_filter_clothe_sub_item_name.text = item.title
			if(item.isChosen){
				image_view_item_constructor_filter_clothe_sub_items.visibility = View.VISIBLE
			}else{
				image_view_item_constructor_filter_clothe_sub_items.visibility = View.GONE
			}
			frame_layout_item_constructor_filter_clothe_sub_items_container.setOnClickListener {
				adapter.itemClickListener?.onViewClicked(it,position,item)
			}
		}
	}
}