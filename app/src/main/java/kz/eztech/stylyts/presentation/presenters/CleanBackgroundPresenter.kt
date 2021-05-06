package kz.eztech.stylyts.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.wardrobe.ClothesCreateModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesByIdUseCase
import kz.eztech.stylyts.domain.usecases.wardrobe.CreateClothesByImageUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection_constructor.CleanBackgroundContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 24.02.2021.
 */
class CleanBackgroundPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesByIdUseCase: GetClothesByIdUseCase,
    private val createClothesByImageUseCase: CreateClothesByImageUseCase
) : CleanBackgroundContract.Presenter {

    private lateinit var view: CleanBackgroundContract.View

    override fun disposeRequests() {
        getClothesByIdUseCase.clear()
        createClothesByImageUseCase.clear()
    }

    override fun attach(view: CleanBackgroundContract.View) {
        this.view = view
    }

    override fun saveClothes(
        token: String,
        clothesCreateModel: ClothesCreateModel
    ) {
        view.displayProgress()

        createClothesByImageUseCase.initParams(token, clothesCreateModel)
        createClothesByImageUseCase.execute(object : DisposableSingleObserver<ClothesModel>() {
            override fun onSuccess(t: ClothesModel) {
                view.hideProgress()
                getClothesById(token, t.id)
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun getClothesById(token: String, clothesId: Int) {
        view.displayProgress()
        getClothesByIdUseCase.initParams(token, clothesId)
        getClothesByIdUseCase.execute(object : DisposableSingleObserver<ClothesModel>(){
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
}