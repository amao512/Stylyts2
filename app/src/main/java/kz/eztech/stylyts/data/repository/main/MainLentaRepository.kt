package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.repository.main.MainLentaDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLentaRepository:MainLentaDomainRepository{
	private var api: API
	
	@Inject
	constructor(api: API){
		this.api = api
	}
	
	override fun getCollections(token: String, queries: Map<String, Any>?): Single<MainLentaModel> {
		queries?.let {
			return api.getCollections(token,queries).map {
				when(it.isSuccessful){
					true -> it.body()
					false -> throw NetworkException(it)
				}
			}
		} ?: run {
			return api.getCollections(token,HashMap()).map {
				when(it.isSuccessful){
					true -> it.body()
					false -> throw NetworkException(it)
				}
			}
		}
	}
}