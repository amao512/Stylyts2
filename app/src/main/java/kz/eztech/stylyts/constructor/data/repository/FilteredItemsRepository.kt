package kz.eztech.stylyts.constructor.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.common.data.api.API
import kz.eztech.stylyts.common.data.exception.NetworkException
import kz.eztech.stylyts.constructor.domain.models.FilteredItemsModel
import kz.eztech.stylyts.constructor.domain.repository.FilteredItemsDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class FilteredItemsRepository @Inject constructor(
    private var api: API
) : FilteredItemsDomainRepository {

    override fun getFilteredItems(
        token: String,
        map: Map<String, Any>
    ): Single<FilteredItemsModel> {
        return api.getFilteredItems(token, map).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}