package kz.eztech.stylyts.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.repository.posts.PostsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetPostByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<PostModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var postId: String

    override fun createSingleObservable(): Single<PostModel> {
        return postsDomainRepository.getPostById(token, postId)
    }

    fun initParams(
        token: String,
        postId: Int
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.postId = postId.toString()
    }
}