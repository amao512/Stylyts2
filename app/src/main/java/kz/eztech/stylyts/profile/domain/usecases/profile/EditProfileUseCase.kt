package kz.eztech.stylyts.profile.domain.usecases.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.profile.domain.repositories.ProfileDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class EditProfileUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<UserModel>(executorThread, uiThread) {

    private lateinit var data: HashMap<String, Any>

    override fun createSingleObservable(): Single<UserModel> {
        return profileDomainRepository.editUserProfile(data)
    }

    fun initParams(data: HashMap<String, Any>) {
        this.data = data
    }
}