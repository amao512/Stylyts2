package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesBrandsUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.shop.CategoryTypeDetailContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class CategoryTypeDetailFragmentPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesBrandsUseCase: GetClothesBrandsUseCase,
    private val getClothesUseCase: GetClothesUseCase
) : CategoryTypeDetailContract.Presenter {

    private lateinit var view: CategoryTypeDetailContract.View

    override fun disposeRequests() {
        getClothesBrandsUseCase.clear()
        getClothesUseCase.clear()
    }

    override fun attach(view: CategoryTypeDetailContract.View) {
        this.view = view
    }

    override fun getCategoryTypeDetail(
        token: String,
        gender: String,
        clothesCategoryId: Int
    ) {
        view.displayProgress()

        getClothesUseCase.initParams(
            token = token,
            gender = gender,
            clothesCategoryId = clothesCategoryId
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
                    displayMessage(msg = errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun getClothesByType(
        token: String,
        typeId: Int,
        gender: String
    ) {
        getClothesUseCase.initParams(
            token = token,
            gender = gender,
            clothesTypeId = typeId
        )

        val data = HashMap<String, Any>()
        data["type_id"] = typeId
        data["gender_type"] = gender

        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processViewAction {
                    processClothesResults(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getClothesBrands(token: String) {
        getClothesBrandsUseCase.initParams(token)
        getClothesBrandsUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesBrandModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesBrandModel>) {
                view.processViewAction {
                    processClothesBrands(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getClothesByBrand(
        token: String,
        gender: String,
        clothesCategoryId: Int,
        clothesBrandId: Int
    ) {
        getClothesUseCase.initParams(
            token = token,
            gender = gender,
            clothesCategoryId = clothesCategoryId,
            clothesBrandId = clothesBrandId
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processViewAction {
                    processClothesResults(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }
}