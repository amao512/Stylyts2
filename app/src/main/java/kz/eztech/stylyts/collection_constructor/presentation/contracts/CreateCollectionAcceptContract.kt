package kz.eztech.stylyts.collection_constructor.presentation.contracts

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
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