package kz.eztech.stylyts.data.mappers.order

import kz.eztech.stylyts.data.api.models.order.CustomerApiModel
import kz.eztech.stylyts.domain.models.order.CustomerModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class CustomerApiModelMapper @Inject constructor() {

    fun map(data: CustomerApiModel?): CustomerModel {
        return CustomerModel(
            firstName = data?.firstName ?: EMPTY_STRING,
            lastName = data?.lastName ?: EMPTY_STRING,
            email = data?.email ?: EMPTY_STRING,
            phoneNumber = data?.phoneNumber ?: EMPTY_STRING,
        )
    }
}