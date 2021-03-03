package kz.eztech.stylyts.presentation.contracts.main.profile

import kz.eztech.stylyts.domain.models.UserSearchModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
interface UserSearchContract {
	interface View:BaseView{
		fun processUser(list:List<UserSearchModel>)
	}
	interface Presenter:BasePresenter<View>{
		fun getUserByUsername(token:String,username:String)
	}
}