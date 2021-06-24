package kz.eztech.stylyts.presentation.fragments.collection

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_comments.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.api.models.comments.CommentCreateModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection.CommentsAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.CommentSpaceItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection.CommentsContract
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.fragments.shop.ShopProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CommentsPresenter
import kz.eztech.stylyts.presentation.utils.AbstractTextWatcher
import kz.eztech.stylyts.presentation.utils.Paginator
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class CommentsFragment : BaseFragment<MainActivity>(), CommentsContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject lateinit var presenter: CommentsPresenter
    private lateinit var adapter: CommentsAdapter

    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var userAvatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var commentEditText: EditText
    private lateinit var sendTextView: TextView

    companion object {
        const val COLLECTION_ID_KEY = "collection_id"
        const val MODE_KEY = "mode_key"
        const val OUTFIT_MODE = 0
        const val POST_MODE = 1
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_comments

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_comments_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.toolbar_title_comments))

            background = ContextCompat.getDrawable(requireContext(), R.color.toolbar_bg_gray)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        adapter = CommentsAdapter()
        adapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        commentsRecyclerView = fragment_comments_recycler_view
        commentsRecyclerView.adapter = adapter
        commentsRecyclerView.addItemDecoration(CommentSpaceItemDecoration(space = 8))

        userAvatarShapeableImageView = fragment_comments_avatar_shapeable_image_view
        userShortNameTextView = fragment_comments_user_short_name_text_view
        commentEditText = fragment_comments_edit_text

        sendTextView = fragment_comments_send_text_view.apply {
            isClickable = false
            isFocusable = false
        }

        handleCommentEditText()
    }

    override fun initializeListeners() {
        sendTextView.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        displayProgress()
        handleRecyclerView()

        presenter.getProfile()

        if (getModeFromArgs() == POST_MODE) {
            presenter.getComments()
        }
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_comments_progress_bar.show()
    }

    override fun hideProgress() {
        fragment_comments_progress_bar.hide()
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is CommentModel -> navigateToProfile(item.author.id, item.author.isBrand)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_comments_send_text_view -> createComment()
        }
    }

    override fun processProfile(userModel: UserModel) {
        if (userModel.avatar.isBlank()) {
            userAvatarShapeableImageView.hide()
            userShortNameTextView.text = getShortName(
                firstName = userModel.firstName,
                lastName = userModel.lastName
            )
        } else {
            userModel.avatar.loadImageWithCenterCrop(target = userAvatarShapeableImageView)
            userShortNameTextView.hide()
        }
    }

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processComments(state.data)
            is Paginator.State.NewPageProgress<*> -> processComments(state.data)
            else -> {}
        }

        hideProgress()
    }

    override fun processComments(list: List<Any?>) {
        list.map { it!! }.let {
            adapter.updateList(list = it)
        }
    }

    override fun processCreatingComment(commentModel: CommentModel) {
        adapter.addItem(commentModel)
        commentEditText.text.clear()
    }

    override fun getToken(): String = currentActivity.getTokenFromSharedPref()

    override fun getPostId(): Int = arguments?.getInt(COLLECTION_ID_KEY) ?: 0

    private fun handleRecyclerView() {
        commentsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!commentsRecyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMoreComments()
                }
            }
        })
    }

    private fun navigateToProfile(
        userId: Int,
        isBrand: Boolean
    ) {
        val bundle = Bundle()

        if (isBrand) {
            bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, userId)
            findNavController().navigate(R.id.nav_shop_profile, bundle)
        } else {
            bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, userId)
            findNavController().navigate(R.id.nav_profile, bundle)
        }
    }

    private fun handleCommentEditText() {
        commentEditText.addTextChangedListener(object : AbstractTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    sendTextView.apply {
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.app_blue))
                        isClickable = true
                        isFocusable = true
                    }
                }
            }
        })
    }

    private fun createComment() {
        if (commentEditText.text.isNotBlank()) {
            presenter.createComment(
                CommentCreateModel(
                    text = commentEditText.text.toString(),
                    postId = getPostId()
                )
            )
        }
    }

    private fun getModeFromArgs(): Int = arguments?.getInt(MODE_KEY) ?: OUTFIT_MODE
}