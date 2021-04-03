package kz.eztech.stylyts.data.repository.profile

import android.util.Log
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.data.api.ProfileApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.profile.ProfileDomainRepository
import kz.eztech.stylyts.domain.models.ResultsModel
import okhttp3.MultipartBody
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

    override fun editUserProfile(
        token: String,
        data: HashMap<String, Any?>
    ): Single<UserModel> {
        return api.editUserProfile(
            token = token,
            firstName = data["first_name"] as String,
            lastName = data["last_name"] as String,
            avatar = data["avatar"] as MultipartBody.Part?,
            instagram = data["instagram"] as String?,
            webSite = data["web_site"] as String?
        ).map {
            Log.d("TAG", "$it")
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

    override fun getMyPublications(token: String): Single<ResultsModel<PublicationModel>> {
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