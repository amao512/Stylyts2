package kz.eztech.stylyts.data.repository.address

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.AddressApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.data.mappers.address.AddressApiModelMapper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.repository.address.AddressDomainRepository
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
class AddressRepository @Inject constructor(
    private val api: AddressApi,
    private val addressApiModelMapper: AddressApiModelMapper,
    private val resultsApiModelMapper: ResultsApiModelMapper
) : AddressDomainRepository {

    override fun postAddress(
        token: String,
        data: HashMap<String, Any>
    ): Single<AddressModel> {
        return api.postAddress(
            token = token,
            country = data["country"] as String,
            city = data["city"] as String,
            street = data["street"] as String,
            postalCode = data["postal_code"] as String
        ).map {
            when (it.isSuccessful) {
                true -> addressApiModelMapper.map(data = it.body())
                else -> throw NetworkException(it)
            }
        }
    }

    override fun getAllAddress(token: String): Single<ResultsModel<AddressModel>> {
        return api.getAllAddress(token).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.mapAddressResults(data = it.body())
                else -> throw NetworkException(it)
            }
        }
    }

    override fun deleteAddress(
        token: String,
        id: String
    ): Single<Any> {
        return api.deleteAddress(
            token = token,
            id = id
        )
    }
}