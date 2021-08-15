package kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.holders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_ordering_way.view.*
import kz.eztech.stylyts.ordering.domain.models.order.DeliveryWayModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder

class DeliveryWayHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var rootViewLinearLayout: LinearLayout
    private lateinit var iconImageView: ImageView
    private lateinit var titleTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as DeliveryWayModel

        initializeViews()
        processDeliveryWay(deliveryWay = item, position)
    }

    private fun initializeViews() {
        with (itemView) {
            rootViewLinearLayout = item_ordering_way_root_view
            iconImageView = item_ordering_way_icon_image_view
            titleTextView = item_ordering_way_title_text_view
        }
    }

    private fun processDeliveryWay(
        deliveryWay: DeliveryWayModel,
        position: Int
    ) {
        iconImageView.setImageDrawable(
            ContextCompat.getDrawable(iconImageView.context, deliveryWay.icon)
        )
        titleTextView.text = deliveryWay.title

        rootViewLinearLayout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, deliveryWay)
        }
    }
}