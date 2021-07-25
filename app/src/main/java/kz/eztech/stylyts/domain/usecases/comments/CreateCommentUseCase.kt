package kz.eztech.stylyts.domain.usecases.comments

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.repository.CommentsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class CreateCommentUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val commentsDomainRepository: CommentsDomainRepository
) : BaseUseCase<CommentModel>(executorThread, uiThread) {

    private lateinit var commentCreateModel: CommentCreateModel

    override fun createSingleObservable(): Single<CommentModel> {
        return commentsDomainRepository.createComment(commentCreateModel)
    }

    fun initParams(commentCreateModel: CommentCreateModel) {
        this.commentCreateModel = commentCreateModel
    }
}