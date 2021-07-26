package kz.eztech.stylyts.presentation.contracts.incomes

import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface IncomeDetailContract {

    interface View: BaseView {

        fun processReferral(referralModel: ReferralModel)

        fun processOrder(orderModel: OrderModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getReferral(referralId: Int)

        fun getOrder(orderId: Int)
    }
}