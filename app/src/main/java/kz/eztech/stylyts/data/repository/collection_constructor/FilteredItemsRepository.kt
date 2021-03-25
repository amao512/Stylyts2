package kz.eztech.stylyts.data.repository.collection_constructor

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import kz.eztech.stylyts.domain.repository.collection_constructor.FilteredItemsDomainRepository
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