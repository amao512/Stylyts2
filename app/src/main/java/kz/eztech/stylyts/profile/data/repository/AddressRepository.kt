package kz.eztech.stylyts.profile.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.profile.data.api.AddressApi
import kz.eztech.stylyts.common.data.exception.NetworkException
import kz.eztech.stylyts.profile.domain.models.AddressModel
import kz.eztech.stylyts.profile.domain.repository.AddressDomainRepository
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 14.03.2021.
 */
class AddressRepository @Inject constructor(
    private val api: AddressApi
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
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun getAllAddress(token: String): Single<List<AddressModel>> {
        return api.getAllAddress(token).map {
            when (it.isSuccessful) {
                true -> it.body()
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