package kz.eztech.stylyts.presentation.utils.extensions.mappers.referrals

import kz.eztech.stylyts.data.api.models.referrals.ReferralApiModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.mappers.user.map

fun List<ReferralApiModel>?.map(): List<ReferralModel> {
    this ?: return emptyList()

    return this.map {
        ReferralModel(
            id = it.id ?: 0,
            buyer = it.buyer.map(),
            approved = it.approved,
            referralCost = it.referralCost ?: 0,
            createdAt = it.createdAt ?: EMPTY_STRING,
            order = it.order ?: 0
        )
    }
}

fun ReferralApiModel?.map(): ReferralModel {
    return ReferralModel(
        id = this?.id ?: 0,
        buyer = this?.buyer.map(),
        approved = this?.approved ?: false,
        referralCost = this?.referralCost ?: 0,
        createdAt = this?.createdAt ?: EMPTY_STRING,
        order = this?.order ?: 0
    )
}