package kz.eztech.stylyts.presentation.adapters.ordering.holders

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_shop_order.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.enums.ordering.DeliveryStatusEnum
import kz.eztech.stylyts.presentation.enums.ordering.PaymentStatusEnum
import kz.eztech.stylyts.presentation.utils.DateFormatterHelper
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class ShopOrderViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var numberTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var newOrderTextView: TextView
    private lateinit var deliveredOrderTextView: TextView
    private lateinit var completeOrderTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as OrderModel

        initializeViews()
        processOrder(item, position)
    }

    private fun initializeViews() {
        with(itemView) {
            numberTextView = item_shop_order_number_text_view
            priceTextView = item_shop_price_text_view
            dateTextView = item_shop_order_date_text_view
            newOrderTextView = item_shop_order_new_order_text_view
            deliveredOrderTextView = item_shop_order_delivered_text_view
            completeOrderTextView = item_shop_order_complete_order_text_view
        }
    }

    private fun processOrder(
        order: OrderModel,
        position: Int
    ) {
        numberTextView.text =
            itemView.context.getString(R.string.order_number_text_format, "${order.id} - ")
        priceTextView.text =
            itemView.context.getString(R.string.price_tenge_text_format, order.price.toString())
        dateTextView.text = DateFormatterHelper.formatISO_8601(
            order.createdAt,
            DateFormatterHelper.FORMAT_DATE_dd_MM_yyyy_dash
        )

        if (
            order.delivery.deliveryStatus == DeliveryStatusEnum.NEW.status &&
            order.invoice.paymentStatus == PaymentStatusEnum.NEW.status
        ) {
            newOrderTextView.show()
            deliveredOrderTextView.hide()
            completeOrderTextView.hide()
        } else if (order.delivery.deliveryStatus == DeliveryStatusEnum.IN_PROGRESS.status) {
            newOrderTextView.hide()
            deliveredOrderTextView.hide()
            completeOrderTextView.show()
        } else if (order.delivery.deliveryStatus == DeliveryStatusEnum.DELIVERED.status) {
            newOrderTextView.hide()
            deliveredOrderTextView.show()
            completeOrderTextView.hide()
        }

        itemView.item_shop_order_root_view.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, order)
        }
    }
}