package kz.eztech.stylyts.profile.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.profile.domain.models.AddressModel

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
    ): Single<Any>
}