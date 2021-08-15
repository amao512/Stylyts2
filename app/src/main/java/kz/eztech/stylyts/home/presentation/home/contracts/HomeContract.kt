package kz.eztech.stylyts.home.presentation.home.contracts

import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
interface HomeContract {

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