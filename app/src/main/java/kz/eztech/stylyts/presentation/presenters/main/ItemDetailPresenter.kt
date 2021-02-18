package kz.eztech.stylyts.presentation.presenters.main

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.usecases.main.GetItemDetailUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.detail.ItemDetailContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class ItemDetailPresenter: ItemDetailContract.Presenter {

    private var errorHelper: ErrorHelper
    private var getItemDetailUseCase: GetItemDetailUseCase
    private lateinit var view: ItemDetailContract.View
    @Inject
    constructor(errorHelper: ErrorHelper,
                getItemDetailUseCase: GetItemDetailUseCase
    ){
        this.getItemDetailUseCase = getItemDetailUseCase
        this.errorHelper = errorHelper
    }

    override fun getItemDetail(token: String, id: Int) {
        view.displayProgress()
        getItemDetailUseCase.initParams(token,id)
        getItemDetailUseCase.execute(object : DisposableSingleObserver<ClothesMainModel>(){
            override fun onSuccess(t: ClothesMainModel) {
                view.processViewAction {
                    hideProgress()
                    processItemDetail(t)
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

    override fun disposeRequests() {
        getItemDetailUseCase.clear()
    }

    override fun attach(view: ItemDetailContract.View) {
        this.view = view
    }
    
    override fun getItemByBarcode(token: String, value: String) {
    
    }
}