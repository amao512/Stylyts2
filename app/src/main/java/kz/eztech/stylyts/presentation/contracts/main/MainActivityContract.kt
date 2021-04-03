package kz.eztech.stylyts.presentation.contracts.main

import kz.eztech.stylyts.domain.models.ErrorModel
import kz.eztech.stylyts.domain.models.auth.TokenModel
import kz.eztech.stylyts.presentation.base.BasePresenter

interface MainActivityContract {

    interface View  {

        fun processVerification(errorModel: ErrorModel)

        fun processRefresh(tokenModel: TokenModel)
    }

    interface Presenter: BasePresenter<View> {

        fun verifyToken(token: String)

        fun refreshToken(refreshToken: String)
    }
}