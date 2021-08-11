package kz.eztech.stylyts.utils.mappers.referrals

import kz.eztech.stylyts.data.api.models.referrals.ReferralApiModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.extensions.getZonedDateTime
import kz.eztech.stylyts.utils.extensions.orFalse
import kz.eztech.stylyts.utils.mappers.user.map

fun List<ReferralApiModel>?.map(): List<ReferralModel> {
    this ?: return emptyList()

    return this.map {
        ReferralModel(
            id = it.id ?: 0,
            buyer = it.buyer.map(),
            items = it.items.map(),
            approved = it.approved,
            totalProfit = it.totalProfit ?: 0,
            referralPercentage = it.referralPercentage ?: 0,
            createdAt = (it.createdAt ?: EMPTY_STRING).getZonedDateTime(),
            order = it.order ?: 0
        )
    }
}

fun ReferralApiModel?.map(): ReferralModel {
    return ReferralModel(
        id = this?.id ?: 0,
        buyer = this?.buyer.map(),
        items = this?.items.map(),
        approved = this?.approved.orFalse(),
        totalProfit = this?.totalProfit ?: 0,
        referralPercentage = this?.referralPercentage ?: 0,
        createdAt = (this?.createdAt ?: EMPTY_STRING).getZonedDateTime(),
        order = this?.order ?: 0
    )
}