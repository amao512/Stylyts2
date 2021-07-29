package kz.eztech.stylyts.presentation.adapters.incomes

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_income.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.getIncomeDateTime
import kz.eztech.stylyts.presentation.utils.extensions.getTimeByFormat
import org.threeten.bp.Month

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
    ) {
        dateTextView.text = getDateString(income.getReferralList())
        priceTextView.text = priceTextView.context.getString(
            R.string.price_tenge_text_format,
            income.getReferralList().sumBy { it.referralCost }.toString()
        )

        dateTextView.setOnClickListener {
            adapter.itemClickListener?.onViewClicked(view = it, position, income)
        }
    }

    private fun getDateString(referralList: List<ReferralModel>): String {
        val startDate = referralList.first().createdAt
        val lastDate = referralList.last().createdAt

        return if (startDate.dayOfMonth != lastDate.dayOfMonth) {
            startDate.getIncomeDateTime() + " - " + lastDate.getIncomeDateTime()
        } else {
            if ((lastDate.dayOfMonth + 7) > 31) {
                val day = lastDate.month.length(false)
                val newDate = getDate(lastDate.year, lastDate.month, day.toString())

                startDate.getIncomeDateTime() + " - " + newDate.getTimeByFormat().getIncomeDateTime()
            } else {
                val newDate = getDate(lastDate.year, lastDate.month, getDay(lastDate.dayOfMonth))

                startDate.getIncomeDateTime() + " - " + newDate.getTimeByFormat().getIncomeDateTime()
            }
        }
    }

    private fun getDate(
        year: Int,
        month: Month,
        day: String
    ): String = "$year-${getMonth(month)}-${day}T12:12:03.784536+06:00"

    private fun getMonth(month: Month): String {
        return if (month.value < 10) {
            "0${month.value}"
        } else {
            month.value.toString()
        }
    }

    private fun getDay(day: Int): String {
        return if (day < 10) {
            "0${day + 7}"
        } else {
            (day + 7).toString()
        }
    }
}