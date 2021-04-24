package kz.eztech.stylyts.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.repository.posts.PostsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
import javax.inject.Inject
import javax.inject.Named

class GetPostsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : BaseUseCase<ResultsModel<PostModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var queryMap: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<PostModel>> {
        return postsDomainRepository.getPosts(token, queryMap)
            .map {
                it.results.forEach { post ->
                    getUserByIdUseCase.initParams(token.substring(4), post.author.toString())
                    getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
                        override fun onSuccess(t: UserModel) {
                            post.owner = t
                        }

                        override fun onError(e: Throwable) {}
                    })
                }

                it
            }
    }

    fun initParams(
        token: String,
        authorId: Int = 0
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)

        val map = HashMap<String, String>()

        if (authorId != 0) {
            map["author"] = authorId.toString()
        }

        this.queryMap = map
    }
}