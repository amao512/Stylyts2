package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.wardrobe.ClothesCreateModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesCategoriesByTypeUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesStylesUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.domain.usecases.wardrobe.CreateClothesByImageUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection_constructor.SaveClothesAcceptContract
import javax.inject.Inject

class SaveClothesAcceptPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getClothesCategoriesByTypeUseCase: GetClothesCategoriesByTypeUseCase,
    private val getClothesStylesUseCase: GetClothesStylesUseCase,
    private val createClothesByImageUseCase: CreateClothesByImageUseCase
) : SaveClothesAcceptContract.Presenter {

    private lateinit var view: SaveClothesAcceptContract.View

    override fun disposeRequests() {
        getClothesTypesUseCase.dispose()
    }

    override fun attach(view: SaveClothesAcceptContract.View) {
        this.view = view
    }

    override fun getTypes(token: String) {
        view.displayProgress()

        getClothesTypesUseCase.initParams(token)
        getClothesTypesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                view.processViewAction {
                    processTypes(resultsModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getCategories(
        token: String,
        typeId: Int
    ) {
        view.displayProgress()

        getClothesCategoriesByTypeUseCase.initParams(token, typeId)
        getClothesCategoriesByTypeUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesCategoryModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesCategoryModel>) {
                view.processViewAction {
                    processCategories(resultsModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getStyles(token: String) {
        view.displayProgress()

        getClothesStylesUseCase.initParams(token)
        getClothesStylesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesStyleModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesStyleModel>) {
                view.processViewAction {
                    processStyles(resultsModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun createClothes(
        token: String,
        clothesCreateModel: ClothesCreateModel
    ) {
        createClothesByImageUseCase.initParams(token, clothesCreateModel)
        createClothesByImageUseCase.execute(object : DisposableSingleObserver<ClothesModel>() {
            override fun onSuccess(t: ClothesModel) {
                view.processSuccessCreating(wardrobeModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }
}