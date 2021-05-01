package kz.eztech.stylyts.presentation.fragments.collection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.comments.CommentModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CommentsAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.CommentSpaceItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection.CommentsContract
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CommentsPresenter
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class CommentsFragment : BaseFragment<MainActivity>(), CommentsContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject lateinit var presenter: CommentsPresenter
    @Inject lateinit var imageLoader: DomainImageLoader
    private lateinit var adapter: CommentsAdapter

    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var userAvatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var commentEditText: EditText
    private lateinit var sendTextView: TextView

    companion object {
        const val COLLECTION_ID_KEY = "postId"
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
            toolbar_left_corner_action_image_button.setOnClickListener(this@CommentsFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.toolbar_title_comments)
            toolbar_title_text_view.show()

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
        adapter = CommentsAdapter(imageLoader = imageLoader)
        adapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        commentsRecyclerView = fragment_comments_recycler_view
        commentsRecyclerView.adapter = adapter
        commentsRecyclerView.addItemDecoration(CommentSpaceItemDecoration(space = 8))

        userAvatarShapeableImageView = fragment_comments_avatar_shapeable_image_view
        userShortNameTextView = fragment_comments_user_short_name_text_view

        commentEditText = fragment_comments_edit_text
        sendTextView = fragment_comments_send_text_view
        sendTextView.isClickable = false
        sendTextView.isFocusable = false
        handleCommentEditText()
    }

    override fun initializeListeners() {
        sendTextView.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getComments(
            token = currentActivity.getTokenFromSharedPref(),
            postId = getPostIdFromArgs()
        )
        presenter.getProfile(token = currentActivity.getTokenFromSharedPref())
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
        when (view.id) {
            R.id.item_comment_avatar_shapeable_image_view -> navigateToProfile(item)
            R.id.item_comment_user_short_name_text_view -> navigateToProfile(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
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
            imageLoader.load(
                url = userModel.avatar,
                target = userAvatarShapeableImageView
            )
            userShortNameTextView.hide()
        }
    }

    override fun processComments(results: ResultsModel<CommentModel>) {
        adapter.updateList(list = results.results)
    }

    override fun processCreatingComment(commentModel: CommentModel) {
        adapter.addItem(commentModel)
        commentEditText.text.clear()
    }

    private fun navigateToProfile(item: Any?) {
        item as CommentModel

        val bundle = Bundle()
        bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.author.id)

        findNavController().navigate(R.id.nav_profile, bundle)
    }

    private fun handleCommentEditText() {
        commentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    sendTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_blue))
                    sendTextView.isClickable = true
                    sendTextView.isFocusable = true
                }
            }
        })
    }

    private fun createComment() {
        if (commentEditText.text.isNotBlank()) {
            presenter.createComment(
                token = currentActivity.getTokenFromSharedPref(),
                text = commentEditText.text.toString(),
                postId = getPostIdFromArgs()
            )
        }
    }

    private fun getPostIdFromArgs(): Int = arguments?.getInt(COLLECTION_ID_KEY) ?: 0
}