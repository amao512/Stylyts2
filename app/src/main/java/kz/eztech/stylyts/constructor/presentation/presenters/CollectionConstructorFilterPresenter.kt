package kz.eztech.stylyts.constructor.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.common.domain.models.BrandsModel
import kz.eztech.stylyts.constructor.domain.models.ShopCategoryModel
import kz.eztech.stylyts.constructor.domain.usecases.GetFilteredItemsUseCase
import kz.eztech.stylyts.constructor.domain.usecases.GetBrandsUseCase
import kz.eztech.stylyts.constructor.domain.usecases.GetCategoryUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.common.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.constructor.presentation.contracts.CollectionConstructorFilterContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var getCategoryUseCase: GetCategoryUseCase,
    private var getFilteredItemsUseCase: GetFilteredItemsUseCase,
    private var getBrandsUseCase: GetBrandsUseCase
) : CollectionConstructorFilterContract.Presenter {

    private lateinit var view: CollectionConstructorFilterContract.View
    private lateinit var token: String

    override fun getCategories() {
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

    override fun getBrands() {
        view.displayProgress()

        getBrandsUseCase.initParam(token)
        getBrandsUseCase.execute(object : DisposableSingleObserver<BrandsModel>() {
            override fun onSuccess(t: BrandsModel) {
                view.processBrands(t)
                view.hideProgress()
            }

            override fun onError(e: Throwable) {
                view.hideProgress()
                view.displayMessage(errorHelper.processError(e))
            }
        })
    }

    override fun getColors() {}

    override fun getPriceRange(range: Pair<Int, Int>) {}

    override fun getDiscount() {}

    override fun disposeRequests() {
        getFilteredItemsUseCase.clear()
        getCategoryUseCase.clear()
    }

    override fun attach(view: CollectionConstructorFilterContract.View) {
        this.view = view
    }

    fun initToken(token: String?) {
        this.token = token ?: EMPTY_STRING
    }
}