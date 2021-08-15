package kz.eztech.stylyts.global.presentation.card.contracts

import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
interface ProfileCardContract {
	interface View: BaseView
	interface Presenter: BasePresenter<View>
}