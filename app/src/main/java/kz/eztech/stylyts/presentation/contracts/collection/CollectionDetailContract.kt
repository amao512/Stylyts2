package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
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

        fun navigateToUserProfile(userModel: UserModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getOutfitById(outfitId: Int)

        fun getPostById(postId: Int)

        fun deleteOutfit(outfitId: Int)

        fun deletePost(postId: Int)

        fun onLikeClick(postId: Int)

        fun getUserForNavigate(userId: Int)
    }
}