package kz.eztech.stylyts.presentation.adapters.incomes

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.income.IncomeModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

class IncomesAdapter : BaseAdapter() {

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list) {
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as IncomeModel).id ==
                        (list[newItemPosition] as IncomeModel).id
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