package kz.eztech.stylyts.domain.usecases.collection_constructor

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.repository.collection_constructor.CollectionConstructorDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

class CreatePublicationUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val collectionConstructorDomainRepository: CollectionConstructorDomainRepository
) : BaseUseCase<PublicationModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var bodyList: List<MultipartBody.Part>

    override fun createSingleObservable(): Single<PublicationModel> {
        return collectionConstructorDomainRepository.createPost(token, bodyList)
    }

    fun initParams(
        token: String,
        bodyList: List<MultipartBody.Part>
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.bodyList = bodyList
    }
}