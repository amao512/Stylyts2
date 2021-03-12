package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.AuthModel

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface AuthorizationDomainRepository {

    fun registerUser(data: HashMap<String, Any>): Single<AuthModel>

    fun loginUser(data: HashMap<String, Any>): Single<AuthModel>

    fun generateForgotPassword(email: String): Single<Unit>

    fun setNewPassword(data: HashMap<String, Any>): Single<Unit>
}