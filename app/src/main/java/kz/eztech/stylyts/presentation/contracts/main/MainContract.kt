package kz.eztech.stylyts.presentation.contracts.main

import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostFilterModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
interface MainContract {

    interface View : BaseView {

        fun processPostResults(resultsModel: ResultsModel<PostModel>)

        fun processSuccessDeleting()

        fun processLike(
            isLiked: Boolean,
            postId: Int
        )

        fun navigateToUserProfile(userModel: UserModel)
    }

    interface Presenter : BasePresenter<View> {

        fun getPosts(
            token: String,
            filterModel: PostFilterModel
        )

        fun deletePost(
            token: String,
            postId: Int
        )

        fun likePost(
            token: String,
            postId: Int
        )

        fun getUserForNavigate(
            token: String,
            userId: Int
        )
    }
}