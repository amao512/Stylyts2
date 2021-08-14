package kz.eztech.stylyts.presentation.fragments.profile.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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
import kz.eztech.stylyts.presentation.global.ProfileInfoView
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.profile.ProfilePresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class ProfileFragment : BaseFragment<MainActivity>(), ProfileContract.View, View.OnClickListener,
    UniversalViewClickListener, EditorListener {

    @Inject
    lateinit var presenter: ProfilePresenter

    private lateinit var gridAdapter: GridImageAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter
    private lateinit var wardrobeAdapter: ClothesDetailAdapter
    private lateinit var outfitsAdapter: OutfitsAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var postFilterModel: PostFilterModel
    private lateinit var outfitFilterModel: OutfitFilterModel
    private lateinit var clothesFilterModel: ClothesFilterModel

    private lateinit var profileInfoView: ProfileInfoView
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

        profileInfoView = fragment_profile_profile_info_view
        refreshLayout = fragment_profile_swipe_refresh_layout
        refreshLayout.setHeaderView(ProgressLayout(requireContext()))
        refreshLayout.setBottomView(LoadingView(requireContext()))
    }

    override fun initializeListeners() {
        adapterFilter.setOnClickListener(this)
        gridAdapter.setOnClickListener(this)

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
            R.id.fragment_shop_profile_followers_linear_layout -> navigateToFollowers()
            R.id.linear_layout_fragment_profile_following_item -> navigateToFollowings()
            R.id.toolbar_left_corner_action_image_button -> navigateBack()
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

    override fun processProfile(userModel: UserModel) = with(userModel) {
        include_toolbar_profile.toolbar_title_text_view.text = username

        currentUserId = id
        currentUsername = username
        profileInfoView.setUserModel(userModel, isOwnProfile())

        presenter.getFilerList(isOwnProfile = isOwnProfile())
        presenter.getFollowers()

        adapterFilter.changeItemByPosition(
            position = 2,
            title = "${getString(R.string.filter_list_photo_outfits)} (${outfitsCount})"
        )

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
        setProfileListeners()
    }

    override fun processFollowers(resultsModel: ResultsModel<FollowerModel>) {
        var isAlreadyFollow = false

        resultsModel.results.map {
            if (it.id == currentActivity.getUserIdFromSharedPref()) {
                isAlreadyFollow = true
            }
        }

        profileInfoView.setButtonStates(
            isOwnProfile = isOwnProfile(),
            isAlreadyFollow = isAlreadyFollow
        )
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
            else -> {
            }
        }
    }

    override fun processCollections(list: List<Any?>) {
        when (list[0]) {
            is PostModel -> list.map { it!! }.let {
                gridAdapter.updateList(list = it)
                profileInfoView.setPublicationsCount(count = it.size)
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
            profileInfoView.setButtonStatesOnSuccessFollowing()
        }
    }

    override fun processSuccessUnfollowing() {
        profileInfoView.setButtonStatesOnSuccessUnfollowing(isOwnProfile = isOwnProfile())
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

    private fun setProfileListeners() {
        profileInfoView.onFollowersClickListener {
            navigateToFollowers()
        }

        profileInfoView.onFollowingsClickListener {
            navigateToFollowings()
        }

        profileInfoView.onChangeClickListener {
            openEditProfileDialog()
        }

        profileInfoView.onFollowClickListener {
            presenter.followUser()
        }

        profileInfoView.onUnFollowClickListener {
            presenter.unfollowUser()
        }
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