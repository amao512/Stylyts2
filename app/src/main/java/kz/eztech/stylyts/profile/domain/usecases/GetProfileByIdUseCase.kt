package kz.eztech.stylyts.profile.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.profile.domain.repository.ProfileDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetProfileByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<UserModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var userId: String

    override fun createSingleObservable(): Single<UserModel> {
        return profileDomainRepository.getUserProfileById(token, userId)
    }

    fun initParams(
        token: String,
        userId: String
    ) {
        this.token = "JWT $token"
        this.userId = userId
    }
}