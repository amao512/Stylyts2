package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.user.UserModel
import okhttp3.MultipartBody

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
interface ProfileDomainRepository {

    fun getUserProfile(): Single<UserModel>

    fun editUserProfile(data: HashMap<String, Any>): Single<UserModel>

    fun setProfilePhoto(avatar: MultipartBody.Part): Single<UserModel>

    fun getUserProfileById(userId: String): Single<UserModel>
}