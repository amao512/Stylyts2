package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.outfits.GetOutfitByIdUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection.CollectionDetailContract
import javax.inject.Inject

class CollectionDetailPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getOutfitByIdUseCase: GetOutfitByIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : CollectionDetailContract.Presenter {

    private lateinit var view: CollectionDetailContract.View

    override fun disposeRequests() {
        view.disposeRequests()
        getOutfitByIdUseCase.clear()
    }

    override fun attach(view: CollectionDetailContract.View) {
        this.view = view
    }

    override fun getOutfitById(token: String, outfitId: String) {
        view.displayProgress()

        getOutfitByIdUseCase.initParams(token, outfitId)
        getOutfitByIdUseCase.execute(object : DisposableSingleObserver<OutfitModel>() {
            override fun onSuccess(t: OutfitModel) {
                view.processViewAction {
                    processOutfit(outfitModel = t)
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

    override fun getOutfitOwner(token: String, userId: String) {
        getUserByIdUseCase.initParams(token, userId)
        getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processOutfitOwner(userModel = t)
            }

            override fun onError(e: Throwable) {
               view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }
}