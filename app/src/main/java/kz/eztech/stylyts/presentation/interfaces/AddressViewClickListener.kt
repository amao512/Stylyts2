package kz.eztech.stylyts.presentation.interfaces

import kz.eztech.stylyts.domain.models.AddressModel

interface AddressViewClickListener {

    fun setDefaultAddress(addressModel: AddressModel)
}