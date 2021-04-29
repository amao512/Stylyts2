package kz.eztech.stylyts.presentation.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.data.models.SharedConstants.ACCESS_TOKEN_KEY
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.clothes.ClothesDetailAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.collection.GridImageCollectionItemAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.base.EditorListener
import kz.eztech.stylyts.presentation.contracts.profile.ProfileContract
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.dialogs.profile.CreatorChooserDialog
import kz.eztech.stylyts.presentation.dialogs.profile.EditProfileDialog
import kz.eztech.stylyts.presentation.fragments.camera.CameraFragment
import kz.eztech.stylyts.presentation.fragments.collection.ClothesDetailFragment
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
    UniversalViewClickListener, EditorListener, DialogChooserListener {

    @Inject lateinit var presenter: ProfilePresenter
    @Inject lateinit var imageLoader: DomainImageLoader

    private lateinit var gridAdapter: GridImageAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter
    private lateinit var wardrobeAdapter: ClothesDetailAdapter
    private lateinit var outfitsAdapter: GridImageCollectionItemAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var currentFilter: FilterModel

    private var isOwnProfile: Boolean = true
    private var userId: Int = 0
    private var isAlreadyFollow: Boolean = false
    private var isMyData = false
    private var currentName = EMPTY_STRING
    private var currentUsername = EMPTY_STRING
    private var currentSurname = EMPTY_STRING
    private var currentGender = EMPTY_STRING
    private var currentPage: Int = 1
    private var isLastPage: Boolean = false
    private var isPosts: Boolean = true
    private var isOutfits: Boolean = false
    private var isWardrobes: Boolean = false

    companion object {
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

            if (isOwnProfile) {
                toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_person_add)
                toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_drawer)
                toolbar_right_corner_action_image_button.show()
            } else {
                toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            }

            customizeActionToolBar(toolbar = this)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() = presenter.attach(this)

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(USER_ID_BUNDLE_KEY)) {
                userId = it.getInt(USER_ID_BUNDLE_KEY)
                isOwnProfile = userId == currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)
            }
        }
    }

    override fun initializeViewsData() {
        currentFilter = FilterModel()
        filterDialog = FilterDialog.getNewInstance(
            token = getTokenFromSharedPref(),
            itemClickListener = this,
            gender = currentGender,
            isShowDiscount = false
        ).apply {
            setFilter(filterModel = currentFilter)
        }
        adapterFilter = CollectionsFilterAdapter()
        gridAdapter = GridImageAdapter()

        wardrobeAdapter = ClothesDetailAdapter()
        wardrobeAdapter.itemClickListener = this

        outfitsAdapter = GridImageCollectionItemAdapter()
        outfitsAdapter.itemClickListener = this
    }

    override fun initializeViews() {
        recycler_view_fragment_profile_filter_list.adapter = adapterFilter
        recycler_view_fragment_profile_items_list.adapter = gridAdapter
        recycler_view_fragment_profile_items_list.addItemDecoration(GridSpacesItemDecoration(space = 16))
    }

    override fun initializeListeners() {
        adapterFilter.setOnClickListener(this)
        gridAdapter.setOnClickListener(this)

        include_toolbar_profile.toolbar_right_corner_action_image_button.setOnClickListener(this)
        frame_layout_fragment_profile_my_incomes.setOnClickListener(this)
        frame_layout_fragment_profile_my_addresses.setOnClickListener(this)
        fragment_profile_edit_text_view.setOnClickListener(this)
        frame_layout_fragment_profile_cards.setOnClickListener(this)
        linear_layout_fragment_profile_followers_item.setOnClickListener(this)
        linear_layout_fragment_profile_following_item.setOnClickListener(this)
        linear_layout_fragment_profile_photos_item.setOnClickListener(this)
        toolbar_left_corner_action_image_button.setOnClickListener(this)
        fragment_profile_follow_text_view.setOnClickListener(this)
        fragment_profile_unfollow_text_view.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getFilerList(isOwnProfile)
        presenter.getProfile(
            token = getTokenFromSharedPref(),
            userId = userId.toString(),
            isOwnProfile = isOwnProfile
        )
        presenter.getFollowers(
            token = getTokenFromSharedPref(),
            userId = userId
        )

        recycler_view_fragment_profile_items_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recycler_view_fragment_profile_items_list.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLastPage) {
                        getCollections()
                    }
                }
            }
        })
    }

    override fun disposeRequests() = presenter.disposeRequests()

    override fun displayMessage(msg: String) = displayToast(msg)

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() = progress_bar_fragment_profile.show()

    override fun hideProgress() = progress_bar_fragment_profile.hide()

    override fun showMyData() {
        linear_layout_fragment_profile_followers_item.hide()
        linear_layout_fragment_profile_change_buttons.hide()
        linear_layout_fragment_profile_photos_item.hide()
        linear_layout_fragment_profile_following_item.hide()
        recycler_view_fragment_profile_filter_list.hide()
        recycler_view_fragment_profile_items_list.hide()
        frame_layout_fragment_profile_settings_container.show()
    }

    override fun hideMyData() {
        frame_layout_fragment_profile_settings_container.hide()
        linear_layout_fragment_profile_followers_item.show()
        linear_layout_fragment_profile_change_buttons.show()
        linear_layout_fragment_profile_photos_item.show()
        linear_layout_fragment_profile_following_item.show()
        recycler_view_fragment_profile_filter_list.show()
        recycler_view_fragment_profile_items_list.show()
    }

    override fun processMyData() {
        if (isMyData) {
            isMyData = false
            hideMyData()
        } else {
            isMyData = true
            showMyData()
        }
    }

    override fun processProfile(userModel: UserModel) {
        userModel.run {
            userId = id
            currentName = firstName
            currentUsername = username
            currentSurname = lastName
            currentGender = when (gender) {
                "male" -> "M"
                else -> "F"
            }
        }

        presenter.getPosts(
            token = getTokenFromSharedPref(),
            authorId = userId,
            page = currentPage
        )

        fillProfileInfo(userModel = userModel)
        loadProfilePhoto(userModel = userModel)
    }

    override fun processFilter(filterList: List<CollectionFilterModel>) {
        adapterFilter.updateList(filterList)
    }

    override fun processPostResults(resultsModel: ResultsModel<PostModel>) {
        gridAdapter.updateMoreList(list = resultsModel.results)

        if (resultsModel.totalPages != currentPage) {
            currentPage++
        } else {
            isLastPage = true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_right_corner_action_image_button -> {
                findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
            }
            R.id.frame_layout_fragment_profile_my_incomes -> {
                findNavController().navigate(R.id.action_profileFragment_to_profileIncomeFragment)
            }
            R.id.frame_layout_fragment_profile_my_addresses -> {
                findNavController().navigate(R.id.action_profileFragment_to_addressProfileFragment)
            }
            R.id.fragment_profile_edit_text_view -> EditProfileDialog.getNewInstance(
                token = getTokenFromSharedPref(),
                editorListener = this
            ).show(childFragmentManager, "Cart")
            R.id.frame_layout_fragment_profile_cards -> {
                findNavController().navigate(R.id.action_profileFragment_to_cardFragment)
            }
            R.id.linear_layout_fragment_profile_followers_item -> {
                val bundle = Bundle()
                bundle.putInt(UserSubsFragment.USER_ID_ARGS, userId)
                bundle.putString(UserSubsFragment.USERNAME_ARGS, currentUsername)
                bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWERS_POSITION)

                findNavController().navigate(R.id.action_profileFragment_to_userSubsFragment, bundle)
            }
            R.id.linear_layout_fragment_profile_following_item -> {
                val bundle = Bundle()
                bundle.putInt(UserSubsFragment.USER_ID_ARGS, userId)
                bundle.putString(UserSubsFragment.USERNAME_ARGS, currentUsername)
                bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWINGS_POSITION)

                findNavController().navigate(R.id.action_profileFragment_to_userSubsFragment, bundle)
            }
            R.id.linear_layout_fragment_profile_photos_item -> {
                findNavController().navigate(R.id.userSubsFragment)
            }
            R.id.toolbar_left_corner_action_image_button -> {
                if (!isOwnProfile) {
                    findNavController().navigateUp()
                }
            }
            R.id.fragment_profile_follow_text_view -> presenter.followUser(
                token = getTokenFromSharedPref(),
                userId = userId
            )
            R.id.fragment_profile_unfollow_text_view -> presenter.unfollowUser(
                token = getTokenFromSharedPref(),
                userId = userId
            )
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
            presenter.getProfile(
                token = getTokenFromSharedPref(),
                userId = userId.toString(),
                isOwnProfile = isOwnProfile
            )
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        when (v?.id) {
            R.id.dialog_bottom_creator_chooser_barcode -> {
                navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
            }
            R.id.dialog_bottom_creator_chooser_photo -> {
                navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
            }
            R.id.dialog_bottom_creator_chooser_create_publish -> {
                findNavController().navigate(R.id.action_profileFragment_to_photoPostCreatorFragment)
            }
        }
    }

    override fun processFollowers(resultsModel: ResultsModel<FollowerModel>) {
        resultsModel.results.let {
            val currentUserId = currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)

            it.map { follower ->
                isAlreadyFollow = follower.id == currentUserId
            }

            if (!isOwnProfile && !isAlreadyFollow) {
                fragment_profile_edit_text_view.hide()
                fragment_profile_follow_text_view.show()
                fragment_profile_unfollow_text_view.hide()
            }

            if (!isOwnProfile && isAlreadyFollow) {
                fragment_profile_edit_text_view.hide()
                fragment_profile_follow_text_view.hide()
                fragment_profile_unfollow_text_view.show()
            }
        }
    }

    override fun processSuccessFollowing(followSuccessModel: FollowSuccessModel) {
        val currentUserId = currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)

        if (!isOwnProfile && followSuccessModel.follower == currentUserId) {
            fragment_profile_edit_text_view.hide()
            fragment_profile_follow_text_view.hide()
            fragment_profile_unfollow_text_view.show()
        }
    }

    override fun processSuccessUnfollowing() {
        if (!isOwnProfile) {
            fragment_profile_edit_text_view.hide()
            fragment_profile_follow_text_view.show()
            fragment_profile_unfollow_text_view.hide()
        }
    }

    override fun processWardrobeResults(resultsModel: ResultsModel<ClothesModel>) {
        wardrobeAdapter.updateMoreList(list = resultsModel.results)

        if (resultsModel.totalPages != currentPage) {
            currentPage++
        } else {
            isLastPage = true
        }
    }

    override fun processOutfitResults(resultsModel: ResultsModel<OutfitModel>) {
        outfitsAdapter.updateMoreList(list = resultsModel.results)

        if (resultsModel.totalPages != currentPage) {
            currentPage++
        } else {
            isLastPage = true
        }
    }

    private fun getCollections() {
        when {
            isOutfits -> presenter.getOutfits(
                token = getTokenFromSharedPref(),
                page = currentPage
            )
            isPosts -> presenter.getPosts(
                token = getTokenFromSharedPref(),
                authorId = userId,
                page = currentPage
            )
            isWardrobes -> presenter.getWardrobe(
                token = getTokenFromSharedPref(),
                filterModel = currentFilter,
                page = currentPage
            )
        }
    }

    private fun fillProfileInfo(userModel: UserModel) {
        include_toolbar_profile.toolbar_title_text_view.text = userModel.username
        text_view_fragment_profile_user_name.text = userModel.firstName
        fragment_profile_photos_count.text = "${0}"
        fragment_profile_followers_count.text = userModel.followersCount.toString()
        fragment_profile_followings_count.text = userModel.followingsCount.toString()
    }

    private fun loadProfilePhoto(userModel: UserModel) {
        if (userModel.avatar.isBlank()) {
            shapeable_image_view_fragment_profile_avatar.hide()
            text_view_fragment_profile_user_short_name.show()
            text_view_fragment_profile_user_short_name.text = getShortName(
                firstName = userModel.firstName,
                lastName = userModel.lastName
            )
        } else {
            text_view_fragment_profile_user_short_name.hide()
            imageLoader.load(
                url = userModel.avatar,
                target = shapeable_image_view_fragment_profile_avatar
            )
        }
    }

    private fun onFilterClick(position: Int) {
        when (position) {
            0 -> filterDialog.apply {
                setFilter(filterModel = currentFilter)
            }.show(childFragmentManager, EMPTY_STRING)
            1 -> onPublicationsFilterClick(position)
            2 -> onOutfitsFilterClick(position)
            3 -> onWardrobeFilterClick(position)
            4 -> processMyData()
            5 ->  CreatorChooserDialog().apply {
                setChoiceListener(listener = this@ProfileFragment)
            }.show(childFragmentManager, EMPTY_STRING)
        }
    }

    private fun onPublicationsFilterClick(position: Int) {
        currentPage = 1
        isLastPage = false
        isPosts = true
        isWardrobes = false
        isOutfits = false

        getCollections()
        recycler_view_fragment_profile_items_list.adapter = gridAdapter

        adapterFilter.onChooseItem(position)
        gridAdapter.clearList()
    }

    private fun onOutfitsFilterClick(position: Int) {
        if (isOwnProfile) {
            currentPage = 1
            isLastPage = false
            isPosts = false
            isWardrobes = false
            isOutfits = true

            getCollections()
        }
        recycler_view_fragment_profile_items_list.adapter = outfitsAdapter
        adapterFilter.onChooseItem(position)
        outfitsAdapter.clearList()
    }

    private fun onWardrobeFilterClick(position: Int) {
        currentPage = 1
        isLastPage = false
        isPosts = false
        isWardrobes = true
        isOutfits = false

        if (isOwnProfile) {
            currentFilter.isMyWardrobe = true
        }

        recycler_view_fragment_profile_items_list.adapter = wardrobeAdapter

        getCollections()
        adapterFilter.onChooseItem(position)
        wardrobeAdapter.clearList()
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

    private fun showFilterResults(item: Any?) {
        currentFilter = item as FilterModel
        currentPage = 1
        isLastPage = false
        isPosts = false
        isWardrobes = true
        isOutfits = false
        wardrobeAdapter.clearList()

        if (isOwnProfile) {
            currentFilter.isMyWardrobe = true
        }

        recycler_view_fragment_profile_items_list.adapter = wardrobeAdapter

        getCollections()
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}