package kz.eztech.stylyts.domain.usecases.main

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import kz.eztech.stylyts.domain.repository.main.FilteredItemsDomainRepository
import kz.eztech.stylyts.domain.repository.main.ItemDetailDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class GetFilteredItemsUseCase:BaseUseCase<FilteredItemsModel> {
	private var filteredItemsDomainRepository: FilteredItemsDomainRepository
	private lateinit var token:String
	private lateinit var map: Map<String,Any>
	@Inject
	constructor(@Named("executor_thread") executorThread: Scheduler,
	            @Named("ui_thread") uiThread: Scheduler,
	            filteredItemsDomainRepository: FilteredItemsDomainRepository
	) :
			super(executorThread, uiThread) {
		this.filteredItemsDomainRepository = filteredItemsDomainRepository
	}
	
	fun initParams(token:String,map:Map<String,Any>){
		this.token = token
		this.map = map
	}
	
	
	override fun createSingleObservable(): Single<FilteredItemsModel> {
		return filteredItemsDomainRepository.getFilteredItems(token,map)
	}
}