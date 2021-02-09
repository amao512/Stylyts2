package kz.eztech.stylyts.presentation.presenters.main.constructor

import android.R.attr.description
import com.google.gson.Gson
import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.domain.usecases.main.GetFilteredItemsUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.*
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.constructor.CollectionConstructorContract
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart
import java.io.File
import javax.inject.Inject


/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorPresenter : CollectionConstructorContract.Presenter{
	private var errorHelper: ErrorHelper
	private var getCategoryUseCase: GetCategoryUseCase
	private var getCategoryTypeDetailUseCase: GetCategoryTypeDetailUseCase
	private var getFilteredItemsUseCase: GetFilteredItemsUseCase
	private var getStylesUseCase: GetStylesUseCase
	private var saveCollectionConstructor: SaveCollectionConstructor
	private var updateCollectionUseCase: UpdateCollectionUseCase
	private lateinit var view: CollectionConstructorContract.View
	@Inject
	constructor(errorHelper: ErrorHelper,
	            getCategoryUseCase: GetCategoryUseCase,
	            getCategoryTypeDetailUseCase: GetCategoryTypeDetailUseCase,
	            getStylesUseCase: GetStylesUseCase,
	            getFilteredItemsUseCase: GetFilteredItemsUseCase,
	            saveCollectionConstructor: SaveCollectionConstructor,
	            updateCollectionUseCase: UpdateCollectionUseCase
	){
		this.getFilteredItemsUseCase = getFilteredItemsUseCase
		this.getCategoryTypeDetailUseCase = getCategoryTypeDetailUseCase
		this.getCategoryUseCase = getCategoryUseCase
		this.errorHelper = errorHelper
		this.saveCollectionConstructor = saveCollectionConstructor
		this.getStylesUseCase = getStylesUseCase
		this.updateCollectionUseCase = updateCollectionUseCase
	}
	override fun disposeRequests() {
		getCategoryUseCase.clear()
		getCategoryTypeDetailUseCase.clear()
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
		saveCollectionConstructor.execute(object : DisposableSingleObserver<Unit>() {
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