package kz.eztech.stylyts.presentation.contracts.ordering

import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface PickupPointsContract {

    interface View : BaseView {

        fun processPoints(resultsModel: ResultsModel<AddressModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun getPickupPoints(
            token: String,
            owner: Int
        )
    }
}