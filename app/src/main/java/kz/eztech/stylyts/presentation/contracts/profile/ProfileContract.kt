package kz.eztech.stylyts.presentation.contracts.profile

import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.domain.models.ResultsModel

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface ProfileContract {

    interface View : BaseView {

        fun showMyData()

        fun hideMyData()

        fun processMyData()

        fun processProfile(userModel: UserModel)

        fun processFilter(filterList: List<CollectionFilterModel>)

        fun processMyPublications(resultsModel: ResultsModel<PublicationModel>)
    }

    interface Presenter : BasePresenter<View> {
        fun getProfile(
            token: String,
            userId: String,
            isOwnProfile: Boolean
        )

        fun getFilerList(isOwnProfile: Boolean)

        fun getPublications(
            token: String,
            isOwnProfile: Boolean
        )
    }
}