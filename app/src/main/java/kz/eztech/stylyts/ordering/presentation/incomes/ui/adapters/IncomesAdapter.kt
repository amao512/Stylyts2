package kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.INCOME_DATE_TYPE
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomesItem
import kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.viewHolders.IncomeViewHolder
import kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.viewHolders.IncomeDateViewHolder

class IncomesAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return currentList[oldItemPosition].hashCode() == list[newItemPosition].hashCode()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = (currentList[position] as IncomesItem).type

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            INCOME_DATE_TYPE -> IncomeDateViewHolder(
                itemView = inflateView(parent, R.layout.item_income_date),
                adapter = this
            )
            else -> IncomeViewHolder(
                itemView = inflateView(parent, R.layout.item_income),
                adapter = this
            )
        }
    }
}