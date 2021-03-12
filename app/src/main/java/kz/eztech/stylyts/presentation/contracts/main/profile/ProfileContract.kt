package kz.eztech.stylyts.presentation.contracts.main.profile

import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ProfileContract {

    interface View : BaseView {

        fun showSettings()

        fun hideSettings()

        fun processSettings()

        fun processProfile(profileModel: ProfileModel)

        fun processMyCollections(models: MainLentaModel)
    }

    interface Presenter : BasePresenter<View> {
        fun getProfile(token: String)

        fun getMyCollections(token: String, map: Map<String, Any>)
    }
}