package kz.eztech.stylyts.presentation.utils.extensions.mappers.referrals

import kz.eztech.stylyts.data.api.models.referrals.ReferralApiModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.domain.models.user.UserShortModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getTimeByFormat
import kz.eztech.stylyts.presentation.utils.extensions.mappers.user.map

fun List<ReferralApiModel>?.map(): List<ReferralModel> {
    this ?: return emptyList()

    val testList: MutableList<ReferralModel> = mutableListOf()

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2020-12-12T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2020-12-16T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2020-12-21T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2020-12-30T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2021-06-28T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2021-07-18T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    this.map {
        val rm = ReferralModel(
            id = it.id ?: 0,
            buyer = it.buyer.map(),
            approved = it.approved,
            referralCost = it.referralCost ?: 0,
            createdAt = (it.createdAt ?: EMPTY_STRING).getTimeByFormat(),
            order = it.order ?: 0
        )
        testList.add(rm)
    }

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2021-07-30T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2021-08-18T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    testList.add(
        ReferralModel(
            id = 100,
            buyer = UserShortModel(
                id = 100,
                firstName = "ssdad",
                lastName = "asdsad",
                username = "adasd",
                avatar = "asdas",
                isBrand = false,
                isAlreadyFollow = false
            ),
            approved = false,
            referralCost = 1000,
            createdAt = ("2022-01-21T12:12:03.784536+06:00").getTimeByFormat(),
            order = 200
        )
    )

    return testList
}

fun ReferralApiModel?.map(): ReferralModel {
    return ReferralModel(
        id = this?.id ?: 0,
        buyer = this?.buyer.map(),
        approved = this?.approved ?: false,
        referralCost = this?.referralCost ?: 0,
        createdAt = (this?.createdAt ?: EMPTY_STRING).getTimeByFormat(),
        order = this?.order ?: 0
    )
}