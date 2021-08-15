package kz.eztech.stylyts.search.presentation.shop.presenters

import android.app.Application
import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.search.presentation.shop.contracts.ShopItemContract
import kz.eztech.stylyts.search.presentation.shop.data.UIShopItemData
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */

class ShopCategoryPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val uiShopItemData: UIShopItemData,
    private val application: Application
) : ShopItemContract.Presenter {

    private lateinit var view: ShopItemContract.View

    override fun disposeRequests() {
        getClothesTypesUseCase.clear()
    }

    override fun attach(view: ShopItemContract.View) {
        this.view = view
    }

    override fun getClothesTypes() {
        getClothesTypesUseCase.initParams()
        getClothesTypesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                view.processViewAction {
                    processClothesTypes(
                        clothesTypes = uiShopItemData.getClothesTypes(
                            context = application,
                            typesList = t.results
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
}