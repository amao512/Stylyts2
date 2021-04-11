package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import kz.eztech.stylyts.domain.models.shop.ShopCategoryModel
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.domain.usecases.shop.SaveCollectionConstructor
import kz.eztech.stylyts.domain.usecases.collection_constructor.*
import kz.eztech.stylyts.presentation.contracts.collection_constructor.CollectionConstructorContract
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorPresenter @Inject constructor(
	private var errorHelper: ErrorHelper,
	private var getCategoryUseCase: GetCategoryUseCase,
	private var getStylesUseCase: GetStylesUseCase,
	private var getFilteredItemsUseCase: GetFilteredItemsUseCase,
	private var saveCollectionConstructor: SaveCollectionConstructor,
	private var updateCollectionUseCase: UpdateCollectionUseCase,
	private var saveCollectionToMeUseCase: SaveCollectionToMeUseCase
) : CollectionConstructorContract.Presenter{

	private lateinit var view: CollectionConstructorContract.View

	override fun disposeRequests() {
		getCategoryUseCase.clear()
		getStylesUseCase.clear()
		saveCollectionConstructor.clear()
		getFilteredItemsUseCase.clear()
		updateCollectionUseCase.clear()
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
	
	override fun getShopCategoryTypeDetail(token: String, map: Map<String, Any>) {
		view.displayProgress()

		getFilteredItemsUseCase.initParams(token,map)
		getFilteredItemsUseCase.execute(object : DisposableSingleObserver<FilteredItemsModel>() {

			override fun onSuccess(t: FilteredItemsModel) {
				view.processViewAction {
					view.processFilteredItems(t)
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
		getStylesUseCase.execute(object : DisposableSingleObserver<List<Style>>() {
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

	override fun saveCollection(token: String, model: CollectionPostCreateModel, data: File) {
		view.displayProgress()

		val requestFile = data.asRequestBody(("image/*").toMediaTypeOrNull())
		val body = MultipartBody.Part.createFormData("cover_photo", data.name, requestFile)
		val clothesList = ArrayList<MultipartBody.Part>()
		model.clothes?.forEach {
			clothesList.add(MultipartBody.Part.createFormData("clothes",it.toString()))
		}
		
		clothesList.add(body)
		model.clothes_location?.forEachIndexed { index, clothesCollection ->
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]clothes_id",clothesCollection.clothes_id.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]point_x",clothesCollection.point_x.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]point_y",clothesCollection.point_y.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]width",clothesCollection.width.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]height",clothesCollection.height.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]degree",clothesCollection.degree.toString()))
		}
		clothesList.add(MultipartBody.Part.createFormData("title",model.title.toString()))
		clothesList.add(MultipartBody.Part.createFormData("text",model.text.toString()))
		clothesList.add(MultipartBody.Part.createFormData("style",model.style.toString()))
		clothesList.add(MultipartBody.Part.createFormData("author",model.author.toString()))
		clothesList.add(MultipartBody.Part.createFormData("total_price",model.total_price.toString()))
		
		saveCollectionConstructor.initParam(token, clothesList)
		saveCollectionConstructor.execute(object : DisposableSingleObserver<MainResult>() {
			override fun onSuccess(t: MainResult) {
				view.processViewAction {
					view.processSuccess(t)
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
	
	override fun updateCollection(token: String, id: Int, model: CollectionPostCreateModel, data: File) {
		view.displayProgress()
		val requestFile = data.asRequestBody(("image/*").toMediaTypeOrNull())
		val body = MultipartBody.Part.createFormData("cover_photo", data.name, requestFile)
		val clothesList = ArrayList<MultipartBody.Part>()
		model.clothes?.forEach {
			clothesList.add(MultipartBody.Part.createFormData("clothes",it.toString()))
		}
		
		clothesList.add(body)
		model.clothes_location?.forEachIndexed { index, clothesCollection ->
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]clothes_id",clothesCollection.clothes_id.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]point_x",clothesCollection.point_x.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]point_y",clothesCollection.point_y.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]width",clothesCollection.width.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]height",clothesCollection.height.toString()))
			clothesList.add(MultipartBody.Part.createFormData("clothes_location[${index}]degree",clothesCollection.degree.toString()))
		}
		clothesList.add(MultipartBody.Part.createFormData("title",model.title.toString()))
		clothesList.add(MultipartBody.Part.createFormData("text",model.text.toString()))
		clothesList.add(MultipartBody.Part.createFormData("style",model.style.toString()))
		clothesList.add(MultipartBody.Part.createFormData("author",model.author.toString()))
		clothesList.add(MultipartBody.Part.createFormData("total_price",model.total_price.toString()))
		
		updateCollectionUseCase.initParam(token, id,clothesList)
		updateCollectionUseCase.execute(object : DisposableSingleObserver<Unit>() {
			override fun onSuccess(t: Unit) {
				view.processViewAction {
					view.processSuccess(null)
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
	
	override fun saveCollectionToMe(token: String, id: Int) {
		saveCollectionToMeUseCase.initParam(token, id)
		saveCollectionToMeUseCase.execute(object : DisposableSingleObserver<Unit>() {
			override fun onSuccess(t: Unit) {
				view.processViewAction {
					view.processSuccess(null)
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