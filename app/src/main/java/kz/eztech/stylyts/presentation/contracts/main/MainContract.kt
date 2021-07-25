package kz.eztech.stylyts.presentation.contracts.main

import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
interface MainContract {

    interface View : BaseView {

        fun renderPaginatorState(state: Paginator.State)

        fun processPostResults(list: List<Any?>)

        fun processSuccessDeleting()

        fun processLike(
            isLiked: Boolean,
            postId: Int
        )

        fun navigateToUserProfile(userModel: UserModel)
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun getPosts()

        fun loadMorePost()

        fun deletePost(postId: Int)

        fun likePost(postId: Int)

        fun getUserForNavigate(userId: Int)
    }
}