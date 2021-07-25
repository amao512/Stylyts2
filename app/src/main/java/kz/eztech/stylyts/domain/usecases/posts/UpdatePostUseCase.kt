package kz.eztech.stylyts.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.models.posts.TagApiModel
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.repository.PostsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

class UpdatePostUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<PostCreateModel>(executorThread, uiThread) {

    private lateinit var postId: String
    private lateinit var tags: TagsApiModel
    private lateinit var multipartList: List<MultipartBody.Part>

    override fun createSingleObservable(): Single<PostCreateModel> {
        return postsDomainRepository.updatePost(
            postId = postId,
            tags = tags,
            multipartList = multipartList
        )
    }

    fun initParams(
        postId: Int,
        postCreateModel: PostCreateModel
    ) {
        this.postId = postId.toString()

        val multipartList = ArrayList<MultipartBody.Part>()

        multipartList.add(MultipartBody.Part.createFormData("description", postCreateModel.description))
        multipartList.add(MultipartBody.Part.createFormData("hidden", postCreateModel.hidden.toString()))

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

            multipartList.add(
                MultipartBody.Part.createFormData("clothes", it.id.toString())
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
        this.multipartList = multipartList
    }
}