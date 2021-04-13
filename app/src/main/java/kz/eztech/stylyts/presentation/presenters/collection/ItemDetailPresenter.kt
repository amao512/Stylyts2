package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesByIdUseCase
import kz.eztech.stylyts.domain.usecases.clothes.SaveClothesToWardrobeUseCase
import kz.eztech.stylyts.domain.usecases.collection.GetItemByBarcodeUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection.ItemDetailContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class ItemDetailPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesByIdUseCase: GetClothesByIdUseCase,
    private val getItemByBarcodeUseCase: GetItemByBarcodeUseCase,
    private val saveClothesToWardrobeUseCase: SaveClothesToWardrobeUseCase
) : ItemDetailContract.Presenter {

    private lateinit var view: ItemDetailContract.View

    override fun disposeRequests() {
        getClothesByIdUseCase.clear()
        getItemByBarcodeUseCase.clear()
        saveClothesToWardrobeUseCase.clear()
    }

    override fun attach(view: ItemDetailContract.View) {
        this.view = view
    }

    override fun getClothesById(
        token: String,
        clothesId: String
    ) {
        view.displayProgress()

        getClothesByIdUseCase.initParams(token, clothesId)
        getClothesByIdUseCase.execute(object : DisposableSingleObserver<ClothesModel>() {
            override fun onSuccess(t: ClothesModel) {
                view.processViewAction {
                    hideProgress()
                    processClothes(t)
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

    override fun getItemByBarcode(token: String, value: String) {
        view.displayProgress()
        getItemByBarcodeUseCase.initParams(token, value)
        getItemByBarcodeUseCase.execute(object : DisposableSingleObserver<ClothesMainModel>() {
            override fun onSuccess(t: ClothesMainModel) {
                view.processViewAction {
                    hideProgress()
//                    processClothes(t)
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

    override fun saveClothesToWardrobe(
        token: String,
        clothesId: Int
    ) {
        saveClothesToWardrobeUseCase.initParams(token, clothesId.toString())
        saveClothesToWardrobeUseCase.execute(object : DisposableSingleObserver<Any>() {
            override fun onSuccess(t: Any) {
                view.processViewAction {
                    processSuccessSavedWardrobe()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    processSuccessSavedWardrobe()
                }
            }
        })
    }
}