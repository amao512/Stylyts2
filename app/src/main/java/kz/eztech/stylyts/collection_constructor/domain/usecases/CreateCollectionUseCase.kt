package kz.eztech.stylyts.collection_constructor.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.common.data.api.RestConstants
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
import kz.eztech.stylyts.collection_constructor.domain.repository.CollectionConstructorDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

class CreateCollectionUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val collectionConstructorDomainRepository: CollectionConstructorDomainRepository
) : BaseUseCase<PublicationModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var description: String
    private lateinit var tags: String
    private lateinit var imageOne: MultipartBody.Part

    private var hidden: Boolean = false

    override fun createSingleObservable(): Single<PublicationModel> {
        return collectionConstructorDomainRepository.createPost(
            token = token,
            description = description,
            tags = tags,
            imageOne = imageOne,
            hidden = hidden
        )
    }

    fun initParams(
        token: String,
        description: String,
        tags: String,
        imageOne: MultipartBody.Part,
        hidden: Boolean
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.description = description
        this.tags = tags
        this.imageOne = imageOne
        this.hidden = hidden
    }
}