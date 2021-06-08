package kz.eztech.stylyts.presentation.presenters.ordering

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.address.GetAddressUseCase
import kz.eztech.stylyts.domain.usecases.profile.GetUserByIdUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.ordering.PickupPointsContract
import javax.inject.Inject

class PickupPointsPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getAddressUseCase: GetAddressUseCase
) : PickupPointsContract.Presenter {

    private lateinit var view: PickupPointsContract.View

    override fun disposeRequests() {
        getUserByIdUseCase.clear()
        getAddressUseCase.clear()
    }

    override fun attach(view: PickupPointsContract.View) {
        this.view = view
    }

    override fun getShop(
        token: String,
        id: Int
    ) {
        getUserByIdUseCase.initParams(token, id)
        getUserByIdUseCase.execute(object : DisposableSingleObserver<UserModel>() {
            override fun onSuccess(t: UserModel) {
                view.processShop(userModel = t)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun getPickupPoints(token: String, owner: Int) {
        view.displayProgress()

        getAddressUseCase.initParams(
            token = token,
            isMy = false,
            owner = owner
        )
        getAddressUseCase.execute(object : DisposableSingleObserver<ResultsModel<AddressModel>>() {
            override fun onSuccess(t: ResultsModel<AddressModel>) {
                view.processViewAction {
                    processPoints(resultsModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }
}