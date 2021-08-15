package kz.eztech.stylyts.profile.domain.usecases.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.profile.domain.repositories.ProfileDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetUserByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<UserModel>(executorThread, uiThread) {

    private lateinit var userId: String

    override fun createSingleObservable(): Single<UserModel> {
        return profileDomainRepository.getUserProfileById(userId)
    }

    fun initParams(userId: Int) {
        this.userId = userId.toString()
    }
}