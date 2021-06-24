package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

interface CommentsContract {

    interface View : BaseView {

        fun getToken(): String

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