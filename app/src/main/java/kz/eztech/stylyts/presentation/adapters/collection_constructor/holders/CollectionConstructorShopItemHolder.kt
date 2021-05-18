package kz.eztech.stylyts.presentation.adapters.collection_constructor.holders

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_collection_constructor_clothes.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.CollectionConstructorShopItemAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopItemHolder(
	itemView: View,
	adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    @SuppressLint("ClickableViewAccessibility")
    override fun bindData(
		item: Any,
		position: Int
	) {
        with(item as ClothesModel) {
            with(itemView) {
                val doubleTapGesture = getDoubleTapGesture(
					clothesModel = item,
					position = position
				)

				if (coverImages.isNotEmpty()) {
					Glide.with(image_view_item_collection_constructor_clothes_item_image_holder.context)
						.load(coverImages[0])
						.into(image_view_item_collection_constructor_clothes_item_image_holder)
				}

				if (title.toCharArray().size > 7) {
					text_view_item_collection_constructor_clothes_item_title.text = "${title.substring(0, 7)}..."
				} else {
					text_view_item_collection_constructor_clothes_item_title.text = title
				}

				if (isChosen) {
					image_view_item_collection_constructor_clothes_item_image_chooser.show()
				} else {
					image_view_item_collection_constructor_clothes_item_image_chooser.hide()
				}

				image_view_item_collection_constructor_clothes_item_image_holder.apply {
                    tag = "image_view_item_collection_constructor_category_item_image_holder_$position"

                    setOnTouchListener { _, event ->
                        doubleTapGesture.onTouchEvent(event)
                    }
                }
            }
        }
    }

	private fun getDoubleTapGesture(
		clothesModel: ClothesModel,
		position: Int
	): GestureDetector {
		adapter as CollectionConstructorShopItemAdapter

		with(itemView) {
			return GestureDetector(
				context,
				object : GestureDetector.SimpleOnGestureListener() {
					override fun onDown(e: MotionEvent?): Boolean {
						return true
					}

					override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
						adapter.itemClickListener?.onViewClicked(
							image_view_item_collection_constructor_clothes_item_image_holder,
							position,
							clothesModel
						)

						return true
					}

					override fun onDoubleTap(e: MotionEvent?): Boolean {
						adapter.itemDoubleClickListener?.onViewDoubleClicked(
							image_view_item_collection_constructor_clothes_item_image_holder,
							position,
							clothesModel
						)

						return true
					}
				}
			)
		}
	}
}