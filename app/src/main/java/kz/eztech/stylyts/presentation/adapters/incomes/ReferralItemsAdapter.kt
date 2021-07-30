package kz.eztech.stylyts.presentation.adapters.incomes

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.adapters.incomes.viewHolders.ReferralIncomeViewHolder

class ReferralItemsAdapter : BaseAdapter() {

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

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ReferralIncomeViewHolder(
            itemView = inflateView(parent, R.layout.item_shop_order_clothes),
            adapter = this
        )
    }
}