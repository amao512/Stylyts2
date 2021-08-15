package kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.viewHolders

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_income.view.*
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeListItem
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomesItem
import kz.eztech.stylyts.utils.getIncomeDateString

class IncomeViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    private lateinit var dateTextView: TextView
    private lateinit var priceTextView: TextView

    override fun bindData(
        item: Any,
        position: Int
    ) {
        val income = (item as IncomesItem) as? IncomeListItem

        initViews()

        income?.let {
            processIncome(income = it, position)
        }
    }

    private fun initViews() {
        with(itemView) {
            dateTextView = item_income_date_text_view
            priceTextView = item_income_price_text_view
        }
    }

    private fun processIncome(
        income: IncomeListItem,
        position: Int
    ) = with(income) {
        dateTextView.text = getReferralList().getIncomeDateString()
        priceTextView.text = displayTotalProfit

        dateTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(view = it, position, income)
        }
    }
}