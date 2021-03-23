package kz.eztech.stylyts.profile.presentation.contracts

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
interface ProfileCardContract {
	interface View: BaseView
	interface Presenter: BasePresenter<View>
}