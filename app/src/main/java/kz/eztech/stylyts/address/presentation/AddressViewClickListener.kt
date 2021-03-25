package kz.eztech.stylyts.address.presentation

import kz.eztech.stylyts.address.domain.models.AddressModel

interface AddressViewClickListener {

    fun setDefaultAddress(addressModel: AddressModel)

    fun onSwipeDelete(addressModel: AddressModel)
}