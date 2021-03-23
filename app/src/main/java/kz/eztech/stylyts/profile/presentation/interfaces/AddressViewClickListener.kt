package kz.eztech.stylyts.profile.presentation.interfaces

import kz.eztech.stylyts.profile.domain.models.AddressModel

interface AddressViewClickListener {

    fun setDefaultAddress(addressModel: AddressModel)

    fun onSwipeDelete(addressModel: AddressModel)
}