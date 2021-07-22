package kz.eztech.stylyts.presentation.adapters.incomes

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.income.IncomeModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder

class IncomesAdapter : BaseAdapter() {

    override fun getLayoutId(viewType: Int): Int = R.layout.item_income

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

    override fun getViewHolder(view: View): BaseViewHolder {
        return IncomeViewHolder(itemView = view, adapter = this)
    }
}