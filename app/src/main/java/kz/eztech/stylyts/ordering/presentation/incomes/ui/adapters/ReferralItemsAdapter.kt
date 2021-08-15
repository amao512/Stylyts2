package kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters

import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.REFERRAL_PROFIT
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.ReferralItem
import kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.viewHolders.ReferralIncomeViewHolder
import kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.viewHolders.ReferralProfitViewHolder

class ReferralItemsAdapter : BaseAdapter() {

    override fun getItemViewType(position: Int): Int {
        return (currentList[position] as ReferralItem).type
    }

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
        return when (viewType) {
            REFERRAL_PROFIT -> ReferralProfitViewHolder(
                itemView = inflateView(parent, R.layout.item_referral_profit),
                adapter = this
            )
            else -> ReferralIncomeViewHolder(
                itemView = inflateView(parent, R.layout.item_shop_order_clothes),
                adapter = this
            )
        }
    }
}