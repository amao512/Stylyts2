package kz.eztech.stylyts.domain.models.referrals

data class ReferralItemModel(
    val title: String,
    val description: String,
    val coverImages: List<String>,
    val cost: Int,
    val count: Int,
    val referralProfit: Int
)
