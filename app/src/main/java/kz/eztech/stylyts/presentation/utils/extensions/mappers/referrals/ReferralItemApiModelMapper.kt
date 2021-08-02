package kz.eztech.stylyts.presentation.utils.extensions.mappers.referrals

import kz.eztech.stylyts.data.api.models.referrals.ReferralItemApiModel
import kz.eztech.stylyts.domain.models.referrals.ReferralItemModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<ReferralItemApiModel>?.map(): List<ReferralItemModel> {
    this ?: return emptyList()

    return this.map {
        ReferralItemModel(
            title = it.title ?: EMPTY_STRING,
            description = it.description ?: EMPTY_STRING,
            coverImages = it.coverImages ?: emptyList(),
            cost = it.cost ?: 0,
            referralProfit = it.referralProfit ?: 0,
            count = it.count ?: 0
        )
    }
}

fun ReferralItemApiModel?.map(): ReferralItemModel {
    return ReferralItemModel(
        title = this?.title ?: EMPTY_STRING,
        description = this?.description ?: EMPTY_STRING,
        coverImages = this?.coverImages ?: emptyList(),
        cost = this?.cost ?: 0,
        referralProfit = this?.referralProfit ?: 0,
        count = this?.count ?: 0
    )
}