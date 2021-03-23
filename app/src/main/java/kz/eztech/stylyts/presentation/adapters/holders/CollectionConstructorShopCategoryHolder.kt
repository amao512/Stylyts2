package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_constructor_category_item.view.*
import kz.eztech.stylyts.domain.models.ClothesTypes
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopCategoryHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter){
	override fun bindData(item: Any, position: Int) {
		when(item){
			is GenderCategory -> {
				with(item){
					with(itemView){
						if(isExternal){
							image_view_item_collection_constructor_category_item_image_holder.setImageResource(externalImageId)
							text_view_item_collection_constructor_category_item_title.text = title
							
							image_view_item_collection_constructor_category_item_image_holder.setOnClickListener {
								adapter.itemClickListener?.onViewClicked(it,position,item)
							}
						}else{
							Glide.with(this).load(
									cover_image).into(this.image_view_item_collection_constructor_category_item_image_holder)
							text_view_item_collection_constructor_category_item_title.text = title
							
							image_view_item_collection_constructor_category_item_image_holder.setOnClickListener {
								adapter.itemClickListener?.onViewClicked(it,position,item)
							}
							if(isChoosen){
								image_view_item_collection_constructor_category_item_image_chooser.visibility = View.VISIBLE
							}else{
								image_view_item_collection_constructor_category_item_image_chooser.visibility = View.GONE
							}
						}
						
					}
				}
			}
			is ClothesTypes -> {
				with(item){
					with(itemView){
						Glide.with(this).load(
								constructor_icon)
							.into(this.image_view_item_collection_constructor_category_item_image_holder)
						text_view_item_collection_constructor_category_item_title.text = title

						image_view_item_collection_constructor_category_item_image_holder.setOnClickListener {
							adapter.itemClickListener?.onViewClicked(it,position,item)
						}
					}
				}
			}
		}

	}

}