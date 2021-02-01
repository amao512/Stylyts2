package kz.eztech.stylyts.domain.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.QueryMap

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
interface FilteredItemsDomainRepository {
	fun getFilteredItems(token: String, map:Map<String,Any>): Single<FilteredItemsModel>
}