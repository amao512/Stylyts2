package kz.eztech.stylyts.collections.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesStylesUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.collections.presentation.contracts.CollectionsContract
import javax.inject.Inject

class CollectionsPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesStylesUseCase: GetClothesStylesUseCase
): CollectionsContract.Presenter {

    private lateinit var view: CollectionsContract.View

    override fun disposeRequests() {
        getClothesStylesUseCase.clear()
    }

    override fun attach(view: CollectionsContract.View) {
        this.view = view
    }

    override fun getClothesStyles() {
        getClothesStylesUseCase.initParams()
        getClothesStylesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesStyleModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesStyleModel>) {
                view.processViewAction {
                    processClothesStylesResults(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }
}