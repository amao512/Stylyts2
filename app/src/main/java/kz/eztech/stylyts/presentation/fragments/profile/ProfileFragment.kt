package kz.eztech.stylyts.presentation.fragments.profile

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.common.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.clothes.ClothesDetailAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.collection.OutfitsAdapter
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
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ProfileFragment : BaseFragment<MainActivity>(), ProfileContract.View, View.OnClickListener,
    UniversalViewClickListener, EditorListener {

    @Inject lateinit var presenter: ProfilePresenter
    @Inject lateinit var imageLoader: DomainImageLoader

    private lateinit var gridAdapter: GridImageAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter
    private lateinit var wardrobeAdapter: ClothesDetailAdapter
    private lateinit var outfitsAdapter: OutfitsAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var currentFilter: FilterModel

    private lateinit var avatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var photosCountTextView: TextView
    private lateinit var followersItemLinearLayout: LinearLayout
    private lateinit var followersCountTextView: TextView
    private lateinit var followingsItemLinearLayout: LinearLayout
    private lateinit var followingsCountTextView: TextView
    private lateinit var changeProfileTextView: TextView
    private lateinit var followTextView: TextView
    private lateinit var unFollowTextView: TextView
    private lateinit var filterRecyclerVew: RecyclerView
    private lateinit var collectionRecyclerView: RecyclerView

    private var collectionMode: Int = POSTS_MODE

    companion object {
        private const val POSTS_MODE = 1
        private const val OUTFITS_MODE = 2
        private const val WARDROBE_MODE = 3

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
            toolbar_left_corner_action_image_button.show()

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
                currentFilter.userId = it.getInt(USER_ID_BUNDLE_KEY)
            }
        }
    }

    override fun initializeViewsData() {
        currentFilter = FilterModel()
        currentFilter.onlyBrands = false

        filterDialog = FilterDialog.getNewInstance(
            token = currentActivity.getTokenFromSharedPref(),
            itemClickListener = this,
            gender = currentFilter.gender,
            isShowDiscount = false
        ).apply {
            setFilter(filterModel = currentFilter)
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
        photosCountTextView = fragment_profile_photos_count
        followersItemLinearLayout = fragment_shop_profile_followers_linear_layout
        followersCountTextView = fragment_profile_followers_count
        followingsItemLinearLayout = linear_layout_fragment_profile_following_item
        followingsCountTextView = fragment_profile_followings_count
        changeProfileTextView = fragment_profile_edit_text_view
        followTextView = fragment_profile_follow_text_view
        unFollowTextView = fragment_profile_unfollow_text_view
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
        linear_layout_fragment_profile_photos_item.setOnClickListener(this)
        toolbar_left_corner_action_image_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        getProfile()
        handleRecyclerViewScrolling()
    }

    override fun disposeRequests() = presenter.disposeRequests()

    override fun displayMessage(msg: String) = displayToast(msg)

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() = progress_bar_fragment_profile.show()

    override fun hideProgress() = progress_bar_fragment_profile.hide()

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
            R.id.fragment_profile_follow_text_view -> followUser()
            R.id.fragment_profile_unfollow_text_view -> unFollowUser()
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
            is FilterModel -> showFilterResults(item)
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

    override fun processProfile(userModel: UserModel) {
        currentFilter.userId = userModel.id
        currentFilter.username = userModel.username
        currentFilter.gender = userModel.gender

        getFilterList()
        getFollowers()
        getCollections()
        fillProfileInfo(userModel = userModel)
        loadProfilePhoto(userModel = userModel)

        if (isOwnProfile()) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_person_add)
            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_drawer)
            toolbar_right_corner_action_image_button.show()
        } else {
            currentFilter.owner = userModel.username
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
        }
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

    override fun processPostResults(resultsModel: ResultsModel<PostModel>) {
        gridAdapter.updateMoreList(list = resultsModel.results)
        setPagesCondition(resultsModel.totalPages)
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

    override fun processWardrobeResults(resultsModel: ResultsModel<ClothesModel>) {
        wardrobeAdapter.updateMoreList(list = resultsModel.results)
        setPagesCondition(resultsModel.totalPages)
    }

    override fun processOutfitResults(resultsModel: ResultsModel<OutfitModel>) {
        outfitsAdapter.updateMoreList(list = resultsModel.results)
        setPagesCondition(resultsModel.totalPages)
    }

    private fun getProfile() {
        presenter.getProfile(
            token = currentActivity.getTokenFromSharedPref(),
            userId = currentFilter.userId,
        )
    }

    private fun getFilterList() {
        presenter.getFilerList(isOwnProfile = isOwnProfile())
    }

    private fun getFollowers() {
        presenter.getFollowers(
            token = currentActivity.getTokenFromSharedPref(),
            userId = currentFilter.userId
        )
    }

    private fun getCollections() {
        presenter.getCollections(
            token = currentActivity.getTokenFromSharedPref(),
            mode = collectionMode,
            filterModel = currentFilter
        )
    }

    private fun followUser() {
        presenter.followUser(
            token = currentActivity.getTokenFromSharedPref(),
            userId = currentFilter.userId
        )
    }

    private fun unFollowUser() {
        presenter.unfollowUser(
            token = currentActivity.getTokenFromSharedPref(),
            userId = currentFilter.userId
        )
    }

    private fun handleRecyclerViewScrolling() {
        collectionRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!collectionRecyclerView.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!currentFilter.isLastPage) {
                        getCollections()
                    }
                }
            }
        })
    }

    private fun fillProfileInfo(userModel: UserModel) {
        include_toolbar_profile.toolbar_title_text_view.text = userModel.username

        userNameTextView.text = userModel.firstName
        photosCountTextView.text = "${0}"
        followersCountTextView.text = userModel.followersCount.toString()
        followingsCountTextView.text = userModel.followingsCount.toString()
         adapterFilter.changeItemByPosition(
             position = 2,
             title = "${getString(R.string.filter_list_photo_outfits)} (${userModel.outfitsCount})"
         )

        if (isOwnProfile()) {
            changeProfileTextView.show()
            followTextView.hide()
            unFollowTextView.hide()
        }
    }

    private fun loadProfilePhoto(userModel: UserModel) {
        if (userModel.avatar.isBlank()) {
            avatarShapeableImageView.hide()
            userShortNameTextView.show()
            userShortNameTextView.text = getShortName(
                firstName = userModel.firstName,
                lastName = userModel.lastName
            )
        } else {
            userShortNameTextView.hide()
            imageLoader.load(
                url = userModel.avatar,
                target = avatarShapeableImageView
            )
        }
    }

    private fun onFilterClick(position: Int) {
        when (position) {
            0 -> filterDialog.apply {
                resetPages(mode = WARDROBE_MODE)
                setFilter(filterModel = currentFilter)
            }.show(childFragmentManager, EMPTY_STRING)
            1 -> onPublicationsFilterClick(position)
            2 -> onOutfitsFilterClick(position)
            3 -> onWardrobeFilterClick(position)
            4 -> navigateToMyData()
            5 -> navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
            6 -> navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
        }
    }

    private fun onPublicationsFilterClick(position: Int) {
        resetPages(mode = POSTS_MODE)

        collectionRecyclerView.adapter = gridAdapter
        adapterFilter.onChooseItem(position)
        gridAdapter.clearList()

        getCollections()
    }

    private fun onOutfitsFilterClick(position: Int) {
        resetPages(mode = OUTFITS_MODE)

        collectionRecyclerView.adapter = outfitsAdapter
        adapterFilter.onChooseItem(position)
        outfitsAdapter.clearList()

        getCollections()
    }

    private fun onWardrobeFilterClick(position: Int) {
        resetPages(mode = WARDROBE_MODE)

        if (isOwnProfile()) {
            currentFilter.isMy = true
        }

        collectionRecyclerView.adapter = wardrobeAdapter

        adapterFilter.onChooseItem(position, isDisabledFirstPosition = false)
        wardrobeAdapter.clearList()

        getCollections()
    }

    private fun onOutfitItemClick(item: Any?) {
        item as OutfitModel

        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, item.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.OUTFIT_MODE)

        findNavController().navigate(R.id.action_profileFragment_to_collectionDetailFragment, bundle)
    }

    private fun onPostItemClick(item: Any?) {
        item as PostModel

        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, item.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.POST_MODE)

        findNavController().navigate(R.id.action_profileFragment_to_collectionDetailFragment, bundle)
    }

    private fun onWardrobeItemClick(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(R.id.action_profileFragment_to_itemDetailFragment, bundle)
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

        bundle.putInt(UserSubsFragment.USER_ID_ARGS, currentFilter.userId)
        bundle.putString(UserSubsFragment.USERNAME_ARGS, currentFilter.username)
        bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWERS_POSITION)

        findNavController().navigate(R.id.action_profileFragment_to_userSubsFragment, bundle)
    }

    private fun navigateToFollowings() {
        val bundle = Bundle()

        bundle.putInt(UserSubsFragment.USER_ID_ARGS, currentFilter.userId)
        bundle.putString(UserSubsFragment.USERNAME_ARGS, currentFilter.username)
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
        currentFilter = item as FilterModel
        currentFilter.isMy = isOwnProfile()
        wardrobeAdapter.clearList()
        collectionRecyclerView.adapter = wardrobeAdapter

        adapterFilter.onChooseItem(position = 3, isDisabledFirstPosition = false)
        resetPages(mode = WARDROBE_MODE)
        getCollections()
    }

    private fun setPagesCondition(totalPages: Int) {
        if (totalPages > currentFilter.page) {
            currentFilter.page++
        } else {
            currentFilter.isLastPage = true
        }
    }

    private fun resetPages(mode: Int) {
        currentFilter.page = 1
        currentFilter.isLastPage = false
        collectionMode = mode
    }

    private fun isOwnProfile(): Boolean = currentFilter.userId == currentActivity.getUserIdFromSharedPref()
}