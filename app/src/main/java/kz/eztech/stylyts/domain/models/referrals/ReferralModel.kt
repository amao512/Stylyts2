package kz.eztech.stylyts.domain.models.referrals

import kz.eztech.stylyts.domain.models.user.UserShortModel
import org.threeten.bp.ZonedDateTime

data class ReferralModel(
    val id: Int,
    val buyer: UserShortModel,
    val items: List<ReferralItemModel>,
    val approved: Boolean,
    val totalProfit: Int,
    val referralPercentage: Int,
    val createdAt: ZonedDateTime,
    val order: Int
)