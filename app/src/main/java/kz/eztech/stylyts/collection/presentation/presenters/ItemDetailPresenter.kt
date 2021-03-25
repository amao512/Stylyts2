package kz.eztech.stylyts.collection.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.collection.domain.usecases.GetItemByBarcodeUseCase
import kz.eztech.stylyts.collection.domain.usecases.GetItemDetailUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.collection.presentation.contracts.ItemDetailContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class ItemDetailPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var getItemDetailUseCase: GetItemDetailUseCase,
    private var getItemByBarcodeUseCase: GetItemByBarcodeUseCase
) : ItemDetailContract.Presenter {

    private lateinit var view: ItemDetailContract.View

    override fun getItemDetail(
        token: String,
        id: Int
    ) {
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
        view.displayProgress()
        getItemByBarcodeUseCase.initParams(token,value)
        getItemByBarcodeUseCase.execute(object : DisposableSingleObserver<ClothesMainModel>(){
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
}