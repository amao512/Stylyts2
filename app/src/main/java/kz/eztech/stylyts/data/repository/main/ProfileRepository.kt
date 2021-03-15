package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.ProfileApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.domain.repository.main.ProfileDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfileRepository @Inject constructor(
    private var api: ProfileApi
) : ProfileDomainRepository {

    override fun getProfile(token: String): Single<ProfileModel> {
        return api.getUserProfile(token).map {
            when (it.isSuccessful) {
                true -> it.body()?.get(0)
                else -> throw NetworkException(it)
            }
        }
    }

    override fun editProfile(
        token: String,
        data: HashMap<String, Any>
    ): Single<ProfileModel> {
        return api.editUserProfile(
            token = token,
            name = data["name"] as String
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}