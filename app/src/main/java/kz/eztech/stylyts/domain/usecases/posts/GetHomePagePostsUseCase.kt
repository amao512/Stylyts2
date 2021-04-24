package kz.eztech.stylyts.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.repository.posts.PostsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetHomePagePostsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<ResultsModel<PostModel>>(executorThread, uiThread) {

    private lateinit var token: String

    override fun createSingleObservable(): Single<ResultsModel<PostModel>> {
        return postsDomainRepository.getHomepagePosts(token)
    }

    fun initParams(token: String) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
    }
}