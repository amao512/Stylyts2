package kz.eztech.stylyts.presentation.presenters.clothes

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.cart.CartDataSource
import kz.eztech.stylyts.data.db.cart.CartMapper
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesByBarcode
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesByIdUseCase
import kz.eztech.stylyts.domain.usecases.clothes.SaveClothesToWardrobeUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.clothes.ClothesDetailContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class ClothesDetailPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesByIdUseCase: GetClothesByIdUseCase,
    private val getClothesByBarcode: GetClothesByBarcode,
    private val saveClothesToWardrobeUseCase: SaveClothesToWardrobeUseCase,
    private val cartDataSource: CartDataSource
) : ClothesDetailContract.Presenter {

    private lateinit var view: ClothesDetailContract.View

    private val disposables = CompositeDisposable()

    override fun disposeRequests() {
        getClothesByIdUseCase.clear()
        getClothesByBarcode.clear()
        saveClothesToWardrobeUseCase.clear()
    }

    override fun attach(view: ClothesDetailContract.View) {
        this.view = view
    }

    override fun getClothesById(
        token: String,
        clothesId: Int
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

    override fun getClothesByBarcode(token: String, barcode: String) {
        view.displayProgress()

        getClothesByBarcode.initParams(token, barcode)
        getClothesByBarcode.execute(object : DisposableSingleObserver<ClothesModel>() {
            override fun onSuccess(t: ClothesModel) {
                view.processViewAction {
                    hideProgress()
                    processClothes(clothesModel = t)
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
                view.processSuccessSavedWardrobe()
            }

            override fun onError(e: Throwable) {
                view.processSuccessSavedWardrobe()
            }
        })
    }

    override fun insertToCart(clothesModel: ClothesModel) {
        disposables.clear()
        disposables.add(
            cartDataSource.insert(CartMapper.mapToEntity(clothesModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.processInsertingCart()
                }
        )
    }
}