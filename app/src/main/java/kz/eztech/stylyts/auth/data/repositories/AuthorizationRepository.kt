package kz.eztech.stylyts.auth.data.repositories

import io.reactivex.Single
import kz.eztech.stylyts.auth.data.api.AuthApi
import kz.eztech.stylyts.global.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.auth.map
import kz.eztech.stylyts.auth.domain.models.AuthModel
import kz.eztech.stylyts.auth.data.models.ExistsUsernameModel
import kz.eztech.stylyts.auth.domain.repositories.AuthorizationDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class AuthorizationRepository @Inject constructor(
    private val api: AuthApi
) : AuthorizationDomainRepository {

    override fun registerUser(
        fieldStringMap: Map<String, String>,
        fieldBooleanMap: Map<String, Boolean>
    ): Single<AuthModel> {
        return api.registerUser(
            fieldStringMap = fieldStringMap,
            fieldBooleanMap = fieldBooleanMap
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun loginUser(data: HashMap<String, Any>): Single<AuthModel> {
        return api.loginUser(
            username = data["username"] as String,
            password = data["password"] as String
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun isUsernameExists(username: String): Single<ExistsUsernameModel> {
        return api.isUsernameExists(username).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun generateForgotPassword(email: String): Single<Unit> {
        return api.generateForgotPassword(
            email
        ).map {
            when (it.isSuccessful) {
                true -> Unit
                false -> throw NetworkException(it)
            }
        }
    }

    override fun setNewPassword(data: HashMap<String, Any>): Single<Unit> {
        return api.setNewPassword(
            data["token"] as String,
            data["password"] as String
        ).map {
            when (it.isSuccessful) {
                true -> Unit
                false -> throw NetworkException(it)
            }
        }
    }
}