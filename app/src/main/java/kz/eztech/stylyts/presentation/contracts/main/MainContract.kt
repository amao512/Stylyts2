package kz.eztech.stylyts.presentation.contracts.main

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
interface MainContract {

    interface View : BaseView {

        fun processPostResults(resultsModel: ResultsModel<PostModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun getPosts(token: String)
    }
}