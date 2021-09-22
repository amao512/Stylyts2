package kz.eztech.stylyts.home.presentation.home.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_main_line.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.posts.PostModel
import kz.eztech.stylyts.global.domain.models.posts.TagModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.home.presentation.home.contracts.HomeContract
import kz.eztech.stylyts.home.presentation.home.presenters.HomePresenter
import kz.eztech.stylyts.home.presentation.home.ui.adapters.MainLineAdapter
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.global.presentation.base.DialogChooserListener
import kz.eztech.stylyts.global.presentation.collection.ui.dialogs.CollectionContextDialog
import kz.eztech.stylyts.global.presentation.clothes.ui.ClothesDetailFragment
import kz.eztech.stylyts.global.presentation.collection.ui.CollectionDetailFragment
import kz.eztech.stylyts.global.presentation.collection.ui.CommentsFragment
import kz.eztech.stylyts.collection_constructor.presentation.ui.fragments.CollectionConstructorFragment
import kz.eztech.stylyts.collection_constructor.presentation.ui.fragments.CreateCollectionAcceptFragment
import kz.eztech.stylyts.profile.presentation.profile.ui.ProfileFragment
import kz.eztech.stylyts.profile.presentation.profile.ui.ShopProfileFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.FileUtils
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class HomeFragment : BaseFragment<MainActivity>(), HomeContract.View, View.OnClickListener,
    UniversalViewClickListener, DialogChooserListener {

    @Inject
    lateinit var presenter: HomePresenter
    private lateinit var postsAdapter: MainLineAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: TwinklingRefreshLayout

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
        postsAdapter = MainLineAdapter()
        postsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recyclerView = recycler_view_fragment_main_images_list
        recyclerView.adapter = postsAdapter

        refreshLayout = fragment_main_swipe_refresh_layout
        refreshLayout.setHeaderView(ProgressLayout(requireContext()))
        refreshLayout.setBottomView(LoadingView(requireContext()))
    }

    override fun initializeListeners() {
        toolbar_right_corner_action_image_button.setOnClickListener(this)
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
        presenter.getPosts()
        handleRefreshLayout()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getContractView(): BaseView = this

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_right_corner_action_image_button -> {
                findNavController().navigate(R.id.action_mainFragment_to_chatsFragment)
            }
        }
    }

    override fun displayProgress() {
        refreshLayout.startRefresh()
    }

    override fun hideProgress() {
        refreshLayout.finishRefreshing()
    }

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> {
                processPostResults(state.data)
                refreshLayout.finishRefreshing()
            }
            is Paginator.State.NewPageProgress<*> -> {
                processPostResults(state.data)
                refreshLayout.finishLoadmore()
            }
            is Paginator.State.Empty -> {
                refreshLayout.finishRefreshing()
                refreshLayout.finishLoadmore()
            }
            else -> hideProgress()
        }
    }

    override fun processPostResults(list: List<Any?>) {
        list.map { it!! }.let {
            postsAdapter.updateList(it)
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
            R.id.dialog_bottom_collection_context_delete_text_view -> onPostDeleteContextClicked(
                item
            )
            R.id.dialog_bottom_collection_context_change_text_view -> onChangeCollectionClicked(item)
        }
    }

    override fun navigateToUserProfile(userModel: UserModel) = with(userModel) {
        val bundle = Bundle()

        if (isBrand && id != currentActivity.getUserIdFromSharedPref()) {
            bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, id)

            findNavController().navigate(R.id.action_mainFragment_to_nav_shop_profile, bundle)
        } else {
            bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, id)

            findNavController().navigate(R.id.action_mainFragment_to_nav_profile, bundle)
        }
    }

    private fun handleRefreshLayout() {
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)
                refreshLayout?.startRefresh()
                presenter.getPosts()
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)
                refreshLayout?.startLoadMore()
                presenter.loadMorePost()
            }
        })
    }

    private fun onProfileClicked(item: Any?) {
        item as PostModel

        with(item) {
            val bundle = Bundle()

            if (author.isBrand && author.id != currentActivity.getUserIdFromSharedPref()) {
                bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, author.id)

                findNavController().navigate(R.id.action_mainFragment_to_nav_shop_profile, bundle)
            } else {
                bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, author.id)

                findNavController().navigate(R.id.action_mainFragment_to_nav_profile, bundle)
            }
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

                if (tagModel.referralUser != currentActivity.getUserIdFromSharedPref()) {
                    bundle.putInt(ClothesDetailFragment.INFLUENCER_ID_KEY, tagModel.referralUser)
                }

                findNavController().navigate(
                    R.id.action_mainFragment_to_clothesDetailFragment,
                    bundle
                )
            }
            2 -> presenter.getUserForNavigate(userId = tagModel.id)
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

        if (item.referralUser != currentActivity.getUserIdFromSharedPref()) {
            bundle.putInt(ClothesDetailFragment.INFLUENCER_ID_KEY, item.referralUser)
        }

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
            setChoiceListener(listener = this@HomeFragment)
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun likePost(item: Any?) {
        item as PostModel

        presenter.likePost(postId = item.id)
    }

    private fun onPostDeleteContextClicked(item: Any?) {
        item as PostModel

        presenter.deletePost(postId = item.id)
    }
}