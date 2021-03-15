package kz.eztech.stylyts.domain.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.AddressModel

interface AddressDomainRepository {

    fun postAddress(
        token: String,
        data: HashMap<String, Any>
    ): Single<AddressModel>

    fun getAllAddress(
        token: String
    ): Single<List<AddressModel>>

    fun deleteAddress(
        token: String,
        id: String
    )
}