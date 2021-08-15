package kz.eztech.stylyts.ordering.presentation.order_constructor.contracts

import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

interface PickupPointsContract {

    interface View : BaseView {

        fun getShopId(): Int

        fun processShop(userModel: UserModel)

        fun renderPaginatorState(state: Paginator.State)

        fun processPoints(list: List<Any?>)
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun getShop(id: Int)

        fun getPickupPoints()

        fun loadMorePage()
    }
}