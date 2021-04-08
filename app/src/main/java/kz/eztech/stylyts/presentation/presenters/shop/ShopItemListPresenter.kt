package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesCategoryUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemListContract
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 08.04.2021.
 */
class ShopItemListPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesCategoryUseCase: GetClothesCategoryUseCase
) : ShopItemListContract.Presenter {

    private lateinit var view: ShopItemListContract.View

    override fun disposeRequests() {
        getClothesCategoryUseCase.clear()
    }

    override fun attach(view: ShopItemListContract.View) {
        this.view = view
    }

    override fun getCategoriesByType(token: String, clothesTypeId: Int) {
        view.displayProgress()

        getClothesCategoryUseCase.initParams(token, clothesTypeId)
        getClothesCategoryUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesCategoryModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesCategoryModel>) {
                view.processViewAction {
                    processCategories(resultsModel = t)
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
}