package kz.eztech.stylyts.domain.repository.auth

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.domain.models.auth.ExistsUsernameModel

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface AuthorizationDomainRepository {

    fun registerUser(data: HashMap<String, Any>): Single<AuthModel>

    fun loginUser(data: HashMap<String, Any>): Single<AuthModel>

    fun isUsernameExists(username: String): Single<ExistsUsernameModel>

    fun generateForgotPassword(email: String): Single<Unit>

    fun setNewPassword(data: HashMap<String, Any>): Single<Unit>
}