package kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.holders

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_delivery_condition.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.ordering.domain.models.order.DeliveryConditionModel
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder

class DeliveryConditionViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var titleTextView: TextView
    private lateinit var fittingTimeTextView: TextView
    private lateinit var deliverDateTextView: TextView
    private lateinit var choiceTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        item as DeliveryConditionModel

        initializeViews()
        processCondition(deliveryConditionModel = item, position)
    }

    private fun initializeViews() {
        with (itemView) {
            titleTextView = item_delivery_condition_title_text_view
            fittingTimeTextView = item_delivery_condition_fitting_time_text_view
            deliverDateTextView = item_delivery_condition_fitting_date_text_view
            choiceTextView = item_delivery_condition_choice_text_view
        }
    }

    private fun processCondition(
        deliveryConditionModel: DeliveryConditionModel,
        position: Int
    ) {
        titleTextView.text = deliveryConditionModel.title
        fittingTimeTextView.text = fittingTimeTextView.context.getString(
            R.string.delivery_fitting_info,
            deliveryConditionModel.fittingTime
        )
        deliverDateTextView.text = deliverDateTextView.context.getString(
            R.string.delivery_date_info,
            deliveryConditionModel.deliveryDate
        )

        choiceTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(it, position, deliveryConditionModel)
        }
    }
}