package kz.eztech.stylyts.profile.presentation.contracts

import kz.eztech.stylyts.collection_constructor.domain.models.PublicationsModel
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.search.domain.models.SearchModel

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ProfileContract {

    interface View : BaseView {

        fun showMyData()

        fun hideMyData()

        fun processMyData()

        fun processProfile(userModel: UserModel)

        fun processMyPublications(searchModel: SearchModel<PublicationsModel>)
    }

    interface Presenter : BasePresenter<View> {
        fun getProfile(token: String)

        fun getProfileById(token: String, userId: String)

        fun getMyPublications(token: String)
    }
}