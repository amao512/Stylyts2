package kz.eztech.stylyts.search.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.common.data.api.RestConstants
import kz.eztech.stylyts.search.domain.models.SearchModel
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.search.domain.repository.SearchDomainRepository
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchUserUseCase @Inject constructor(
	@Named("executor_thread") executorThread: Scheduler,
	@Named("ui_thread") uiThread: Scheduler,
	private var searchDomainRepository: SearchDomainRepository
) : BaseUseCase<SearchModel<UserModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var username: String

    override fun createSingleObservable(): Single<SearchModel<UserModel>> {
        return searchDomainRepository.getUserByUsername(token, username)
    }

    fun initParams(token: String, username: String) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.username = username
    }
}