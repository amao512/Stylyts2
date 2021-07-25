package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.ProfileApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.presentation.utils.extensions.mappers.user.map
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.repository.ProfileDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfileRepository @Inject constructor(
    private var api: ProfileApi
) : ProfileDomainRepository {

    override fun getUserProfile(): Single<UserModel> {
        return api.getMyProfile().map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun editUserProfile(data: HashMap<String, Any>): Single<UserModel> {
        return api.editUserProfile(
            firstName = data["first_name"] as String,
            lastName = data["last_name"] as String,
            username = data["username"] as String
//            instagram = data["instagram"] as String,
//            webSite = data["web_site"] as String
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }

    override fun setProfilePhoto(avatar: MultipartBody.Part): Single<UserModel> {
        return api.setProfilePhoto(avatar).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getUserProfileById(userId: String): Single<UserModel> {
        return api.getUserProfileById(userId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                else -> throw NetworkException(it)
            }
        }
    }
}