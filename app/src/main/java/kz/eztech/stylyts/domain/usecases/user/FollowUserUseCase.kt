package kz.eztech.stylyts.domain.usecases.user

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.repository.UserDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class FollowUserUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val userDomainRepository: UserDomainRepository
) : BaseUseCase<FollowSuccessModel>(executorThread, uiThread) {

    private lateinit var userId: String

    override fun createSingleObservable(): Single<FollowSuccessModel> {
        return userDomainRepository.followUser(userId)
    }

    fun initParams(userId: Int) {
        this.userId = userId.toString()
    }
}