package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface ShopProfileContract {

    interface View: BaseView {

        fun processProfile(userModel: UserModel)

        fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>)
    }

    interface Presenter: BasePresenter<View> {

        fun getProfile(
            token: String,
            id: Int
        )

        fun getTypes(token: String)
    }
}