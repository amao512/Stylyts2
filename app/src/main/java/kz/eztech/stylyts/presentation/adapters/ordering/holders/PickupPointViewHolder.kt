package kz.eztech.stylyts.presentation.adapters.ordering.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_pickup_point.view.*
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

class PickupPointViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var fittingAvailableTextView: TextView
    private lateinit var timeTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as AddressModel

        initializeViews()
        processAddress(item, position)
    }

    private fun initializeViews() {
        with (itemView) {
            imageView = item_pickup_point_image_view
            titleTextView = item_pickup_point_address_title_text_view
            addressTextView = item_pickup_address_text_view
            fittingAvailableTextView = item_pickup_fitting_available_text_view
            timeTextView = item_pickup_time_text_view
        }
    }

    private fun processAddress(
        address: AddressModel,
        position: Int
    ) {
        titleTextView.text = address.user
        (address.city + ", " + address.street).also { addressTextView.text = it }

        itemView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, address)
        }
    }
}