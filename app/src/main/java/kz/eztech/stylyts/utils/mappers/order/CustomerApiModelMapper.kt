package kz.eztech.stylyts.utils.mappers.order

import kz.eztech.stylyts.ordering.data.models.order.CustomerApiModel
import kz.eztech.stylyts.ordering.domain.models.order.CustomerModel

fun CustomerApiModel?.map(): CustomerModel {
    return CustomerModel(
        firstName = this?.firstName.orEmpty(),
        lastName = this?.lastName.orEmpty(),
        email = this?.email.orEmpty(),
        phoneNumber = this?.phoneNumber.orEmpty(),
    )
}