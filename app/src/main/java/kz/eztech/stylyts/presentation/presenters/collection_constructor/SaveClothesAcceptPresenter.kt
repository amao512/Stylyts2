package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.*
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.usecases.clothes.*
import kz.eztech.stylyts.domain.usecases.wardrobe.CreateClothesByImageUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection_constructor.SaveClothesAcceptContract
import javax.inject.Inject

class SaveClothesAcceptPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getClothesCategoriesByTypeUseCase: GetClothesCategoriesByTypeUseCase,
    private val getClothesStylesUseCase: GetClothesStylesUseCase,
    private val getClothesBrandsUseCase: GetClothesBrandsUseCase,
    private val createClothesByImageUseCase: CreateClothesByImageUseCase
) : SaveClothesAcceptContract.Presenter {

    private lateinit var view: SaveClothesAcceptContract.View

    override fun disposeRequests() {
        getClothesTypesUseCase.clear()
        getClothesCategoriesByTypeUseCase.clear()
        getClothesStylesUseCase.clear()
        getClothesBrandsUseCase.clear()
        createClothesByImageUseCase.clear()
    }

    override fun attach(view: SaveClothesAcceptContract.View) {
        this.view = view
    }

    override fun getTypes(token: String) {
        view.displaySmallProgress()

        getClothesTypesUseCase.initParams(token)
        getClothesTypesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                view.processViewAction {
                    processTypes(resultsModel = t)
                    hideSmallProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideSmallProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getCategories(
        token: String,
        typeId: Int
    ) {
        view.displaySmallProgress()

        getClothesCategoriesByTypeUseCase.initParams(token, typeId)
        getClothesCategoriesByTypeUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesCategoryModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesCategoryModel>) {
                view.processViewAction {
                    processCategories(resultsModel = t)
                    hideSmallProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideSmallProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getStyles(token: String) {
        view.displaySmallProgress()

        getClothesStylesUseCase.initParams(token)
        getClothesStylesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesStyleModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesStyleModel>) {
                view.processViewAction {
                    processStyles(resultsModel = t)
                    hideSmallProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideSmallProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getBrands(token: String) {
        view.displaySmallProgress()

        getClothesBrandsUseCase.initParams(token)
        getClothesBrandsUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesBrandModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesBrandModel>) {
                view.processViewAction {
                    processBrands(resultsModel = t)
                    hideSmallProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideSmallProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun createClothes(
        token: String,
        clothesCreateModel: ClothesCreateModel
    ) {
        view.displayProgress()

        createClothesByImageUseCase.initParams(token, clothesCreateModel)
        createClothesByImageUseCase.execute(object : DisposableSingleObserver<ClothesModel>() {
            override fun onSuccess(t: ClothesModel) {
                view.processSuccessCreating(wardrobeModel = t)
                view.hideProgress()
            }

            override fun onError(e: Throwable) {
                view.hideProgress()
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }
}