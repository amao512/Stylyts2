package kz.eztech.stylyts.domain.usecases.user

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.repository.UserDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetFollowingsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val userDomainRepository: UserDomainRepository
) : BaseUseCase<ResultsModel<FollowerModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var userId: String

    override fun createSingleObservable(): Single<ResultsModel<FollowerModel>> {
        return userDomainRepository.getFollowingsById(token, userId)
    }

    fun initParams(
        token: String,
        userId: Int = 0
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.userId = when (userId) {
            0 -> "me"
            else -> userId.toString()
        }
    }
}