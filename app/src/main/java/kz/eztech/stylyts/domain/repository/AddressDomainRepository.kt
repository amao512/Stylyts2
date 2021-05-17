package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.address.AddressModel

interface AddressDomainRepository {

    fun postAddress(
        token: String,
        data: HashMap<String, Any>
    ): Single<AddressModel>

    fun getAllAddress(
        token: String
    ): Single<ResultsModel<AddressModel>>

    fun deleteAddress(
        token: String,
        id: String
    ): Single<Any>
}