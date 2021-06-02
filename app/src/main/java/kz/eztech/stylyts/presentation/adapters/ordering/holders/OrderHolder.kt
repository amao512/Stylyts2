package kz.eztech.stylyts.presentation.adapters.ordering.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_order.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

class OrderHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var imageView: ImageView
    private lateinit var shopNameTextView: TextView
    private lateinit var clothesNameTextView: TextView
    private lateinit var clothesIdTextView: TextView
    private lateinit var countTextView: TextView
    private lateinit var sizeTextView: TextView
    private lateinit var priceTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as OrderModel

        initializeViews()
        processOrder(orderModel = item, position)
    }

    private fun initializeViews() {
        with (itemView) {
            imageView = item_order_image_view
            shopNameTextView = item_order_shop_name_text_view
            clothesNameTextView = item_order_clothes_name_text_view
            clothesIdTextView = item_order_id_text_view
            countTextView = item_order_count_text_view
            sizeTextView = item_order_size_text_view
            priceTextView = item_order_price_text_view
        }
    }

    private fun processOrder(
        orderModel: OrderModel,
        position: Int
    ) {
        shopNameTextView.text = orderModel.seller.username
        countTextView.text = "Кол-во ${orderModel.itemObjects.size}"
        sizeTextView.text = "Размер L"
        clothesIdTextView.text = "ID изделия: 23432423"
        priceTextView.text = priceTextView.context.getString(
            R.string.price_tenge_text_format,
            orderModel.price.toString()
        )

        itemView.item_order_detail_linear_layout.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, orderModel)
        }
    }
}