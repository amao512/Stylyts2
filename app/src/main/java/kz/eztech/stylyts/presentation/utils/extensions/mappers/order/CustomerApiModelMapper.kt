package kz.eztech.stylyts.presentation.utils.extensions.mappers.order

import kz.eztech.stylyts.data.api.models.order.CustomerApiModel
import kz.eztech.stylyts.domain.models.order.CustomerModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun CustomerApiModel?.map(): CustomerModel {
    return CustomerModel(
        firstName = this?.firstName ?: EMPTY_STRING,
        lastName = this?.lastName ?: EMPTY_STRING,
        email = this?.email ?: EMPTY_STRING,
        phoneNumber = this?.phoneNumber ?: EMPTY_STRING,
    )
}