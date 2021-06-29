package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */

class ShopCategoryPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesTypesUseCase: GetClothesTypesUseCase
) : ShopItemContract.Presenter {

    private lateinit var view: ShopItemContract.View

    override fun disposeRequests() {
        getClothesTypesUseCase.clear()
    }

    override fun attach(view: ShopItemContract.View) {
        this.view = view
    }

    override fun getClothesTypes() {
        view.displayProgress()

        getClothesTypesUseCase.initParams(token = view.getToken())
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
}