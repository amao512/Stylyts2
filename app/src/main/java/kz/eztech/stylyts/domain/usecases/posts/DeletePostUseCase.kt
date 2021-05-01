package kz.eztech.stylyts.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.repository.PostsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class DeletePostUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var postId: String

    override fun createSingleObservable(): Single<Any> {
        return postsDomainRepository.deletePost(token, postId)
    }

    fun initParams(
        token: String,
        postId: Int
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.postId = postId.toString()
    }
}