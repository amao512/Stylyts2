package kz.eztech.stylyts.presentation.presenters.main.constructor

import android.R.attr.description
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
		val json = Gson().toJson(model).toRequestBody("application/json".toMediaTypeOrNull())
		val json2 = Gson().toJson(model)
		val requestBody = MultipartBody.Builder().apply {
			setType(MultipartBody.FORM)
			addPart(body)
			addFormDataPart("title", model.title.toString())
			//addFormDataPart("clothes",null,Gson().toJson(model.clothes).toRequestBody((mediaType).toMediaTypeOrNull()))
			//addFormDataPart("clothes_location",null,Gson().toJson(model.clothes_location).toRequestBody((mediaType).toMediaTypeOrNull()))
			addFormDataPart("style", model.style.toString())
			addFormDataPart("author", model.author.toString())
			addFormDataPart("total_price", model.total_price.toString())
			addFormDataPart("text", model.text.toString())
		}.build()
		
		val map: HashMap<String, RequestBody> = HashMap()
		map["title"] =  model.title.toString().toRequestBody((MultipartBody.FORM))
		map["text"] = model.text.toString().toRequestBody((MultipartBody.FORM))
		map["style"] = model.style.toString().toRequestBody((MultipartBody.FORM))
		map["author"] = model.author.toString().toRequestBody((MultipartBody.FORM))
		map["total_price"] = model.total_price.toString().toRequestBody((MultipartBody.FORM))
		model.clothes?.forEachIndexed { index, i ->
			//map["clothes"] = i.toString().toRequestBody(MultipartBody.FORM)
		}
		
		model.clothes_location?.forEachIndexed { index, clothesCollection ->
			map["clothes_location[${index}]clothes_id"] = clothesCollection.clothes_id.toString().toRequestBody(MultipartBody.FORM)
			map["clothes_location[${index}]point_x"] = clothesCollection.point_x.toString().toRequestBody(MultipartBody.FORM)
			map["clothes_location[${index}]point_y"] = clothesCollection.point_y.toString().toRequestBody(MultipartBody.FORM)
			map["clothes_location[${index}]width"] = clothesCollection.width.toString().toRequestBody(MultipartBody.FORM)
			map["clothes_location[${index}]height"] = clothesCollection.height.toString().toRequestBody(MultipartBody.FORM)
			map["clothes_location[${index}]degree"] = clothesCollection.degree.toString().toRequestBody(MultipartBody.FORM)
		}
		/*val sJsonAttachment = JSONObject()
		sJsonAttachment.put("title", model.title)
		sJsonAttachment.put("clothes", model.clothes)
		sJsonAttachment.put("clothes_location", model.clothes_location)
		sJsonAttachment.put("style", model.style)
		sJsonAttachment.put("author", model.author)
		sJsonAttachment.put("total_price", model.total_price)
		sJsonAttachment.put("text", model.text)
		val bodyJsonAttachment = (sJsonAttachment.toString()).toRequestBody(MultipartBody.FORM)*/
		//saveCollectionConstructor.initParam(token,requestBody)
		saveCollectionConstructor.initParam(token, map, body)
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