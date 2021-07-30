package kz.eztech.stylyts.presentation.presenters.incomes

import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.usecases.order.GetOrderByIdUseCase
import kz.eztech.stylyts.domain.usecases.referrals.GetReferralUseCase
import kz.eztech.stylyts.presentation.contracts.incomes.IncomeDetailContract
import javax.inject.Inject

class IncomeDetailPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getReferralUseCase: GetReferralUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : IncomeDetailContract.Presenter {

    private lateinit var view: IncomeDetailContract.View

    override fun disposeRequests() {
        getReferralUseCase.clear()
        getOrderByIdUseCase.clear()
    }

    override fun attach(view: IncomeDetailContract.View) {
        this.view = view
    }

//    override fun getReferral(referralId: Int) {
//        view.displayProgress()
//
//        getReferralUseCase.initParams(referralId)
//        getReferralUseCase.execute(object : DisposableSingleObserver<ReferralModel>() {
//            override fun onSuccess(t: ReferralModel) {
//                view.processViewAction {
//                    processReferral(referralModel = t)
//                    hideProgress()
//                }
//            }
//
//            override fun onError(e: Throwable) {
//                view.processViewAction {
//                    hideProgress()
//                    displayMessage(msg = errorHelper.processError(e))
//                }
//            }
//        })
//    }
//
//    override fun getOrder(orderId: Int) {
//        getOrderByIdUseCase.initParams(orderId)
//        getOrderByIdUseCase.execute(object : DisposableSingleObserver<OrderModel>() {
//            override fun onSuccess(t: OrderModel) {
//                view.processOrder(orderModel = t)
//            }
//
//            override fun onError(e: Throwable) {
//                view.displayMessage(msg = errorHelper.processError(e))
//            }
//        })
//    }
}