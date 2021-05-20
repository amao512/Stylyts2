package kz.eztech.stylyts.presentation.fragments.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_main_line.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.posts.TagModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.main.MainLineAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.presentation.dialogs.collection.CollectionContextDialog
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection.CollectionDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection.CommentsFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CreateCollectionAcceptFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.fragments.shop.ShopProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.MainLinePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class MainFragment : BaseFragment<MainActivity>(), MainContract.View, View.OnClickListener,
    UniversalViewClickListener, DialogChooserListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var presenter: MainLinePresenter
    @Inject lateinit var imageLoader: DomainImageLoader
    private lateinit var postsAdapter: MainLineAdapter
    private lateinit var currentFilter: FilterModel

    private lateinit var recyclerView: RecyclerView

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun customizeActionBar() {
        toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_send_message)

        toolbar_title_text_view.text = getString(R.string.app_name)
        toolbar_title_text_view.show()
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        currentFilter = FilterModel()
        postsAdapter = MainLineAdapter(
            ownId = currentActivity.getUserIdFromSharedPref(),
            imageLoader = imageLoader
        )
        postsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recyclerView = recycler_view_fragment_main_images_list
        recyclerView.adapter = postsAdapter
    }

    override fun initializeListeners() {
        toolbar_right_corner_action_image_button.setOnClickListener(this)
        fragment_main_swipe_refresh_layout.setOnRefreshListener(this)
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.constraint_layout_fragment_item_main_image_profile_container -> onProfileClicked(
                item
            )
//            R.id.button_item_main_image_change_collection -> onChangeCollectionClicked(item)
            R.id.frame_layout_item_main_image_holder_container -> onClothesItemClicked(item)
            R.id.item_main_image_image_card_view -> onCollectionImageClicked(item)
            R.id.text_view_item_main_image_comments_count -> navigateToComments(item)
            R.id.item_main_image_dialog_menu_image_button -> onContextMenuClicked(item)
            R.id.item_main_image_like_image_button -> likePost(item)
            R.id.item_main_image_comments_image_button -> navigateToComments(item)
            R.id.item_main_line_first_comment_text_view -> onProfileClicked(item)
        }

        when (item) {
            is TagModel -> onTagClicked(item, position)
        }
    }

    override fun processPostInitialization() {
        getPosts()
        handleRecyclerView()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun getContractView(): BaseView = this

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_right_corner_action_image_button -> {
                findNavController().navigate(R.id.action_mainFragment_to_chatsFragment)
            }
        }
    }

    override fun displayProgress() {
        fragment_main_more_small_progress_bar.show()
        fragment_main_swipe_refresh_layout.isRefreshing = true
    }

    override fun hideProgress() {
        fragment_main_more_small_progress_bar.hide()
        fragment_main_swipe_refresh_layout.isRefreshing = false
    }

    override fun processPostResults(resultsModel: ResultsModel<PostModel>) {
        postsAdapter.updateMoreList(list = resultsModel.results)

        if (resultsModel.totalPages != currentFilter.page) {
            currentFilter.page++
        } else {
            currentFilter.isLastPage = true
        }
    }

    override fun processSuccessDeleting() {
        processPostInitialization()
    }

    override fun processLike(
        isLiked: Boolean,
        postId: Int
    ) {
        postsAdapter.setLikePost(isLiked, postId)
    }

    override fun onChoice(
        v: View?,
        item: Any?
    ) {
        when (v?.id) {
            R.id.dialog_bottom_collection_context_delete_text_view -> onPostDeleteContextClicked(item)
            R.id.dialog_bottom_collection_context_change_text_view -> onChangeCollectionClicked(item)
        }
    }

    override fun navigateToUserProfile(userModel: UserModel) {
        val bundle = Bundle()

        if (userModel.isBrand) {
            bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, userModel.id)

            findNavController().navigate(R.id.action_mainFragment_to_nav_shop_profile, bundle)
        } else {
            bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, userModel.id)

            findNavController().navigate(R.id.action_mainFragment_to_nav_profile, bundle)
        }
    }

    override fun onRefresh() {
        displayProgress()
        getPosts()
    }

    private fun getPosts() {
        presenter.getPosts(
            token = currentActivity.getTokenFromSharedPref(),
            page = currentFilter.page
        )
    }

    private fun handleRecyclerView() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!currentFilter.isLastPage) {
                        getPosts()
                    }
                }
            }
        })
    }

    private fun onProfileClicked(item: Any?) {
        item as PostModel

        val bundle = Bundle()

        if (item.author.isBrand) {
            bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, item.author.id)

            findNavController().navigate(R.id.action_mainFragment_to_nav_shop_profile, bundle)
        } else {
            bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.author.id)

            findNavController().navigate(R.id.action_mainFragment_to_nav_profile, bundle)
        }
    }

    private fun onTagClicked(
        tagModel: TagModel,
        position: Int
    ) {
        when (position) {
            1 -> {
                val bundle = Bundle()
                bundle.putInt(ClothesDetailFragment.CLOTHES_ID, tagModel.id)

                findNavController().navigate(R.id.action_mainFragment_to_clothesDetailFragment, bundle)
            }
            2 -> presenter.getUserForNavigate(
                token = currentActivity.getTokenFromSharedPref(),
                userId = tagModel.id
            )
        }
    }

    private fun onChangeCollectionClicked(item: Any?) {
        item as PostModel

        findNavController().navigate(
            R.id.action_mainFragment_to_createCollectionAcceptFragment,
            getChangeCollectionBundle(postModel = item)
        )
    }

    private fun getChangeCollectionBundle(postModel: PostModel): Bundle {
        val bundle = Bundle()
        val itemsList = ArrayList<ClothesModel>()
        val images = ArrayList<String>()

        itemsList.addAll(postModel.clothes)
        images.addAll(postModel.images)
        images.removeFirst()

        bundle.apply {
            putInt(
                CreateCollectionAcceptFragment.MODE_KEY,
                CreateCollectionAcceptFragment.POST_MODE
            )
            putInt(CreateCollectionAcceptFragment.ID_KEY, postModel.id)
            putBoolean(CreateCollectionAcceptFragment.IS_UPDATING_KEY, true)
            putStringArrayList(CreateCollectionAcceptFragment.CHOSEN_PHOTOS_KEY, images)
            putParcelableArrayList(CreateCollectionAcceptFragment.CLOTHES_KEY, itemsList)
            putParcelableArrayList(CollectionConstructorFragment.CLOTHES_ITEMS_KEY, itemsList)
            putInt(CollectionConstructorFragment.MAIN_ID_KEY, postModel.id)

            if (postModel.images.isNotEmpty()) {
                putParcelable(
                    CreateCollectionAcceptFragment.PHOTO_URI_KEY,
                    FileUtils.getUriFromUrl(postModel.images[0])
                )
            }
        }

        return bundle
    }

    private fun onClothesItemClicked(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(R.id.action_mainFragment_to_clothesDetailFragment, bundle)
    }

    private fun onCollectionImageClicked(item: Any?) {
        item as PostModel

        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, item.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.POST_MODE)

        findNavController().navigate(R.id.action_mainFragment_to_collectionDetailFragment, bundle)
    }

    private fun navigateToComments(item: Any?) {
        item as PostModel

        val bundle = Bundle()
        bundle.putInt(CommentsFragment.COLLECTION_ID_KEY, item.id)
        bundle.putInt(CommentsFragment.MODE_KEY, CommentsFragment.POST_MODE)

        findNavController().navigate(R.id.action_mainFragment_to_userCommentsFragment, bundle)
    }

    private fun onContextMenuClicked(item: Any?) {
        item as PostModel

        CollectionContextDialog(
            isOwn = item.author.id == currentActivity.getUserIdFromSharedPref(),
            item = item
        ).apply {
            setChoiceListener(listener = this@MainFragment)
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun likePost(item: Any?) {
        item as PostModel

        presenter.likePost(
            token = currentActivity.getTokenFromSharedPref(),
            postId = item.id
        )
    }

    private fun onPostDeleteContextClicked(item: Any?) {
        item as PostModel

        presenter.deletePost(
            token = currentActivity.getTokenFromSharedPref(),
            postId = item.id
        )
    }
}