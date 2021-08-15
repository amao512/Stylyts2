package kz.eztech.stylyts.search.presentation.shop.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesCategoriesUseCase
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.search.presentation.shop.contracts.ShopItemListContract
import kz.eztech.stylyts.search.presentation.shop.data.UIShopCategoryData
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 08.04.2021.
 */
class ShopItemListPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesUseCase: GetClothesUseCase,
    private val getClothesCategoriesUseCase: GetClothesCategoriesUseCase,
    private val uiShopCategoryData: UIShopCategoryData
) : ShopItemListContract.Presenter {

    private lateinit var view: ShopItemListContract.View

    override fun disposeRequests() {
        getClothesUseCase.clear()
        getClothesCategoriesUseCase.clear()
    }

    override fun attach(view: ShopItemListContract.View) {
        this.view = view
    }

    override fun getCategoriesByType(clothesTypeModel: ClothesTypeModel) {
        view.displayProgress()

        getClothesCategoriesUseCase.initParams(typeId = clothesTypeModel.id)
        getClothesCategoriesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesCategoryModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesCategoryModel>) {
                view.processViewAction {
                    processCategories(
                        list = uiShopCategoryData.getCategoryFilterList(
                            categoryList = t.results,
                            clothesTypeModel = clothesTypeModel
                        )
                    )
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

    override fun getClothesResultsByType(filterModel: ClothesFilterModel) {
        getClothesUseCase.initParams(filterModel.page, filterModel)
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

    override fun getClothesResultsByCategory(filterModel: ClothesFilterModel) {
        getClothesUseCase.initParams(filterModel.page, filterModel)
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processClothesResults(resultsModel = t)
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }
}