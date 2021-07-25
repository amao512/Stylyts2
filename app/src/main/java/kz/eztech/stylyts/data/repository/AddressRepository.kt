package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.AddressApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.presentation.utils.extensions.mappers.address.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.map
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.repository.AddressDomainRepository
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
class AddressRepository @Inject constructor(
    private val api: AddressApi
) : AddressDomainRepository {

    override fun postAddress(data: HashMap<String, Any>): Single<AddressModel> {
        return api.postAddress(
            country = data["country"] as String,
            city = data["city"] as String,
            street = data["street"] as String,
            postalCode = data["postal_code"] as String,
            house = data["house"] as String
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun getAllAddress(queryMap: Map<String, String>): Single<ResultsModel<AddressModel>> {
        return api.getAllAddress(queryMap).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun deleteAddress(id: String): Single<Any> {
        return api.deleteAddress(id = id)
    }
}