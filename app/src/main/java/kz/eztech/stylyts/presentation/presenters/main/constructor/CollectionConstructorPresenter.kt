package kz.eztech.stylyts.presentation.presenters.main.constructor

import com.google.gson.Gson
import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.domain.usecases.main.shop.GetCategoryTypeDetailUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.GetCategoryUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.GetStylesUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.SaveCollectionConstructor
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.constructor.CollectionConstructorContract
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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
	override fun getShopCategoryTypeDetail(typeId: Int, gender: String) {
		view.displayProgress()
		val map = HashMap<String, Any>()
		map["id"] = typeId
		map["gender_type"] = gender
		getCategoryTypeDetailUseCase.initParams(map)
		getCategoryTypeDetailUseCase.execute(object : DisposableSingleObserver<CategoryTypeDetailModel>() {
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
		val mediaType = "multipart/form-data"
		val requestBody = MultipartBody.Builder().apply {
			addPart(body)
			addFormDataPart("title",null,model.title!!.toRequestBody((mediaType).toMediaTypeOrNull()))
			//addFormDataPart("clothes",null,Gson().toJson(model.clothes).toRequestBody((mediaType).toMediaTypeOrNull()))
			//addFormDataPart("clothes_location",null,Gson().toJson(model.clothes_location).toRequestBody((mediaType).toMediaTypeOrNull()))
			//addFormDataPart("style",null,Gson().toJson(model.style).toRequestBody((mediaType).toMediaTypeOrNull()))
			//addFormDataPart("author",null,Gson().toJson(model.author).toRequestBody((mediaType).toMediaTypeOrNull()))
			//addFormDataPart("total_price",null,Gson().toJson(model.total_price).toRequestBody((mediaType).toMediaTypeOrNull()))
			//addFormDataPart("text",null,Gson().toJson(model.text).toRequestBody((mediaType).toMediaTypeOrNull()))
		}.build()
		/*val sJsonAttachment = JSONObject()
		sJsonAttachment.put("title", model.title)
		sJsonAttachment.put("clothes", model.clothes)
		sJsonAttachment.put("clothes_location", model.clothes_location)
		sJsonAttachment.put("style", model.style)
		sJsonAttachment.put("author", model.author)
		sJsonAttachment.put("total_price", model.total_price)
		sJsonAttachment.put("text", model.text)
		val bodyJsonAttachment = (sJsonAttachment.toString()).toRequestBody(MultipartBody.FORM)*/
		val json = Gson().toJson(model).toRequestBody("application/json".toMediaTypeOrNull())
		saveCollectionConstructor.initParam(token, json, body)
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
}