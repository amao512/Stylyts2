package kz.eztech.stylyts.domain.usecases.main.shop

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.domain.repository.main.ShopCategoryDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class GetCategoryTypeDetailUseCase:BaseUseCase<CategoryTypeDetailModel> {
	private var shopCategoryDomainRepository: ShopCategoryDomainRepository
	private lateinit var data:HashMap<String,Any>
	@Inject
	constructor(@Named("executor_thread") executorThread: Scheduler,
	            @Named("ui_thread") uiThread: Scheduler,
	            shopCategoryDomainRepository: ShopCategoryDomainRepository
	) :
			super(executorThread, uiThread) {
		this.shopCategoryDomainRepository = shopCategoryDomainRepository
	}
	fun initParams(data:HashMap<String,Any>){
		this.data = data
	}
	override fun createSingleObservable(): Single<CategoryTypeDetailModel> {
		return shopCategoryDomainRepository.getCategoryDetail(data)
	}
}