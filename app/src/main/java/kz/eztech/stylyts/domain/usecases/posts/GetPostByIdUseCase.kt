package kz.eztech.stylyts.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.repository.PostsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetPostByIdUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<PostModel>(executorThread, uiThread) {

    private lateinit var postId: String

    override fun createSingleObservable(): Single<PostModel> {
        return postsDomainRepository.getPostById(postId)
    }

    fun initParams(postId: Int) {
        this.postId = postId.toString()
    }
}