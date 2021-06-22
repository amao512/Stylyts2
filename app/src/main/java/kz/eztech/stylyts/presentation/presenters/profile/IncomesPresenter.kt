package kz.eztech.stylyts.presentation.presenters.profile

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.PageFilterModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.usecases.order.GetOrderListUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.profile.ProfileIncomeContract
import javax.inject.Inject

class IncomesPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getOrderListUseCase: GetOrderListUseCase
) : ProfileIncomeContract.Presenter {

    private lateinit var view: ProfileIncomeContract.View

    override fun disposeRequests() {
        getOrderListUseCase.clear()
    }

    override fun attach(view: ProfileIncomeContract.View) {
        this.view = view
    }

    override fun getIncomes(
        token: String,
        pageFilterModel: PageFilterModel
    ) {
        view.displayProgress()

        getOrderListUseCase.initParams(token, pageFilterModel)
        getOrderListUseCase.execute(object : DisposableSingleObserver<ResultsModel<OrderModel>>() {
            override fun onSuccess(t: ResultsModel<OrderModel>) {
                view.processViewAction {
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