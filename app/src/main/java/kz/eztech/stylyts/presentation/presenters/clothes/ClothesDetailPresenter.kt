package kz.eztech.stylyts.presentation.presenters.clothes

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.cart.CartDataSource
import kz.eztech.stylyts.data.db.cart.CartMapper
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesByIdUseCase
import kz.eztech.stylyts.domain.usecases.clothes.SaveClothesToWardrobeUseCase
import kz.eztech.stylyts.domain.usecases.collection.GetItemByBarcodeUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.clothes.ClothesDetailContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class ClothesDetailPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesByIdUseCase: GetClothesByIdUseCase,
    private val getItemByBarcodeUseCase: GetItemByBarcodeUseCase,
    private val saveClothesToWardrobeUseCase: SaveClothesToWardrobeUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val cartDataSource: CartDataSource
) : ClothesDetailContract.Presenter {

    private lateinit var view: ClothesDetailContract.View

    private val disposables = CompositeDisposable()

    override fun disposeRequests() {
        getClothesByIdUseCase.clear()
        getItemByBarcodeUseCase.clear()
        saveClothesToWardrobeUseCase.clear()
        getUserByIdUseCase.clear()
    }

    override fun attach(view: ClothesDetailContract.View) {
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

    override fun getClothesOwner(
        token: String,
        userId: Int
    ) {
        getUserByIdUseCase.initParams(token, userId)
        getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processClothesOwner(userModel = t)
            }

            override fun onError(e: Throwable) {
               view.displayMessage(msg = errorHelper.processError(e))
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