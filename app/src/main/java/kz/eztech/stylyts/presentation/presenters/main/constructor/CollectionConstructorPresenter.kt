package kz.eztech.stylyts.presentation.presenters.main.constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.domain.models.Style
import kz.eztech.stylyts.domain.usecases.main.shop.GetCategoryTypeDetailUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.GetCategoryUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.GetStylesUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.SaveCollectionConstructor
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.constructor.CollectionConstructorContract
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorPresenter : CollectionConstructorContract.Presenter{
	private var errorHelper: ErrorHelper
	private var getCategoryUseCase: GetCategoryUseCase
	private var getCategoryTypeDetailUseCase: GetCategoryTypeDetailUseCase
	private var getStylesUseCase: GetStylesUseCase
	private var saveCollectionConstructor: SaveCollectionConstructor
	private lateinit var view: CollectionConstructorContract.View
	@Inject
	constructor(errorHelper: ErrorHelper,
	            getCategoryUseCase: GetCategoryUseCase,
				getCategoryTypeDetailUseCase: GetCategoryTypeDetailUseCase,
				getStylesUseCase: GetStylesUseCase,
				saveCollectionConstructor: SaveCollectionConstructor
	){
		this.getCategoryTypeDetailUseCase = getCategoryTypeDetailUseCase
		this.getCategoryUseCase = getCategoryUseCase
		this.errorHelper = errorHelper
		this.saveCollectionConstructor = saveCollectionConstructor
		this.getStylesUseCase = getStylesUseCase
	}
	override fun disposeRequests() {
		getCategoryUseCase.clear()
		getCategoryTypeDetailUseCase.clear()
		getStylesUseCase.clear()
		saveCollectionConstructor.clear()
	}
	
	override fun attach(view: CollectionConstructorContract.View) {
		this.view = view
	}
	
	override fun getCategory() {
		view.displayProgress()
		getCategoryUseCase.execute(object : DisposableSingleObserver<ShopCategoryModel>() {
			override fun onSuccess(t: ShopCategoryModel) {
				view.processViewAction {
					hideProgress()
					processShopCategories(t)
				}
				
			}
			
			override fun onError(e: Throwable) {
				view.processViewAction {
					hideProgress()
					displayMessage(errorHelper.processError(e))
				}
				
			}
		})
	}
	override fun getShopCategoryTypeDetail(typeId:Int,gender:String) {
		view.displayProgress()
		val map = HashMap<String,Any>()
		map["id"] = typeId
		map["gender_type"] = gender
		getCategoryTypeDetailUseCase.initParams(map)
		getCategoryTypeDetailUseCase.execute(object : DisposableSingleObserver<CategoryTypeDetailModel>(){
			override fun onSuccess(t: CategoryTypeDetailModel) {
				view.processViewAction {
					view.processTypeDetail(t)
					hideProgress()
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					hideProgress()
					displayMessage(errorHelper.processError(e))
				}
			}
		})
	}

	override fun getStyles(token: String) {
		view.displayProgress()
		getStylesUseCase.initParam(token)
		getStylesUseCase.execute(object : DisposableSingleObserver<List<Style>>(){
			override fun onSuccess(t: List<Style>) {
				view.processViewAction {
					view.processStyles(t)
					hideProgress()
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					displayMessage(errorHelper.processError(e))
					hideProgress()
				}
			}
		})
	}

	override fun saveCollection(token: String, model: CollectionPostCreateModel) {
		view.displayProgress()
		saveCollectionConstructor.initParam(token,model)
		saveCollectionConstructor.execute(object : DisposableSingleObserver<Unit>(){
			override fun onSuccess(t: Unit) {
				view.processViewAction {
					view.processSuccess()
					hideProgress()
				}
			}

			override fun onError(e: Throwable) {
				view.processViewAction {
					displayMessage(errorHelper.processError(e))
					hideProgress()
				}
			}
		})
	}
}