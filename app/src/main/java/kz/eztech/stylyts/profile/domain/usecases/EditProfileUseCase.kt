package kz.eztech.stylyts.profile.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.profile.domain.repository.ProfileDomainRepository
import kz.eztech.stylyts.common.domain.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class EditProfileUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<UserModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var data: HashMap<String, Any>

    override fun createSingleObservable(): Single<UserModel> {
        return profileDomainRepository.editUser(token, data)
    }

    fun initParams(
        token: String,
        data: HashMap<String, Any>
    ) {
        this.token = "JWT $token"
        this.data = data
    }
}