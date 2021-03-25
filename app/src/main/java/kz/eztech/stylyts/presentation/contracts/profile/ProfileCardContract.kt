package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
interface ProfileCardContract {
	interface View: BaseView
	interface Presenter: BasePresenter<View>
}