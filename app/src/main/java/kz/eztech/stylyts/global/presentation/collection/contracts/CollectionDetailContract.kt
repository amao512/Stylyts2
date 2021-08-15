package kz.eztech.stylyts.global.presentation.collection.contracts

import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.global.domain.models.posts.PostModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView

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