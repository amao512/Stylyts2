package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 31.01.2021.
 */
interface CollectionDetailContract {

    interface View: BaseView {

        fun processOutfit(outfitModel: OutfitModel)

        fun processPost(postModel: PostModel)

        fun processSuccessDeleting()

        fun processLike(isLiked: Boolean)
    }

    interface Presenter: BasePresenter<View> {

        fun getOutfitById(
            token: String,
            outfitId: Int
        )

        fun getPostById(
            token: String,
            postId: Int
        )

        fun deleteOutfit(
            token: String,
            outfitId: Int
        )

        fun deletePost(
            token: String,
            postId: Int
        )

        fun onLikeClick(
            token: String,
            postId: Int
        )
    }
}