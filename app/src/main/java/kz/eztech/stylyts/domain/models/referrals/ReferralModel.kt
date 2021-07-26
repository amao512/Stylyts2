package kz.eztech.stylyts.domain.models.referrals

import kz.eztech.stylyts.domain.models.user.UserShortModel

data class ReferralModel(
    val id: Int,
    val buyer: UserShortModel,
    val approved: Boolean,
    val referralCost: Int,
    val createdAt: String,
    val order: Int
)
