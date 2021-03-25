package kz.eztech.stylyts.profile.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationsModel
import kz.eztech.stylyts.profile.data.api.ProfileApi
import kz.eztech.stylyts.common.data.exception.NetworkException
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.profile.domain.repository.ProfileDomainRepository
import kz.eztech.stylyts.search.domain.models.SearchModel
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfileRepository @Inject constructor(
    private var api: ProfileApi
) : ProfileDomainRepository {

    override fun getUserProfile(token: String): Single<UserModel> {
        return api.getUserProfile(token).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun editUser(
        token: String,
        data: HashMap<String, Any>
    ): Single<UserModel> {
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

    override fun getUserProfileById(
        token: String,
        userId: String
    ): Single<UserModel> {
        return api.getUserProfileById(
            token = token,
            userId = userId
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun getMyPublications(token: String): Single<SearchModel<PublicationsModel>> {
        return api.getMyCollections(
            token = token
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}