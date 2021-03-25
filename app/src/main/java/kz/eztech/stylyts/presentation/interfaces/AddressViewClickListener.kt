package kz.eztech.stylyts.presentation.interfaces

import kz.eztech.stylyts.domain.models.address.AddressModel

interface AddressViewClickListener {

    fun setDefaultAddress(addressModel: AddressModel)

    fun onSwipeDelete(addressModel: AddressModel)
}