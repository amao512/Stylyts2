package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.ProfileApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.user.UserApiModelMapper
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.repository.ProfileDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfileRepository @Inject constructor(
    private var api: ProfileApi,
    private val userApiModelMapper: UserApiModelMapper
) : ProfileDomainRepository {

    override fun getUserProfile(token: String): Single<UserModel> {
        return api.getMyProfile(token).map {
            when (it.isSuccessful) {
                true -> userApiModelMapper.map(data = it.body())
                else -> throw NetworkException(it)
            }
        }
    }

    override fun editUserProfile(
        token: String,
        data: HashMap<String, Any>
    ): Single<UserModel> {
        return api.editUserProfile(
            token = token,
            firstName = data["first_name"] as String,
            lastName = data["last_name"] as String,
            instagram = data["instagram"] as String,
            webSite = data["web_site"] as String
        ).map {
            when (it.isSuccessful) {
                true -> userApiModelMapper.map(data = it.body())
                else -> throw NetworkException(it)
            }
        }
    }

    override fun setProfilePhoto(
        token: String,
        avatar: MultipartBody.Part
    ): Single<UserModel> {
        return api.setProfilePhoto(
            token = token,
            avatar = avatar
        ).map {
            when (it.isSuccessful) {
                true -> userApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
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
                true -> userApiModelMapper.map(data = it.body())
                else -> throw NetworkException(it)
            }
        }
    }
}