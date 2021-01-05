package kz.eztech.stylyts.presentation.adapters.holders

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_constructor_category_item.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopItemHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter),UniversalViewClickListener {

	override fun bindData(item: Any, position: Int) {
		with(item as ClothesTypeDataModel){
			with(itemView){
				Glide.with(this).load(
					"http://178.170.221.31:8000$cover_photo").into(this.image_view_item_collection_constructor_category_item_image_holder)
				text_view_item_collection_constructor_category_item_title.text = title

				image_view_item_collection_constructor_category_item_image_holder.apply {
					tag = "image_view_item_collection_constructor_category_item_image_holder_$position"
					setOnLongClickListener{
						this@CollectionConstructorShopItemHolder.onViewClicked(it,position,item)
						true
					}
					adapter.itemClickListener?.onViewClicked(this,position,item)
				}
			}
		}
	}

	override fun onViewClicked(view: View, position: Int, item: Any?) {
		item as ClothesTypeDataModel
		val intent = Intent()
		intent.putExtra("clothesmodel",item)
		initializeDrag(view,intent)
	}


	
	private fun initializeDrag(v: View,intent: Intent){
		val item = ClipData.Item(v.tag as? CharSequence ,intent,null)
		val dragData = ClipData(
				v.tag as? CharSequence,
				arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
				item
		)
		val myShadow = MyDragShadowBuilder(v)
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			v.startDragAndDrop(
					dragData,
					myShadow,
					v,
					0
			)
		} else {
			v.startDrag(
					dragData,
					myShadow,
					v,
					0
			)
		}
	}
	
	private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {
		private val currentView = v
		private var mScaleFactor: Point? = null
		// Defines a callback that sends the drag shadow dimensions and touch point back to the
		// system.
		override fun onProvideShadowMetrics(size: Point, touch: Point) {
			// Sets the width of the shadow to half the width of the original View
			val width: Int = view.width * 3
			
			// Sets the height of the shadow to half the height of the original View
			val height: Int = view.height * 3
			
			
			
			// Sets the size parameter's width and height values. These get back to the system
			// through the size parameter.
			size.set(width, height)
			mScaleFactor = size;
			currentView.requestLayout()
			
			// Sets the touch point's position to be in the middle of the drag shadow
			touch.set(width / 2, height / 2)
		}
		
		// Defines a callback that draws the drag shadow in a Canvas that the system constructs
		// from the dimensions passed in onProvideShadowMetrics().
		override fun onDrawShadow(canvas: Canvas) {
			// Draws the ColorDrawable in the Canvas passed in from the system.
			mScaleFactor?.let {
				canvas.scale(
						3f, 3f
				)
			}
			
			currentView.draw(canvas)
		}
	}
}