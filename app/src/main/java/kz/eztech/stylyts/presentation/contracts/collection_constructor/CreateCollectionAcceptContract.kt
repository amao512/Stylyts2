package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.domain.models.PublicationModel
import java.io.File

interface CreateCollectionAcceptContract {

    interface View : BaseView {

        fun processPublications(publicationModel: PublicationModel)
    }

    interface Presenter : BasePresenter<View> {

        fun createPublications(
            token: String,
            description: String,
            hidden: Boolean,
            tags: String,
            file: File
        )
    }
}