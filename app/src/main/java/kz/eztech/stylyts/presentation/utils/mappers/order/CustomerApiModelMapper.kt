package kz.eztech.stylyts.presentation.utils.mappers.order

import kz.eztech.stylyts.data.api.models.order.CustomerApiModel
import kz.eztech.stylyts.domain.models.order.CustomerModel

fun CustomerApiModel?.map(): CustomerModel {
    return CustomerModel(
        firstName = this?.firstName.orEmpty(),
        lastName = this?.lastName.orEmpty(),
        email = this?.email.orEmpty(),
        phoneNumber = this?.phoneNumber.orEmpty(),
    )
}