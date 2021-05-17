package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
interface CollectionItemContract {

    interface View : BaseView {

        fun processOutfits(resultsModel: ResultsModel<OutfitModel>)

        fun processPostResults(resultsModel: ResultsModel<PostModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun getOutfits(
            token: String,
            filterModel: FilterModel
        )

        fun getPosts(
            token: String,
            filterModel: FilterModel
        )
    }
}