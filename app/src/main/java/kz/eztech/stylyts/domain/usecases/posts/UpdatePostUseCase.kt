package kz.eztech.stylyts.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.posts.TagApiModel
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.repository.PostsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

class UpdatePostUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<PostModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var postId: String
    private lateinit var description: MultipartBody.Part
    private lateinit var tags: TagsApiModel
    private lateinit var hidden: MultipartBody.Part

    override fun createSingleObservable(): Single<PostModel> {
        return postsDomainRepository.updatePost(
            token = token,
            postId = postId,
            description = description,
            tags = tags,
            hidden = hidden
        )
    }

    fun initParams(
        token: String,
        postId: Int,
        postCreateModel: PostCreateModel
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.postId = postId.toString()
        this.description = MultipartBody.Part.createFormData("description", postCreateModel.description)


        val clothesTags: MutableList<TagApiModel> = mutableListOf()
        postCreateModel.clothesList.map {
            clothesTags.add(
                TagApiModel(
                    id = it.id,
                    title = it.title,
                    pointX = it.clothesLocation?.pointX,
                    pointY = it.clothesLocation?.pointY
                )
            )
        }

        val userTags: MutableList<TagApiModel> = mutableListOf()
        postCreateModel.userList.map {
            userTags.add(
                TagApiModel(
                    id = it.id,
                    title = it.username,
                    pointX = it.userLocation?.pointX,
                    pointY = it.userLocation?.pointY
                )
            )
        }

        this.tags = TagsApiModel(
            clothesTags = clothesTags,
            usersTags = userTags
        )
        this.hidden = MultipartBody.Part.createFormData("hidden", postCreateModel.hidden.toString())
    }
}