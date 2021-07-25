package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.address.AddressModel

interface AddressDomainRepository {

    fun postAddress(data: HashMap<String, Any>): Single<AddressModel>

    fun getAllAddress(queryMap: Map<String, String>): Single<ResultsModel<AddressModel>>

    fun deleteAddress(id: String): Single<Any>
}