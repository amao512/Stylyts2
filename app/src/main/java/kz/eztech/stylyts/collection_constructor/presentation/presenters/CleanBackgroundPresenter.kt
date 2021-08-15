package kz.eztech.stylyts.collection_constructor.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCreateModel
import kz.eztech.stylyts.global.domain.usecases.clothes.GetClothesByIdUseCase
import kz.eztech.stylyts.collection_constructor.domain.usecases.CreateClothesByImageUseCase
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.collection_constructor.presentation.contracts.CleanBackgroundContract
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

    override fun saveClothes(clothesCreateModel: ClothesCreateModel) {
        view.displayProgress()

        createClothesByImageUseCase.initParams(clothesCreateModel)
        createClothesByImageUseCase.execute(object : DisposableSingleObserver<ClothesModel>() {
            override fun onSuccess(t: ClothesModel) {
                view.hideProgress()
                getClothesById(t.id)
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun getClothesById(clothesId: Int) {
        view.displayProgress()
        getClothesByIdUseCase.initParams(clothesId)
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