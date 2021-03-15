package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.AddressApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.AddressModel
import kz.eztech.stylyts.domain.repository.main.AddressDomainRepository
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
    ) {
        api.deleteAddress(
            token = token,
            id = id
        )
    }
}