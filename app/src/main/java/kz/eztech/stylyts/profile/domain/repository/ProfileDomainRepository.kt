package kz.eztech.stylyts.profile.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.UserModel

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
interface ProfileDomainRepository {

    fun getUserProfile(token: String): Single<UserModel>

    fun editUser(
        token: String,
        data: HashMap<String, Any>
    ): Single<UserModel>

    fun getUserProfileById(
        token: String,
        userId: String
    ): Single<UserModel>
}