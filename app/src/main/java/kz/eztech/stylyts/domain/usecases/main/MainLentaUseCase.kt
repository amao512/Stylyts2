package kz.eztech.stylyts.domain.usecases.main

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.main.MainLentaDomainRepository
import kz.eztech.stylyts.domain.repository.main.ProfileDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
class MainLentaUseCase: BaseUseCase<MainLentaModel> {
	private var mainLentaDomainRepository: MainLentaDomainRepository
	private lateinit var token:String
	private var queries:Map<String,Any>? = null
	@Inject
	constructor(@Named("executor_thread") executorThread: Scheduler,
	            @Named("ui_thread") uiThread: Scheduler,
	            mainLentaDomainRepository: MainLentaDomainRepository
	) :
			super(executorThread, uiThread) {
		this.mainLentaDomainRepository = mainLentaDomainRepository
	}
	
	fun initParams(token:String,queries:Map<String,Any>? = null) {
		this.token = "Bearer $token"
		this.queries = queries
	}
	
	override fun createSingleObservable(): Single<MainLentaModel> {
		return mainLentaDomainRepository.getCollections(token,queries)
	}
}