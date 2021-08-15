package kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.viewHolders

import android.view.View
import kotlinx.android.synthetic.main.item_referral_profit.view.*
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.ReferralProfitItem

class ReferralProfitViewHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(item: Any, position: Int) {
        val referralProfit = (item as ReferralProfitItem)

        with(itemView) {
            item_referral_date_text_view.text = referralProfit.date
            item_referral_cost_text_view.text = referralProfit.totalProfit
        }
    }
}