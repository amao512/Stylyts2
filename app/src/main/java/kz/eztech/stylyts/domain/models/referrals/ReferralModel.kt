package kz.eztech.stylyts.domain.models.referrals

import kz.eztech.stylyts.domain.models.user.UserShortModel
import org.threeten.bp.ZonedDateTime

data class ReferralModel(
    val id: Int,
    val buyer: UserShortModel,
    val approved: Boolean,
    val referralCost: Int,
    val createdAt: ZonedDateTime,
    val order: Int
)