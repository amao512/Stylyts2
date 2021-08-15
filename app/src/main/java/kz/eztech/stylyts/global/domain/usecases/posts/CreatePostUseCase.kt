package kz.eztech.stylyts.global.domain.usecases.posts

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.data.models.posts.TagApiModel
import kz.eztech.stylyts.global.data.models.posts.TagsApiModel
import kz.eztech.stylyts.global.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.global.domain.repositories.PostsDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject
import javax.inject.Named

class CreatePostUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val postsDomainRepository: PostsDomainRepository
) : BaseUseCase<PostCreateModel>(executorThread, uiThread) {

    private lateinit var multipartList: List<MultipartBody.Part>
    private lateinit var tags: TagsApiModel

    override fun createSingleObservable(): Single<PostCreateModel> {
        return postsDomainRepository.createPost(
            multipartList = multipartList,
            tags = tags
        )
    }

    fun initParams(postCreateModel: PostCreateModel) {
        val multipartList = ArrayList<MultipartBody.Part>()
        val requestFile = postCreateModel.imageFile?.asRequestBody(("image/*").toMediaTypeOrNull())

        multipartList.add(MultipartBody.Part.createFormData("description", postCreateModel.description))
        multipartList.add(MultipartBody.Part.createFormData("hidden", postCreateModel.hidden.toString()))

        requestFile?.let {
            multipartList.add(MultipartBody.Part.createFormData("image_one", postCreateModel.imageFile.name, it))
        }

        var count = 2

        postCreateModel.images.map {
            val request = it.asRequestBody(("image/*").toMediaTypeOrNull())

            multipartList.add(
                when (count) {
                    2 -> MultipartBody.Part.createFormData("image_two", it.name, request)
                    3 -> MultipartBody.Part.createFormData("image_three", it.name, request)
                    4 -> MultipartBody.Part.createFormData("image_four", it.name, request)
                    else -> MultipartBody.Part.createFormData("image_five", it.name, request)
                }
            )

            count++
        }

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

        this.multipartList = multipartList
        this.tags = TagsApiModel(
            clothesTags = clothesTags,
            usersTags = userTags
        )
    }
}