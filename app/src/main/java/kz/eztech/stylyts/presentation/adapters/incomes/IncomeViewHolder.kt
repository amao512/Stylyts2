package kz.eztech.stylyts.presentation.adapters.incomes

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_income.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.getIncomeDateTime

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
        val income = ((item as IncomesItem) as? IncomeListItem)?.data

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
        income: ReferralModel,
        position: Int
    ) {
        dateTextView.text = income.createdAt.getIncomeDateTime()
        priceTextView.text = priceTextView.context.getString(
            R.string.price_tenge_text_format,
            income.referralCost.toString()
        )

        dateTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(view = it, position, income)
        }
    }
}