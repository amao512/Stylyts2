package kz.eztech.stylyts.presentation.utils.extensions.mappers.address

import kz.eztech.stylyts.data.api.models.address.AddressApiModel
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */

fun List<AddressApiModel>?.map(): List<AddressModel> {
    this ?: return emptyList()

    return this.map {
        AddressModel(
            id = it.id ?: 0,
            user = it.user ?: EMPTY_STRING,
            country = it.country ?: EMPTY_STRING,
            city = it.city ?: EMPTY_STRING,
            street = it.street ?: EMPTY_STRING,
            apartment = it.apartment ?: EMPTY_STRING,
            entrance = it.entrance ?: EMPTY_STRING,
            floor = it.floor ?: EMPTY_STRING,
            doorPhone = it.doorPhone ?: EMPTY_STRING,
            postalCode = it.postalCode ?: EMPTY_STRING,
            comment = it.comment ?: EMPTY_STRING,
            house = it.house ?: EMPTY_STRING
        )
    }
}

fun AddressApiModel?.map(): AddressModel {
    return AddressModel(
        id = this?.id ?: 0,
        user = this?.user ?: EMPTY_STRING,
        country = this?.country ?: EMPTY_STRING,
        city = this?.city ?: EMPTY_STRING,
        street = this?.street ?: EMPTY_STRING,
        apartment = this?.apartment ?: EMPTY_STRING,
        entrance = this?.entrance ?: EMPTY_STRING,
        floor = this?.floor ?: EMPTY_STRING,
        doorPhone = this?.doorPhone ?: EMPTY_STRING,
        postalCode = this?.postalCode ?: EMPTY_STRING,
        comment = this?.comment ?: EMPTY_STRING,
        house = this?.house ?: EMPTY_STRING
    )
}