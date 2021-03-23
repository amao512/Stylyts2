package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.usecases.main.GetItemDetailUseCase
import kz.eztech.stylyts.domain.usecases.main.SaveItemByPhotoUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.constructor.CleanBackgroundContract
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 24.02.2021.
 */
class CleanBackgroundPresenter:CleanBackgroundContract.Presenter {
    private var errorHelper: ErrorHelper
    private var getItemDetailUseCase: GetItemDetailUseCase
    private var saveItemByPhotoUseCase: SaveItemByPhotoUseCase
    private lateinit var view: CleanBackgroundContract.View
    @Inject
    constructor(errorHelper: ErrorHelper,
                getItemDetailUseCase: GetItemDetailUseCase,
                saveItemByPhotoUseCase: SaveItemByPhotoUseCase
    ){
        this.getItemDetailUseCase = getItemDetailUseCase
        this.saveItemByPhotoUseCase = saveItemByPhotoUseCase
        this.errorHelper = errorHelper
    }

    override fun disposeRequests() {
        getItemDetailUseCase.clear()
        saveItemByPhotoUseCase.clear()
    }

    override fun attach(view: CleanBackgroundContract.View) {
        this.view = view
    }

    override fun saveItem(token: String, hashMap: HashMap<String, String>, data: File) {
        view.displayProgress()
        val requestFile = data.asRequestBody(("image/*").toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", data.name, requestFile)
        val clothesList = ArrayList<MultipartBody.Part>()

        clothesList.add(body)
        clothesList.add(MultipartBody.Part.createFormData("description",hashMap["description"].toString()))
        clothesList.add(MultipartBody.Part.createFormData("clothes_type",hashMap["clothes_type"].toString()))

        saveItemByPhotoUseCase.initParams(token, clothesList)
        saveItemByPhotoUseCase.execute(object : DisposableSingleObserver<ClothesMainModel>() {
            override fun onSuccess(t: ClothesMainModel) {
                view.processViewAction {
                    hideProgress()
                }
                getItemDetail(token,t.id?:0)
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun getItemDetail(token: String, id: Int) {
        view.displayProgress()
        getItemDetailUseCase.initParams(token,id)
        getItemDetailUseCase.execute(object : DisposableSingleObserver<ClothesMainModel>(){
            override fun onSuccess(t: ClothesMainModel) {
                view.processViewAction {
                    hideProgress()
                    processItemDetail(t)
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
}