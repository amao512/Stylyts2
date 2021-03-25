package kz.eztech.stylyts.common.presentation.contracts.main

import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
import kz.eztech.stylyts.common.domain.models.MainLentaModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.search.domain.models.SearchModel

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
interface MainContract{
    interface View : BaseView {

        fun processCollections(model: MainLentaModel)

        fun processMyPublications(searchModel: SearchModel<PublicationModel>)
    }
    interface Presenter: BasePresenter<View> {

        fun getCollections(token: String)

        fun getMyPublications(token: String)
    }
}