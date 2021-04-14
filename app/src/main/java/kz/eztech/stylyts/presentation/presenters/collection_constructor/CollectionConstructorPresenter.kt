package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesStylesUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.domain.usecases.shop.SaveOutfitConstructorUseCase
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
    private val errorHelper: ErrorHelper,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getStylesUseCase: GetStylesUseCase,
    private val getFilteredItemsUseCase: GetFilteredItemsUseCase,
    private val saveOutfitConstructorUseCase: SaveOutfitConstructorUseCase,
    private val updateCollectionUseCase: UpdateCollectionUseCase,
    private val saveCollectionToMeUseCase: SaveCollectionToMeUseCase,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getClothesUseCase: GetClothesUseCase,
    private val getClothesStylesUseCase: GetClothesStylesUseCase
) : CollectionConstructorContract.Presenter {

    private lateinit var view: CollectionConstructorContract.View

    override fun disposeRequests() {
        getCategoryUseCase.clear()
        getStylesUseCase.clear()
        saveOutfitConstructorUseCase.clear()
        getFilteredItemsUseCase.clear()
        updateCollectionUseCase.clear()
        getClothesTypesUseCase.clear()
        getClothesUseCase.clear()
        getClothesStylesUseCase.clear()
    }

    override fun attach(view: CollectionConstructorContract.View) {
        this.view = view
    }

    override fun getTypes(token: String) {
        view.displayProgress()

        getClothesTypesUseCase.initParams(token)
        getClothesTypesUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel>) {
                view.processViewAction {
                    processTypesResults(resultsModel = t)
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

    override fun getClothesByType(token: String, map: Map<String, Any>) {
        view.displayProgress()

        getClothesUseCase.initParams(
            token = token,
            gender = map["gender"] as String,
            clothesTypeId = map["clothes_type"] as Int
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processViewAction {
                    processClothesResults(resultsModel = t)
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

        getClothesStylesUseCase.initParams(token)
        getClothesStylesUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<ClothesStyleModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesStyleModel>) {
                view.processViewAction {
                    processStylesResults(resultsModel = t)
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
            clothesList.add(MultipartBody.Part.createFormData("clothes", it.toString()))
        }

        clothesList.add(body)
        model.clothes_location?.forEachIndexed { index, clothesCollection ->
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]clothes_id",
                    clothesCollection.clothes_id.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_x",
                    clothesCollection.point_x.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_y",
                    clothesCollection.point_y.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]width",
                    clothesCollection.width.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]height",
                    clothesCollection.height.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]degree",
                    clothesCollection.degree.toString()
                )
            )
        }
        clothesList.add(MultipartBody.Part.createFormData("title", model.title.toString()))
        clothesList.add(MultipartBody.Part.createFormData("text", model.text.toString()))
        clothesList.add(MultipartBody.Part.createFormData("style", model.style.toString()))
        clothesList.add(MultipartBody.Part.createFormData("author", model.author.toString()))
        clothesList.add(
            MultipartBody.Part.createFormData(
                "total_price",
                model.total_price.toString()
            )
        )

        saveOutfitConstructorUseCase.initParam(token, clothesList)
        saveOutfitConstructorUseCase.execute(object : DisposableSingleObserver<OutfitModel>() {
            override fun onSuccess(t: OutfitModel) {
                view.processViewAction {
                    view.processSuccessSaving(outfitModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    view.processSuccessSaving(outfitModel = null)
                    hideProgress()
                }
            }
        })
    }

    override fun updateCollection(
        token: String,
        id: Int,
        model: CollectionPostCreateModel,
        data: File
    ) {
        view.displayProgress()
        val requestFile = data.asRequestBody(("image/*").toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("cover_photo", data.name, requestFile)
        val clothesList = ArrayList<MultipartBody.Part>()
        model.clothes?.forEach {
            clothesList.add(MultipartBody.Part.createFormData("clothes", it.toString()))
        }

        clothesList.add(body)
        model.clothes_location?.forEachIndexed { index, clothesCollection ->
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]clothes_id",
                    clothesCollection.clothes_id.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_x",
                    clothesCollection.point_x.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_y",
                    clothesCollection.point_y.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]width",
                    clothesCollection.width.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]height",
                    clothesCollection.height.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]degree",
                    clothesCollection.degree.toString()
                )
            )
        }
        clothesList.add(MultipartBody.Part.createFormData("title", model.title.toString()))
        clothesList.add(MultipartBody.Part.createFormData("text", model.text.toString()))
        clothesList.add(MultipartBody.Part.createFormData("style", model.style.toString()))
        clothesList.add(MultipartBody.Part.createFormData("author", model.author.toString()))
        clothesList.add(
            MultipartBody.Part.createFormData(
                "total_price",
                model.total_price.toString()
            )
        )

        updateCollectionUseCase.initParam(token, id, clothesList)
        updateCollectionUseCase.execute(object : DisposableSingleObserver<Unit>() {
            override fun onSuccess(t: Unit) {
                view.processViewAction {
                    view.processSuccessSaving(null)
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
                    view.processSuccessSaving(outfitModel = null)
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