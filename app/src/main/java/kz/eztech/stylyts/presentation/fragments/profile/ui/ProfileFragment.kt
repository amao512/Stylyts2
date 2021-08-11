package kz.eztech.stylyts.presentation.fragments.profile.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostFilterModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.clothes.ClothesDetailAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.collection.OutfitsAdapter
import kz.eztech.stylyts.presentation.adapters.common.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.EditorListener
import kz.eztech.stylyts.presentation.contracts.profile.ProfileContract
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.dialogs.profile.EditProfileDialog
import kz.eztech.stylyts.presentation.fragments.camera.CameraFragment
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection.CollectionDetailFragment
import kz.eztech.stylyts.presentation.fragments.users.UserSubsFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.profile.ProfilePresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class ProfileFragment : BaseFragment<MainActivity>(), ProfileContract.View, View.OnClickListener,
    UniversalViewClickListener, EditorListener {

    @Inject lateinit var presenter: ProfilePresenter

    private lateinit var gridAdapter: GridImageAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter
    private lateinit var wardrobeAdapter: ClothesDetailAdapter
    private lateinit var outfitsAdapter: OutfitsAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var postFilterModel: PostFilterModel
    private lateinit var outfitFilterModel: OutfitFilterModel
    private lateinit var clothesFilterModel: ClothesFilterModel

    private lateinit var avatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var publicationsCountTextView: TextView
    private lateinit var followersItemLinearLayout: LinearLayout
    private lateinit var followersCountTextView: TextView
    private lateinit var followingsItemLinearLayout: LinearLayout
    private lateinit var followingsCountTextView: TextView
    private lateinit var changeProfileTextView: TextView
    private lateinit var followTextView: TextView
    private lateinit var unFollowTextView: TextView
    private lateinit var filterRecyclerVew: RecyclerView
    private lateinit var collectionRecyclerView: RecyclerView
    private lateinit var refreshLayout: TwinklingRefreshLayout

    private var collectionMode: Int = POSTS_MODE
    private var currentUsername: String = EMPTY_STRING
    private var currentUserId: Int = 0
    private var currentGender: String = EMPTY_STRING

    companion object {
        const val POSTS_MODE = 1
        const val OUTFITS_MODE = 2
        const val WARDROBE_MODE = 3

        const val MODE_KEY = "mode"
        const val USER_ID_BUNDLE_KEY = "user_id"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile) {
            toolbar_title_text_view.show()
            toolbar_title_text_view.textSize = 14f

            customizeActionToolBar(toolbar = this)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() = presenter.attach(this)

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(USER_ID_BUNDLE_KEY)) {
                currentUserId = it.getInt(USER_ID_BUNDLE_KEY)
            }

            if (it.containsKey(MODE_KEY)) {
                collectionMode = it.getInt(MODE_KEY)
            }
        }

        findNavController().currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<String>(ClothesDetailFragment.DELETED_STATE_KEY)
            ?.observe(viewLifecycleOwner, {
                processPostInitialization()
            })
    }

    override fun initializeViewsData() {
        postFilterModel = PostFilterModel()
        outfitFilterModel = OutfitFilterModel()
        clothesFilterModel = ClothesFilterModel()
        clothesFilterModel.onlyBrands = false

        filterDialog = FilterDialog.getNewInstance(
            itemClickListener = this,
            gender = clothesFilterModel.gender,
            isShowDiscount = false
        ).apply {
            setFilter(filterModel = clothesFilterModel)
        }

        adapterFilter = CollectionsFilterAdapter()
        gridAdapter = GridImageAdapter()

        wardrobeAdapter = ClothesDetailAdapter()
        wardrobeAdapter.setOnClickListener(listener = this)

        outfitsAdapter = OutfitsAdapter()
        outfitsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        filterRecyclerVew = recycler_view_fragment_profile_filter_list
        filterRecyclerVew.adapter = adapterFilter

        collectionRecyclerView = recycler_view_fragment_profile_items_list
        collectionRecyclerView.addItemDecoration(GridSpacesItemDecoration(space = 16))
        collectionRecyclerView.adapter = gridAdapter

        avatarShapeableImageView = fragment_shop_profile_avatar_shapeable_image_view
        userShortNameTextView = text_view_fragment_profile_user_short_name
        userNameTextView = fragment_shop_profile_title_text_view
        publicationsCountTextView = fragment_profile_publications_count
        followersItemLinearLayout = fragment_shop_profile_followers_linear_layout
        followersCountTextView = fragment_profile_followers_count
        followingsItemLinearLayout = linear_layout_fragment_profile_following_item
        followingsCountTextView = fragment_profile_followings_count
        changeProfileTextView = fragment_profile_edit_text_view
        followTextView = fragment_profile_follow_text_view
        unFollowTextView = fragment_profile_unfollow_text_view

        refreshLayout = fragment_profile_swipe_refresh_layout
        refreshLayout.setHeaderView(ProgressLayout(requireContext()))
        refreshLayout.setBottomView(LoadingView(requireContext()))
    }

    override fun initializeListeners() {
        adapterFilter.setOnClickListener(this)
        gridAdapter.setOnClickListener(this)

        changeProfileTextView.setOnClickListener(this)
        followersItemLinearLayout.setOnClickListener(this)
        followingsItemLinearLayout.setOnClickListener(this)
        followTextView.setOnClickListener(this)
        unFollowTextView.setOnClickListener(this)

        include_toolbar_profile.toolbar_right_corner_action_image_button.setOnClickListener(this)
        toolbar_left_corner_action_image_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        getProfile()
        handleRefreshLayout()
    }

    override fun disposeRequests() = presenter.disposeRequests()

    override fun displayMessage(msg: String) = displayToast(msg)

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        refreshLayout.startRefresh()
    }

    override fun hideProgress() {
        refreshLayout.finishRefreshing()
    }

    override fun navigateToMyData() {
        findNavController().navigate(R.id.action_profileFragment_to_myDataFragment)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_right_corner_action_image_button -> navigateToSettings()
            R.id.fragment_profile_edit_text_view -> openEditProfileDialog()
            R.id.fragment_shop_profile_followers_linear_layout -> navigateToFollowers()
            R.id.linear_layout_fragment_profile_following_item -> navigateToFollowings()
            R.id.linear_layout_fragment_profile_photos_item -> navigateToSubs()
            R.id.toolbar_left_corner_action_image_button -> navigateBack()
            R.id.fragment_profile_follow_text_view -> presenter.followUser()
            R.id.fragment_profile_unfollow_text_view -> presenter.unfollowUser()
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.frame_layout_item_collection_filter -> onFilterClick(position)
        }

        when (item) {
            is ClothesFilterModel -> showFilterResults(item)
            is OutfitModel -> onOutfitItemClick(item)
            is PostModel -> onPostItemClick(item)
            is ClothesModel -> onWardrobeItemClick(item)
        }
    }

    override fun completeEditing(isSuccess: Boolean) {
        if (isSuccess) {
            getProfile()
        }
    }

    override fun processProfile(userModel: UserModel) = with (userModel) {
        currentUserId = id
        currentUsername = username

        getFilterList()
        presenter.getFollowers()

        fillProfileInfo(userModel = userModel)
        loadProfilePhoto(userModel = userModel)

        if (isOwnProfile()) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_drawer)
            toolbar_right_corner_action_image_button.show()
        } else {
            clothesFilterModel.owner = username
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
        }

        getWardrobeCount()
        setFilterPosition()
    }

    override fun processFollowers(resultsModel: ResultsModel<FollowerModel>) {
        var isAlreadyFollow = false

        resultsModel.results.map {
            if (it.id == currentActivity.getUserIdFromSharedPref()) {
                isAlreadyFollow = true
            }
        }

        if (!isOwnProfile()) {
            changeProfileTextView.hide()

            if (isAlreadyFollow) {
                unFollowTextView.show()
                followTextView.hide()
            } else {
                unFollowTextView.hide()
                followTextView.show()
            }
        }
    }

    override fun processFilter(filterList: List<CollectionFilterModel>) {
        adapterFilter.updateList(filterList)
    }

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> {
                processCollections(state.data)
                refreshLayout.finishRefreshing()
            }
            is Paginator.State.NewPageProgress<*> -> {
                processCollections(state.data)
                refreshLayout.finishLoadmore()
            }
            else -> {}
        }
    }

    override fun processCollections(list: List<Any?>) {
        when (list[0]) {
            is PostModel -> list.map { it!! }.let {
                gridAdapter.updateList(list = it)
                publicationsCountTextView.text = it.size.toString()
            }
            is ClothesModel -> list.map { it!! }.let {
                wardrobeAdapter.updateList(list = it)
            }
            is OutfitModel -> list.map { it!! }.let {
                outfitsAdapter.updateList(list = it)
            }
        }
    }

    override fun processSuccessFollowing(followSuccessModel: FollowSuccessModel) {
        if (!isOwnProfile() && followSuccessModel.follower == currentActivity.getUserIdFromSharedPref()) {
            changeProfileTextView.hide()
            followTextView.hide()
            unFollowTextView.show()
        }
    }

    override fun processSuccessUnfollowing() {
        if (!isOwnProfile()) {
            changeProfileTextView.hide()
            followTextView.show()
            unFollowTextView.hide()
        }
    }

    override fun processWardrobeCount(count: Int) {
        adapterFilter.changeItemByPosition(
            position = 3,
            title = "${getString(R.string.filter_list_wardrobe)} ($count)"
        )
    }

    override fun getCollectionMode(): Int = collectionMode

    override fun getUserId(): Int = currentUserId

    override fun getClothesFilter(): ClothesFilterModel = clothesFilterModel

    override fun getOutfitFilter(): OutfitFilterModel = outfitFilterModel

    private fun getProfile() {
        presenter.getProfile()
    }

    private fun getFilterList() {
        presenter.getFilerList(isOwnProfile = isOwnProfile())
    }

    private fun getCollections() {
        when (collectionMode) {
            1 -> getPosts()
            2 -> getOutfits()
            3 -> getWardrobe()
        }
    }

    private fun getPosts() {
        postFilterModel.userId = currentUserId

        presenter.getCollections()
    }

    private fun getOutfits() {
        outfitFilterModel.userId = currentUserId
        outfitFilterModel.gender = currentGender
        outfitFilterModel.isMy = isOwnProfile()

        presenter.getCollections()
    }

    private fun getWardrobe() {
        clothesFilterModel.gender = currentGender

        if (isOwnProfile()) {
            clothesFilterModel.inMyWardrobe = isOwnProfile()
        }

        presenter.getCollections()
    }

    private fun getWardrobeCount() {
        clothesFilterModel.gender = currentGender

        if (isOwnProfile()) {
            clothesFilterModel.inMyWardrobe = isOwnProfile()
        }

        presenter.getWardrobeCount()
    }

    private fun handleRefreshLayout() {
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)
                refreshLayout?.startRefresh()
                presenter.getProfile()
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)
                refreshLayout?.startLoadMore()
                presenter.loadMoreList()
            }
        })
    }

    private fun fillProfileInfo(userModel: UserModel) = with (userModel) {
        include_toolbar_profile.toolbar_title_text_view.text = username

        userNameTextView.text = firstName
        followersCountTextView.text = followersCount.toString()
        followingsCountTextView.text = followingsCount.toString()

        adapterFilter.changeItemByPosition(
            position = 2,
            title = "${getString(R.string.filter_list_photo_outfits)} (${outfitsCount})"
        )

        if (isOwnProfile()) {
            changeProfileTextView.show()
            followTextView.hide()
            unFollowTextView.hide()
        }
    }

    private fun loadProfilePhoto(userModel: UserModel) = with (userModel) {
        if (avatar.isBlank()) {
            avatarShapeableImageView.hide()
            userShortNameTextView.show()
            userShortNameTextView.text = displayShortName
        } else {
            userShortNameTextView.hide()
            avatar.loadImageWithCenterCrop(target = avatarShapeableImageView)
        }
    }

    private fun onFilterClick(position: Int) {
        when (position) {
            0 -> filterDialog.apply {
                collectionMode = WARDROBE_MODE
                setFilter(filterModel = clothesFilterModel)
            }.show(childFragmentManager, EMPTY_STRING)
            1 -> onPublicationsFilterClick()
            2 -> onOutfitsFilterClick()
            3 -> onWardrobeFilterClick()
            4 -> navigateToMyData()
            5 -> navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
            6 -> navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
        }
    }

    private fun onPublicationsFilterClick() {
        collectionMode = POSTS_MODE
        setFilterPosition()
    }

    private fun onOutfitsFilterClick() {
        collectionMode = OUTFITS_MODE
        setFilterPosition()
    }

    private fun onWardrobeFilterClick() {
        if (isOwnProfile()) {
            clothesFilterModel.inMyWardrobe = true
        }
        collectionMode = WARDROBE_MODE
        setFilterPosition()
    }

    private fun onOutfitItemClick(item: Any?) {
        item as OutfitModel

        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, item.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.OUTFIT_MODE)

        findNavController().navigate(
            R.id.action_profileFragment_to_collectionDetailFragment,
            bundle
        )
    }

    private fun onPostItemClick(item: Any?) {
        item as PostModel

        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, item.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.POST_MODE)

        findNavController().navigate(
            R.id.action_profileFragment_to_collectionDetailFragment,
            bundle
        )
    }

    private fun onWardrobeItemClick(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(R.id.action_profileFragment_to_clothesDetailFragment, bundle)
    }

    private fun navigateToCameraFragment(mode: Int) {
        val bundle = Bundle()
        bundle.putInt(CameraFragment.MODE_KEY, mode)

        findNavController().navigate(
            R.id.action_profileFragment_to_cameraFragment,
            bundle
        )
    }

    private fun openEditProfileDialog() {
        EditProfileDialog.getNewInstance(
            token = currentActivity.getTokenFromSharedPref(),
            editorListener = this
        ).show(childFragmentManager, "Cart")
    }

    private fun navigateToFollowers() {
        val bundle = Bundle()

        bundle.putInt(UserSubsFragment.USER_ID_ARGS, currentUserId)
        bundle.putString(UserSubsFragment.USERNAME_ARGS, currentUsername)
        bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWERS_POSITION)

        findNavController().navigate(R.id.action_profileFragment_to_userSubsFragment, bundle)
    }

    private fun navigateToFollowings() {
        val bundle = Bundle()

        bundle.putInt(UserSubsFragment.USER_ID_ARGS, currentUserId)
        bundle.putString(UserSubsFragment.USERNAME_ARGS, currentUsername)
        bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWINGS_POSITION)

        findNavController().navigate(R.id.action_profileFragment_to_userSubsFragment, bundle)
    }

    private fun navigateToSettings() {
        findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
    }

    private fun navigateToSubs() {
        findNavController().navigate(R.id.userSubsFragment)
    }

    private fun navigateBack() {
        if (!isOwnProfile()) {
            findNavController().navigateUp()
        }
    }

    private fun showFilterResults(item: Any?) {
        clothesFilterModel = item as ClothesFilterModel
        clothesFilterModel.inMyWardrobe = isOwnProfile()

        collectionMode = WARDROBE_MODE
        setFilterPosition()
    }

    private fun setFilterPosition() {
        when (collectionMode) {
            POSTS_MODE -> {
                adapterFilter.onChooseItem(position = 1)
                gridAdapter.clearList()
                collectionRecyclerView.adapter = gridAdapter
            }
            OUTFITS_MODE -> {
                adapterFilter.onChooseItem(position = 2)
                outfitsAdapter.clearList()
                collectionRecyclerView.adapter = outfitsAdapter
            }
            WARDROBE_MODE -> {
                adapterFilter.onChooseItem(position = 3, isDisabledFirstPosition = false)
                wardrobeAdapter.clearList()
                collectionRecyclerView.adapter = wardrobeAdapter
            }
        }

        getCollections()
    }

    private fun isOwnProfile(): Boolean = currentUserId == currentActivity.getUserIdFromSharedPref()
}