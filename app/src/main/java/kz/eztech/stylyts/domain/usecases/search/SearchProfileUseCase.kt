package kz.eztech.stylyts.domain.usecases.search

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.repository.SearchDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class SearchProfileUseCase @Inject constructor(
	@Named("executor_thread") executorThread: Scheduler,
	@Named("ui_thread") uiThread: Scheduler,
	private var searchDomainRepository: SearchDomainRepository
) : BaseUseCase<ResultsModel<UserModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var username: String
    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<UserModel>> {
        return searchDomainRepository.searchProfileByUsername(
            token = token,
            username = username,
            map = map
        )
    }

    fun initParams(
        token: String,
        username: String,
        isBrand: Boolean
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.username = username

        val map = HashMap<String, String>()

        map["is_brand"] = isBrand.toString()

        this.map = map
    }
}