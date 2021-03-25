package kz.eztech.stylyts.profile.presentation.contracts

import kz.eztech.stylyts.collection.domain.models.CollectionFilterModel
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
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

        fun processFilter(filterList: List<CollectionFilterModel>)

        fun processMyPublications(searchModel: SearchModel<PublicationModel>)
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