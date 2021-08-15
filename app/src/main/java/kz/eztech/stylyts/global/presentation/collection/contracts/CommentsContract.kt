package kz.eztech.stylyts.global.presentation.collection.contracts

import kz.eztech.stylyts.global.data.models.comments.CommentCreateModel
import kz.eztech.stylyts.global.domain.models.comments.CommentModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.base.BasePresenter
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.utils.Paginator

interface CommentsContract {

    interface View : BaseView {

        fun getPostId(): Int

        fun processProfile(userModel: UserModel)

        fun renderPaginatorState(state: Paginator.State)

        fun processComments(list: List<Any?>)

        fun processCreatingComment(commentModel: CommentModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getProfile()

        fun getComments()

        fun loadMoreComments()

        fun loadPage(page: Int)

        fun createComment(commentCreateModel: CommentCreateModel)
    }
}