package kz.eztech.stylyts.data.mappers.address

import kz.eztech.stylyts.data.api.models.address.AddressApiModel
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 11.04.2021.
 */
class AddressApiModelMapper @Inject constructor() {

    fun map(data: List<AddressApiModel>?): List<AddressModel> {
        data ?: return emptyList()

        return data.map {
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

    fun map(data: AddressApiModel?): AddressModel {
        return AddressModel(
            id = data?.id ?: 0,
            user = data?.user ?: EMPTY_STRING,
            country = data?.country ?: EMPTY_STRING,
            city = data?.city ?: EMPTY_STRING,
            street = data?.street ?: EMPTY_STRING,
            apartment = data?.apartment ?: EMPTY_STRING,
            entrance = data?.entrance ?: EMPTY_STRING,
            floor = data?.floor ?: EMPTY_STRING,
            doorPhone = data?.doorPhone ?: EMPTY_STRING,
            postalCode = data?.postalCode ?: EMPTY_STRING,
            comment = data?.comment ?: EMPTY_STRING,
            house = data?.house ?: EMPTY_STRING
        )
    }
}