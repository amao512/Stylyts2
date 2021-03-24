package kz.eztech.stylyts.constructor.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.constructor.domain.models.FilteredItemsModel

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
interface FilteredItemsDomainRepository {

    fun getFilteredItems(
        token: String,
        map: Map<String, Any>
    ): Single<FilteredItemsModel>
}