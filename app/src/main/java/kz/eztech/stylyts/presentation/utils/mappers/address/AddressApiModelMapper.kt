package kz.eztech.stylyts.presentation.utils.mappers.address

import kz.eztech.stylyts.data.api.models.address.AddressApiModel
import kz.eztech.stylyts.domain.models.address.AddressModel

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */

fun List<AddressApiModel>?.map(): List<AddressModel> {
    this ?: return emptyList()

    return this.map {
        AddressModel(
            id = it.id ?: 0,
            user = it.user.orEmpty(),
            country = it.country.orEmpty(),
            city = it.city.orEmpty(),
            street = it.street.orEmpty(),
            apartment = it.apartment.orEmpty(),
            entrance = it.entrance.orEmpty(),
            floor = it.floor.orEmpty(),
            doorPhone = it.doorPhone.orEmpty(),
            postalCode = it.postalCode.orEmpty(),
            comment = it.comment.orEmpty(),
            house = it.house.orEmpty()
        )
    }
}

fun AddressApiModel?.map(): AddressModel {
    return AddressModel(
        id = this?.id ?: 0,
        user = this?.user.orEmpty(),
        country = this?.country.orEmpty(),
        city = this?.city.orEmpty(),
        street = this?.street.orEmpty(),
        apartment = this?.apartment.orEmpty(),
        entrance = this?.entrance.orEmpty(),
        floor = this?.floor.orEmpty(),
        doorPhone = this?.doorPhone.orEmpty(),
        postalCode = this?.postalCode.orEmpty(),
        comment = this?.comment.orEmpty(),
        house = this?.house.orEmpty()
    )
}