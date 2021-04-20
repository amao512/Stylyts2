package kz.eztech.stylyts.domain.usecases.collection_constructor

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.posts.TagApiModel
import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.repository.collection_constructor.CollectionConstructorDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject
import javax.inject.Named

class CreatePublicationUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val collectionConstructorDomainRepository: CollectionConstructorDomainRepository
) : BaseUseCase<PublicationModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var description: String
    private lateinit var imageList: List<MultipartBody.Part>
    private lateinit var tags: TagsApiModel

    override fun createSingleObservable(): Single<PublicationModel> {
        return collectionConstructorDomainRepository.createPost(
            token = token,
            imageList = imageList,
            description = description,
            tags = tags
        )
    }

    fun initParams(
        token: String,
        postCreateModel: PostCreateModel
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.description = postCreateModel.description

        val bodyList = ArrayList<MultipartBody.Part>()
        val requestFile = postCreateModel.imageFile.asRequestBody(("image/*").toMediaTypeOrNull())

        bodyList.add(MultipartBody.Part.createFormData("image_one", postCreateModel.imageFile.name, requestFile))

        this.imageList = bodyList

        val clothesTags: MutableList<TagApiModel> = mutableListOf()
        postCreateModel.clothesList.map {
            clothesTags.add(
                TagApiModel(
                    id = it.id,
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
                    pointX = it.userLocation?.pointX,
                    pointY = it.userLocation?.pointY
                )
            )
        }

        this.tags = TagsApiModel(
            clothesTags = clothesTags,
            usersTags = userTags
        )
    }
}