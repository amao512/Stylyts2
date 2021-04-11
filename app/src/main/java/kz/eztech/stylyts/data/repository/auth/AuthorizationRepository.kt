package kz.eztech.stylyts.data.repository.auth

import android.util.Log
import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.AuthApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.domain.models.auth.ExistsUsernameModel
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
            username = data["username"] as String,
            email = data["email"] as String,
            password = data["password"] as String,
            firstName = data["first_name"] as String,
            lastName = data["last_name"] as String,
            gender = data["gender"] as String,
            dateOfBirth = data["date_of_birth"] as String,
            shouldSendMail = data["should_send_mail"] as Boolean,
            isBrand = data["is_brand"] as Boolean
        ).map {
            Log.d("TAG", it.toString())
            when (it.isSuccessful) {
                true -> it.body()
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
                true -> it.body()
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