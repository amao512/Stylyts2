package kz.eztech.stylyts.domain.usecases.search

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.repository.search.SearchDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchUserUseCase @Inject constructor(
	@Named("executor_thread") executorThread: Scheduler,
	@Named("ui_thread") uiThread: Scheduler,
	private var searchDomainRepository: SearchDomainRepository
) : BaseUseCase<ResultsModel<UserModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var username: String

    override fun createSingleObservable(): Single<ResultsModel<UserModel>> {
        return searchDomainRepository.getUserByUsername(token, username)
    }

    fun initParams(token: String, username: String) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.username = username
    }
}