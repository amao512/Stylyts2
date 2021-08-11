package kz.eztech.stylyts.utils.mappers.referrals

import kz.eztech.stylyts.data.api.models.referrals.ReferralItemApiModel
import kz.eztech.stylyts.domain.models.referrals.ReferralItemModel
import kz.eztech.stylyts.utils.mappers.clothes.map

fun List<ReferralItemApiModel>?.map(): List<ReferralItemModel> {
    this ?: return emptyList()

    return this.map {
        ReferralItemModel(
            id = it.id ?: 0,
            title = it.title.orEmpty(),
            description = it.description.orEmpty(),
            coverImages = it.coverImages ?: emptyList(),
            clothesBrand = it.clothesBrand.map(),
            cost = it.cost ?: 0,
            referralProfit = it.referralProfit ?: 0,
            count = it.count ?: 0
        )
    }
}

fun ReferralItemApiModel?.map(): ReferralItemModel {
    return ReferralItemModel(
        id = this?.id ?: 0,
        title = this?.title.orEmpty(),
        description = this?.description.orEmpty(),
        coverImages = this?.coverImages ?: emptyList(),
        clothesBrand = this?.clothesBrand.map(),
        cost = this?.cost ?: 0,
        referralProfit = this?.referralProfit ?: 0,
        count = this?.count ?: 0
    )
}