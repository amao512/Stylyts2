package kz.eztech.stylyts.domain.repository.auth

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.domain.models.auth.TokenModel

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface AuthorizationDomainRepository {

    fun registerUser(data: HashMap<String, Any>): Single<UserModel>

    fun loginUser(data: HashMap<String, Any>): Single<TokenModel>

    fun generateForgotPassword(email: String): Single<Unit>

    fun setNewPassword(data: HashMap<String, Any>): Single<Unit>
}