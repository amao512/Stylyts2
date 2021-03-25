package kz.eztech.stylyts.domain.repository.auth

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.auth.AuthModel

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface AuthorizationDomainRepository {

    fun registerUser(data: HashMap<String, Any>): Single<AuthModel>

    fun loginUser(data: HashMap<String, Any>): Single<AuthModel>

    fun generateForgotPassword(email: String): Single<Unit>

    fun setNewPassword(data: HashMap<String, Any>): Single<Unit>
}