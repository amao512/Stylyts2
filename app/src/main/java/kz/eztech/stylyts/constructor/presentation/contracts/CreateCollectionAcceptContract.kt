package kz.eztech.stylyts.constructor.presentation.contracts

import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.constructor.domain.models.PostModel
import java.io.File

interface CreateCollectionAcceptContract {

    interface View : BaseView {

        fun processPost(postModel: PostModel)
    }

    interface Presenter : BasePresenter<View> {

        fun createPost(
            token: String,
            description: String,
            hidden: Boolean,
            tags: String,
            file: File
        )
    }
}