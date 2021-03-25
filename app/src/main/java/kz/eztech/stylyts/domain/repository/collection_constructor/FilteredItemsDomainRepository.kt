package kz.eztech.stylyts.domain.repository.collection_constructor

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.FilteredItemsModel

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
interface FilteredItemsDomainRepository {

    fun getFilteredItems(
        token: String,
        map: Map<String, Any>
    ): Single<FilteredItemsModel>
}