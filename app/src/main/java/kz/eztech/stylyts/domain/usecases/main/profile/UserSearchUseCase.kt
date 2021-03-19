package kz.eztech.stylyts.domain.usecases.main.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.SearchModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.main.UserSearchDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class UserSearchUseCase @Inject constructor(
	@Named("executor_thread") executorThread: Scheduler,
	@Named("ui_thread") uiThread: Scheduler,
	private var userSearchDomainRepository: UserSearchDomainRepository
) : BaseUseCase<SearchModel<UserModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var username: String

    override fun createSingleObservable(): Single<SearchModel<UserModel>> {
        return userSearchDomainRepository.getUserByUsername(token, username)
    }

    fun initParams(token: String, username: String) {
        this.token = "JWT $token"
        this.username = username
    }
}