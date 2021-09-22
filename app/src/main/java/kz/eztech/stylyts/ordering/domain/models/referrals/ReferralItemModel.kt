package kz.eztech.stylyts.ordering.domain.models.referrals

import kz.eztech.stylyts.global.domain.models.clothes.ClothesBrandModel

data class ReferralItemModel(
    val id: Int,
    val title: String,
    val description: String,
    val coverImages: List<String>,
    val clothesBrand: ClothesBrandModel,
    val cost: Int,
    val count: Int,
    val referralProfit: Int
) {
    val displayPrice
        get() = "$cost ₸"

    val displayReferralProfit
        get() = "$referralProfit ₸"
}