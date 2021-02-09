package kz.eztech.stylyts.presentation.presenters.main.constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.BrandsModel
import kz.eztech.stylyts.domain.usecases.main.GetFilteredItemsUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.GetBrandsUseCase
import kz.eztech.stylyts.domain.usecases.main.shop.GetCategoryUseCase
import kz.eztech.stylyts.presentation.contracts.main.collections.PhotoChooserContract
import kz.eztech.stylyts.presentation.contracts.main.constructor.CollectionConstructorFilterContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterPresenter:CollectionConstructorFilterContract.Presenter {
    private var errorHelper: ErrorHelper
    private var getCategoryUseCase: GetCategoryUseCase
    private var getFilteredItemsUseCase: GetFilteredItemsUseCase
    private var getBrandsUseCase: GetBrandsUseCase
    private lateinit var view: CollectionConstructorFilterContract.View
    private lateinit var token:String
    @Inject
    constructor(errorHelper: ErrorHelper,
                getCategoryUseCase: GetCategoryUseCase,
                getFilteredItemsUseCase: GetFilteredItemsUseCase,
                getBrandsUseCase: GetBrandsUseCase
    ){
        this.getFilteredItemsUseCase = getFilteredItemsUseCase
        this.getCategoryUseCase = getCategoryUseCase
        this.errorHelper = errorHelper
        this.getBrandsUseCase = getBrandsUseCase
    }

    fun initToken(token:String){
        this.token = token
    }

    override fun getCategories() {

    }

    override fun getBrands() {
        view.displayProgress()
        getBrandsUseCase.initParam(token)
        getBrandsUseCase.execute(object : DisposableSingleObserver<BrandsModel>(){
            override fun onSuccess(t: BrandsModel) {
                view.hideProgress()
            }

            override fun onError(e: Throwable) {
                view.hideProgress()
                view.displayMessage(errorHelper.processError(e))
            }
        })
    }

    override fun getColors() {

    }

    override fun getPriceRange(range: Pair<Int, Int>) {

    }

    override fun getDiscount() {

    }

    override fun disposeRequests() {
        getFilteredItemsUseCase.clear()
        getCategoryUseCase.clear()
    }

    override fun attach(view: CollectionConstructorFilterContract.View) {
        this.view = view
    }
}