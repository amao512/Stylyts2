package kz.eztech.stylyts.presentation.utils.mappers.referrals

import kz.eztech.stylyts.data.api.models.referrals.ReferralItemApiModel
import kz.eztech.stylyts.domain.models.referrals.ReferralItemModel

fun List<ReferralItemApiModel>?.map(): List<ReferralItemModel> {
    this ?: return emptyList()

    return this.map {
        ReferralItemModel(
            title = it.title.orEmpty(),
            description = it.description.orEmpty(),
            coverImages = it.coverImages ?: emptyList(),
            cost = it.cost ?: 0,
            referralProfit = it.referralProfit ?: 0,
            count = it.count ?: 0
        )
    }
}

fun ReferralItemApiModel?.map(): ReferralItemModel {
    return ReferralItemModel(
        title = this?.title.orEmpty(),
        description = this?.description.orEmpty(),
        coverImages = this?.coverImages ?: emptyList(),
        cost = this?.cost ?: 0,
        referralProfit = this?.referralProfit ?: 0,
        count = this?.count ?: 0
    )
}