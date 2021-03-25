package kz.eztech.stylyts.profile.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
import kz.eztech.stylyts.common.data.api.RestConstants
import kz.eztech.stylyts.common.domain.usecases.BaseUseCase
import kz.eztech.stylyts.profile.domain.repository.ProfileDomainRepository
import kz.eztech.stylyts.search.domain.models.SearchModel
import javax.inject.Inject
import javax.inject.Named

class GetMyPublicationsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<SearchModel<PublicationModel>>(executorThread, uiThread) {

    private lateinit var token: String

    override fun createSingleObservable(): Single<SearchModel<PublicationModel>> {
        return profileDomainRepository.getMyPublications(token)
    }

    fun initParams(token: String) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
    }
}