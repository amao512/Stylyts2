package kz.eztech.stylyts.presentation.adapters.collection_constructor.holders

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import androidx.core.view.MotionEventCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_main_image_detail.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.ItemTouchHelperViewHolder
import kz.eztech.stylyts.presentation.interfaces.OnStartDragListener

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImageAdditionalHolder(
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
                adapter.itemClickListener?.onViewClicked(
                    frame_layout_item_main_image_holder_container,
                    position,
                    item
                )
            }

            Glide.with(image_view_image_detail_image_view.context)
                .load(item.constructorImage)
                .into(image_view_image_detail_image_view)

            image_view_image_detail_image_view.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(viewHolder = this@MainImageAdditionalHolder)
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