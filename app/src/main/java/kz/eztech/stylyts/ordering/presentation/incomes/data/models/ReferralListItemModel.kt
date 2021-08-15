package kz.eztech.stylyts.ordering.presentation.incomes.data.models

import kz.eztech.stylyts.ordering.domain.models.referrals.ReferralItemModel

const val REFERRAL_PROFIT = 0
const val REFERRAL_LIST_ITEM = 1

sealed class ReferralItem(
   val type: Int
)

data class ReferralProfitItem(
    val date: String,
    val totalProfit: String
) : ReferralItem(REFERRAL_PROFIT)

data class ReferralListItem(
    val data: ReferralItemModel
) : ReferralItem(REFERRAL_LIST_ITEM)