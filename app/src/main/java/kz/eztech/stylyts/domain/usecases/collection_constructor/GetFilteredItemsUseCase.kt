package kz.eztech.stylyts.domain.usecases.collection_constructor

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import kz.eztech.stylyts.domain.repository.collection_constructor.FilteredItemsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class GetFilteredItemsUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var filteredItemsDomainRepository: FilteredItemsDomainRepository
) : BaseUseCase<FilteredItemsModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var map: Map<String, Any>

    override fun createSingleObservable(): Single<FilteredItemsModel> {
        return filteredItemsDomainRepository.getFilteredItems(token, map)
    }

    fun initParams(
        token: String,
        map: Map<String, Any>
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.map = map
    }
}