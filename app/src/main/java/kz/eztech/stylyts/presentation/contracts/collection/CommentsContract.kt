package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

interface CommentsContract {

    interface View : BaseView {

        fun processPost(postModel: PostModel)

        fun processOutfit(outfitModel: OutfitModel)

        fun processProfile(userModel: UserModel)

        fun processComments(results: ResultsModel<CommentModel>)

        fun processCreatingComment(commentModel: CommentModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getCollection(
            token: String,
            id: Int,
            mode: Int
        )

        fun getPost(
            token: String,
            postId: Int
        )

        fun getOutfit(
            token: String,
            outfitId: Int
        )

        fun getProfile(token: String)

        fun getComments(
            token: String,
            postId: Int
        )

        fun createComment(
            token: String,
            text: String,
            postId: Int
        )
    }
}