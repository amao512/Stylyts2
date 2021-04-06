package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.shop.ShopCategoryModel
import kz.eztech.stylyts.domain.usecases.collection.GetClothesTypesUseCase
import kz.eztech.stylyts.domain.usecases.collection_constructor.GetCategoryUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */

class ShopCategoryPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getCategoryUseCase: GetCategoryUseCase
) : ShopItemContract.Presenter {

    private lateinit var view: ShopItemContract.View

    override fun disposeRequests() {
        getCategoryUseCase.clear()
    }

    override fun attach(view: ShopItemContract.View) {
        this.view = view
    }

    override fun getClothesTypes(token: String) {
        view.displayProgress()

        getClothesTypesUseCase.initParams(token)
        getClothesTypesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                view.processViewAction {
                    processClothesTypes(resultsModel = t)
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

    override fun getCategory() {
        view.displayProgress()
        getCategoryUseCase.execute(object : DisposableSingleObserver<ShopCategoryModel>(){
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
}