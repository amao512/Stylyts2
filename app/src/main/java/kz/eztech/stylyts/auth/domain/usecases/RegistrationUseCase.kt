package kz.eztech.stylyts.auth.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.auth.domain.models.AuthModel
import kz.eztech.stylyts.auth.domain.repositories.AuthorizationDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class RegistrationUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var authorizationDomainRepository: AuthorizationDomainRepository
) : BaseUseCase<AuthModel>(executorThread, uiThread) {

    private lateinit var fieldStringMap: Map<String, String>
    private lateinit var fieldBooleanMap: Map<String, Boolean>

    override fun createSingleObservable(): Single<AuthModel> {
        return authorizationDomainRepository.registerUser(fieldStringMap, fieldBooleanMap)
    }

    fun initParams(data: HashMap<String, Any>) {
        val fieldStringMap = HashMap<String, String>()
        val fieldBooleanMap = HashMap<String, Boolean>()

        fieldStringMap["username"] = data["username"] as String
        fieldStringMap["email"] = data["email"] as String
        fieldStringMap["password"] = data["password"] as String
        fieldStringMap["first_name"] = data["first_name"] as String
        fieldStringMap["last_name"] = data["last_name"] as String
        fieldStringMap["gender"] = data["gender"] as String
        fieldStringMap["date_of_birth"] = data["date_of_birth"] as String
        fieldBooleanMap["should_send_mail"] = data["should_send_mail"] as Boolean
        fieldBooleanMap["is_brand"] = data["is_brand"] as Boolean

        this.fieldStringMap = fieldStringMap
        this.fieldBooleanMap = fieldBooleanMap
    }
}