package kz.eztech.stylyts.profile.presentation.address.interfaces

import kz.eztech.stylyts.global.domain.models.address.AddressModel

interface AddressViewClickListener {

    fun setDefaultAddress(addressModel: AddressModel)

    fun onSwipeDelete(addressModel: AddressModel)
}