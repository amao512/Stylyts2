package kz.eztech.stylyts.presentation.contracts.main.shop

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface PartnerProfileContract {
    interface View: BaseView {}
    interface Presenter: BasePresenter<View> {}
}