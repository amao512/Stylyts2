package kz.eztech.stylyts.home.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.posts.PostModel
import kz.eztech.stylyts.global.domain.repositories.PostsDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetHomePagePostsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<ResultsModel<PostModel>>(executorThread, uiThread) {

    private lateinit var queryMap: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<PostModel>> {
        return postsDomainRepository.getHomepagePosts(queryMap)
    }

    fun initParams(page: Int) {
        val queryMap = HashMap<String, String>()

        queryMap["page"] = page.toString()

        this.queryMap = queryMap
    }
}