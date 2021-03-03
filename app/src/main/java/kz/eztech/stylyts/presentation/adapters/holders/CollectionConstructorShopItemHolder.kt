package kz.eztech.stylyts.presentation.adapters.holders

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_constructor_category_item.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.adapters.CollectionConstructorShopItemAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopItemHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {

	@SuppressLint("ClickableViewAccessibility")
	override fun bindData(item: Any, position: Int) {
		adapter as CollectionConstructorShopItemAdapter
		with(item as ClothesMainModel){
			with(itemView){
				val doubleTapGesture = GestureDetector(context, object :GestureDetector.SimpleOnGestureListener(){
					override fun onDown(e: MotionEvent?): Boolean {
						return true
					}
					
					override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
						adapter.itemClickListener?.let {
							it.onViewClicked(image_view_item_collection_constructor_category_item_image_holder,position,item)
						}
						return true
					}
					
					override fun onDoubleTap(e: MotionEvent?): Boolean {
						adapter.itemDoubleClickListener?.onViewDoubleClicked(image_view_item_collection_constructor_category_item_image_holder,position,item)
						return true
					}
					
				})
				
				Glide.with(this).load(
					constructor_photo).into(this.image_view_item_collection_constructor_category_item_image_holder)
				text_view_item_collection_constructor_category_item_title.text = title

				image_view_item_collection_constructor_category_item_image_holder.apply {
					tag = "image_view_item_collection_constructor_category_item_image_holder_$position"
					setOnTouchListener { v, event ->
						doubleTapGesture.onTouchEvent(event)
					}
				}
			}
		}
	}
}