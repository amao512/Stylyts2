package kz.eztech.stylyts.presentation.presenters.main.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.domain.usecases.main.shop.GetCategoryUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */

class ShopCategoryPresenter: ShopItemContract.Presenter {
    private var errorHelper: ErrorHelper
    private var getCategoryUseCase: GetCategoryUseCase
    private lateinit var view: ShopItemContract.View
    @Inject
    constructor(errorHelper: ErrorHelper,
                getCategoryUseCase: GetCategoryUseCase
    ){
        this.getCategoryUseCase = getCategoryUseCase
        this.errorHelper = errorHelper
    }
    override fun disposeRequests() {
        getCategoryUseCase.clear()
    }

    override fun attach(view: ShopItemContract.View) {
        this.view = view
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