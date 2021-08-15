package kz.eztech.stylyts.global.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.repositories.PostsDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class DeletePostUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<Any>(executorThread, uiThread) {

    private lateinit var postId: String

    override fun createSingleObservable(): Single<Any> {
        return postsDomainRepository.deletePost(postId)
    }

    fun initParams(postId: Int) {
        this.postId = postId.toString()
    }
}