package kz.eztech.stylyts.auth.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.auth.domain.models.AuthModel
import kz.eztech.stylyts.auth.data.models.ExistsUsernameModel

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface AuthorizationDomainRepository {

    fun registerUser(
        fieldStringMap: Map<String, String>,
        fieldBooleanMap: Map<String, Boolean>
    ): Single<AuthModel>

    fun loginUser(data: HashMap<String, Any>): Single<AuthModel>

    fun isUsernameExists(username: String): Single<ExistsUsernameModel>

    fun generateForgotPassword(email: String): Single<Unit>

    fun setNewPassword(data: HashMap<String, Any>): Single<Unit>
}