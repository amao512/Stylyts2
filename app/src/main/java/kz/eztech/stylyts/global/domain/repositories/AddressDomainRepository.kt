package kz.eztech.stylyts.global.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.address.AddressModel

interface AddressDomainRepository {

    fun postAddress(data: HashMap<String, Any>): Single<AddressModel>

    fun getAllAddress(queryMap: Map<String, String>): Single<ResultsModel<AddressModel>>

    fun deleteAddress(id: String): Single<Any>
}