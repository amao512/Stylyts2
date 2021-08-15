package kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.viewHolders

import android.view.View
import kotlinx.android.synthetic.main.item_income_date.view.*
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeDateItem
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomesItem
import kz.eztech.stylyts.utils.extensions.getMonthAndYear

class IncomeDateViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(item: Any, position: Int) {
        val incomeDate = ((item as IncomesItem) as? IncomeDateItem)?.data

        itemView.item_income_date_text_view.text = incomeDate?.getMonthAndYear()
    }
}