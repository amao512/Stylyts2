package kz.eztech.stylyts.presentation.adapters.collection_constructor.holders

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import androidx.core.view.MotionEventCompat
import kotlinx.android.synthetic.main.item_main_image_detail.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.ItemTouchHelperViewHolder
import kz.eztech.stylyts.presentation.interfaces.OnStartDragListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImage
import kz.eztech.stylyts.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class ClothesAdditionalHolder(
    itemView: View,
    adapter: BaseAdapter,
    private val onStartDragListener: OnStartDragListener? = null
) : BaseViewHolder(itemView, adapter), ItemTouchHelperViewHolder {

    @SuppressLint("ClickableViewAccessibility")
    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as ClothesModel

        with(itemView) {
            frame_layout_item_main_image_holder_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, item)
            }

            with (item) {
                if (constructorImage.isNotBlank()) {
                    constructorImage.loadImage(target = image_view_image_detail_image_view)
                } else if (coverImages.isNotEmpty()) {
                    coverImages[0].loadImage(target = image_view_image_detail_image_view)
                }

                if (clothesBrand.id != 0 && cost != 0) {
                    item_main_image_detail_user_tag_frame_layout.hide()
                } else {
                    item_main_image_detail_user_tag_frame_layout.show()
                }
            }

            image_view_image_detail_image_view.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(viewHolder = this@ClothesAdditionalHolder)
                }

                false
            }
        }
    }

    override fun onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY)
    }

    override fun onItemClear() {
        itemView.setBackgroundColor(0)
    }
}