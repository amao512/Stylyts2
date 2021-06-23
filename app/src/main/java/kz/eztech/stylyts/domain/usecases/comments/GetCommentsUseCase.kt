package kz.eztech.stylyts.domain.usecases.comments

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.common.PageFilterModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.repository.CommentsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetCommentsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val commentsDomainRepository: CommentsDomainRepository
) : BaseUseCase<ResultsModel<CommentModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<CommentModel>> {
        return commentsDomainRepository.getComments(token, map)
    }

    fun initParams(
        token: String,
        postId: Int,
        pageFilterModel: PageFilterModel
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val map = HashMap<String, String>()
        map["post"] = postId.toString()
        map["page"] = pageFilterModel.page.toString()

        this.map = map
    }
}