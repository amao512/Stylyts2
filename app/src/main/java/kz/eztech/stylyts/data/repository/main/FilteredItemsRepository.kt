package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import kz.eztech.stylyts.domain.repository.main.FilteredItemsDomainRepository
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class FilteredItemsRepository: FilteredItemsDomainRepository {
	
	private var api: API
	
	@Inject
	constructor(api: API){
		this.api = api
	}
	
	
	override fun getFilteredItems(token: String, map: Map<String, Any>): Single<FilteredItemsModel> {
		return api.getFilteredItems(token,map).map {
			when(it.isSuccessful){
				true -> it.body()
				else ->  throw NetworkException(it)
			}
		}
	}
}