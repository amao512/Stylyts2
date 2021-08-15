package kz.eztech.stylyts.global.presentation.common.contracts

import kz.eztech.stylyts.global.domain.models.common.ErrorModel
import kz.eztech.stylyts.auth.domain.models.TokenModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter

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