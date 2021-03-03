package kz.eztech.stylyts.presentation.contracts.main.profile

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 27.02.2021.
 */
interface AddressProfileContract {
	interface View: BaseView {
		fun displayContent()
		fun displayForm()
	}
	interface Presenter:BasePresenter<View>{}
}