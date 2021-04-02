package kz.eztech.stylyts.data.repository.profile

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.data.api.ProfileApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.profile.ProfileDomainRepository
import kz.eztech.stylyts.domain.models.search.SearchModel
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfileRepository @Inject constructor(
    private var api: ProfileApi
) : ProfileDomainRepository {

    override fun getUserProfile(token: String): Single<UserModel> {
        return api.getMyProfile(token).map {
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

    override fun getMyPublications(token: String): Single<SearchModel<PublicationModel>> {
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