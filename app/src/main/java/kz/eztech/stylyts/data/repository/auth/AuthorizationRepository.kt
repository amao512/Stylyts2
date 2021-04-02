package kz.eztech.stylyts.data.repository.auth

import android.util.Log
import io.reactivex.Single
import kz.eztech.stylyts.data.api.AuthApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.domain.models.auth.TokenModel
import kz.eztech.stylyts.domain.repository.auth.AuthorizationDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class AuthorizationRepository @Inject constructor(
    private var api: AuthApi
) : AuthorizationDomainRepository {

    override fun registerUser(data: HashMap<String, Any>): Single<AuthModel> {
        return api.registerUser(
            data["email"] as String,
            data["password"] as String,
            data["name"] as String,
            data["last_name"] as String,
            data["date_of_birth"] as String,
            data["should_send_mail"] as Boolean,
            data["username"] as String,
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun loginUser(data: HashMap<String, Any>): Single<TokenModel> {
        return api.loginUser(
            username = data["username"] as String,
            password = data["password"] as String
        ).map {
            Log.d("TAG", "$it")
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