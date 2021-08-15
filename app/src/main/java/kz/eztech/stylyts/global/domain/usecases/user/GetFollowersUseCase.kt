package kz.eztech.stylyts.global.domain.usecases.user

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.user.FollowerModel
import kz.eztech.stylyts.global.domain.repositories.UserDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import kz.eztech.stylyts.utils.EMPTY_STRING
import javax.inject.Inject
import javax.inject.Named

class GetFollowersUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val userDomainRepository: UserDomainRepository
) : BaseUseCase<ResultsModel<FollowerModel>>(executorThread, uiThread) {

    private lateinit var userId: String
    private lateinit var queryMap: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<FollowerModel>> {
        return userDomainRepository.getFollowersById(
            userId = userId,
            queryMap = queryMap
        )
    }

    fun initParams(
        userId: Int = 0,
        username: String = EMPTY_STRING
    ) {
        this.userId = when (userId) {
            0 -> "me"
            else -> userId.toString()
        }

        val map = HashMap<String, String>()

        if (username.isNotBlank()) {
            map["username"] = username
        }

        this.queryMap = map
    }
}