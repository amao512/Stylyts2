package kz.eztech.stylyts.presentation.fragments.shop

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_shop_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.helpers.DomainImageLoader
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.clothes.ClothesDetailAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.collection.OutfitsAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopProfileContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.dialogs.shop.ChangeGenderDialog
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection.CollectionDetailFragment
import kz.eztech.stylyts.presentation.fragments.users.UserSubsFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.ShopProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ShopProfileFragment : BaseFragment<MainActivity>(), ShopProfileContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject lateinit var presenter: ShopProfilePresenter
    @Inject lateinit var imageLoader: DomainImageLoader

    private lateinit var clothesAdapter: ClothesDetailAdapter
    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var outfitsAdapter: OutfitsAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var currentFilter: FilterModel

    private lateinit var shopAvatarShapeableImageView: ShapeableImageView
    private lateinit var shopShortNameTextView: TextView
    private lateinit var shopNameTextView: TextView
    private lateinit var followersCountTextView: TextView
    private lateinit var followingsCountTextView: TextView
    private lateinit var followTextView: TextView
    private lateinit var unFollowTextView: TextView
    private lateinit var alreadyFollowedTextView: TextView
    private lateinit var writeMessageTextView: TextView
    private lateinit var selectGenderTextView: TextView
    private lateinit var filterListRecyclerView: RecyclerView
    private lateinit var collectionsRecyclerView: RecyclerView

    private var collectionMode = OUTFITS_MODE

    companion object {
        const val PROFILE_ID_KEY = "profileId"
        private const val OUTFITS_MODE = 0
        private const val CLOTHES_MODE = 1
    }

    override fun getLayoutId(): Int = R.layout.fragment_shop_profile

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_shop_profile_toolbar) {
            toolbar_title_text_view.show()

            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener {
                findNavController().navigateUp()
            }

            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setOnClickListener {
                CartDialog().show(childFragmentManager, EMPTY_STRING)
            }

            customizeActionToolBar(toolbar = this)
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
        currentFilter = FilterModel()
        filterDialog = FilterDialog.getNewInstance(
            token = currentActivity.getTokenFromSharedPref(),
            itemClickListener = this,
            gender = currentFilter.gender
        ).apply {
            setFilter(filterModel = currentFilter)
        }

        filterAdapter = CollectionsFilterAdapter()
        filterAdapter.setOnClickListener(listener = this)

        clothesAdapter = ClothesDetailAdapter()
        clothesAdapter.setOnClickListener(listener = this)

        outfitsAdapter = OutfitsAdapter()
        outfitsAdapter.setOnClickListener(listener = this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_shop_profile_follow_text_view -> presenter.onFollow(
                token = currentActivity.getTokenFromSharedPref(),
                userId = currentFilter.userId
            )
            R.id.fragment_shop_profile_already_followed_text_view -> presenter.onUnFollow(
                token = currentActivity.getTokenFromSharedPref(),
                userId = currentFilter.userId
            )
            R.id.fragment_shop_profile_followers_linear_layout -> navigateToFollowers()
            R.id.fragment_shop_profile_followings_linear_layout -> navigateToFollowings()
            R.id.fragment_shop_profile_select_gender_text_view -> selectGender()
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.frame_layout_item_collection_filter -> onFilterItemClicked(item, position)
            R.id.shapeable_image_view_item_collection_image -> onNavigateToOutfit(item)
            R.id.item_clothes_detail_linear_layout -> onNavigateToClothes(item)
            R.id.dialog_bottom_change_gender_for_his_text_view -> onChangeGender(position)
            R.id.dialog_bottom_change_gender_for_her_text_view -> onChangeGender(position)
        }

        when (item) {
            is FilterModel -> showFilterResults(item)
        }
    }

    override fun initializeViews() {
        shopAvatarShapeableImageView = fragment_shop_profile_avatar_shapeable_image_view
        shopShortNameTextView = fragment_shop_profile_shop_short_name_text_view
        shopNameTextView = fragment_shop_profile_title_text_view
        followersCountTextView = fragment_shop_profile_followers_count_text_view
        followingsCountTextView = fragment_shop_profile_followings_count_text_view
        followTextView = fragment_shop_profile_follow_text_view
        unFollowTextView = fragment_shop_profile_unfollow_text_view
        alreadyFollowedTextView = fragment_shop_profile_already_followed_text_view
        writeMessageTextView = fragment_shop_profile_send_message_text_view
        selectGenderTextView = fragment_shop_profile_select_gender_text_view
        filterListRecyclerView = fragment_shop_profile_filter_recycler_view
        collectionsRecyclerView = fragment_shop_profile_clothes_recycler_view

        filterListRecyclerView.adapter = filterAdapter
        collectionsRecyclerView.adapter = outfitsAdapter
        collectionsRecyclerView.addItemDecoration(GridSpacesItemDecoration(space = 16))
    }

    override fun initializeListeners() {
        followTextView.setOnClickListener(this)
        alreadyFollowedTextView.setOnClickListener(this)
        selectGenderTextView.setOnClickListener(this)

        fragment_shop_profile_followers_linear_layout.setOnClickListener(this)
        fragment_shop_profile_followings_linear_layout.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getProfile(
            token = currentActivity.getTokenFromSharedPref(),
            id = getIdFromArgs()
        )
        presenter.getTypes(token = currentActivity.getTokenFromSharedPref())

        handleCollectionRecyclerView()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg = msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processProfile(userModel: UserModel) {
        currentFilter.userId = userModel.id
        currentFilter.username = userModel.username
        currentFilter.owner = userModel.username
        currentFilter.gender = GenderEnum.MALE.gender

        shopNameTextView.text = userModel.username
        followersCountTextView.text = userModel.followersCount.toString()
        followingsCountTextView.text = userModel.followingsCount.toString()
        fragment_shop_profile_toolbar.toolbar_title_text_view.text = userModel.username


        processShopAvatar(userModel)
        getFollowers(userModel)
        getOutfits()
    }

    override fun processFollowers(resultsModel: ResultsModel<FollowerModel>) {
        var isAlreadyFollow = false

        resultsModel.results.map {
            if (it.id == currentActivity.getUserIdFromSharedPref()) {
                isAlreadyFollow = true
            }
        }

        if (isAlreadyFollow) {
            alreadyFollowedTextView.show()
            writeMessageTextView.show()
            followTextView.hide()
        } else {
            alreadyFollowedTextView.hide()
            writeMessageTextView.hide()
            followTextView.show()
        }
    }

    override fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>) {
        val filterList = ArrayList<CollectionFilterModel>()

        filterList.add(CollectionFilterModel(
            id = 1,
            name = getString(R.string.filter_list_filter),
            icon = R.drawable.ic_filter,
            isDisabled = true
        ))
        filterList.add(CollectionFilterModel(
            id = 2,
            name = getString(R.string.filter_list_photo_outfits),
            isChosen = true
        ))
        filterList.add(CollectionFilterModel(
            id = 3,
            name = getString(R.string.filter_list_all_positions)
        ))

        var counter = 4

        resultsModel.results.map {
            filterList.add(
                CollectionFilterModel(id = counter, name = it.title, item = it)
            )
            counter++
        }

        filterAdapter.updateList(filterList)
    }

    override fun processOutfits(resultsModel: ResultsModel<OutfitModel>) {
        outfitsAdapter.updateMoreList(list = resultsModel.results)
        collectionsRecyclerView.adapter = outfitsAdapter

        setPagesCondition(totalPages = resultsModel.totalPages)
    }

    override fun processClothes(resultsModel: ResultsModel<ClothesModel>) {
        clothesAdapter.updateMoreList(list = resultsModel.results)
        collectionsRecyclerView.adapter = clothesAdapter

        setPagesCondition(totalPages = resultsModel.totalPages)
    }

    override fun processSuccessFollowing(followSuccessModel: FollowSuccessModel) {
        followTextView.hide()
        alreadyFollowedTextView.show()
        writeMessageTextView.show()
    }

    override fun processSuccessUnfollowing() {
        followTextView.show()
        alreadyFollowedTextView.hide()
        writeMessageTextView.hide()
    }

    private fun onFilterItemClicked(
        item: Any?,
        position: Int
    ) {
        item as CollectionFilterModel

        when (position) {
            0 -> onFilterClick()
            1 -> onFilterOutfitsClick(position)
            2 -> onFilterClothesClick(
                position = position,
                typeIdList = emptyList()
            )
            else -> onFilterClothesClick(
                position = position,
                typeIdList = listOf((item.item as ClothesTypeModel).id)
            )
        }
    }

    private fun onFilterClick() {
        filterDialog.apply {
            setFilter(filterModel = currentFilter)
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun onFilterOutfitsClick(position: Int) {
        filterAdapter.onChooseItem(position)
        outfitsAdapter.clearList()

        resetPages(mode = OUTFITS_MODE)
        getOutfits()
    }

    private fun onFilterClothesClick(
        position: Int,
        typeIdList: List<Int>
    ) {
        filterAdapter.onChooseItem(position, isDisabledFirstPosition = false)
        currentFilter.typeIdList = typeIdList
        clothesAdapter.clearList()

        resetPages(mode = CLOTHES_MODE)
        getClothes()
    }

    private fun onNavigateToOutfit(item: Any?) {
        item as OutfitModel

        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, item.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.OUTFIT_MODE)

        findNavController().navigate(R.id.action_shopProfileFragment_to_collectionDetailFragment, bundle)
    }

    private fun onNavigateToClothes(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(R.id.action_shopProfileFragment_to_clothesDetailFragment, bundle)
    }

    private fun onChangeGender(gender: Int) {
        when (gender) {
            ChangeGenderDialog.MALE_GENDER -> {
                selectGenderTextView.text = getString(R.string.for_his)
                currentFilter.gender = GenderEnum.MALE.gender
            }
            ChangeGenderDialog.FEMALE_GENDER -> {
                selectGenderTextView.text = getString(R.string.for_her)
                currentFilter.gender = GenderEnum.FEMALE.gender
            }
        }

        outfitsAdapter.clearList()
        clothesAdapter.clearList()
        resetPages(mode = collectionMode)
        getCollections()
    }

    private fun navigateToFollowers() {
        val bundle = Bundle()

        bundle.putInt(UserSubsFragment.USER_ID_ARGS, currentFilter.userId)
        bundle.putString(UserSubsFragment.USERNAME_ARGS, currentFilter.username)
        bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWERS_POSITION)

        findNavController().navigate(R.id.action_shopProfileFragment_to_userSubsFragment, bundle)
    }

    private fun navigateToFollowings() {
        val bundle = Bundle()

        bundle.putInt(UserSubsFragment.USER_ID_ARGS, currentFilter.userId)
        bundle.putString(UserSubsFragment.USERNAME_ARGS, currentFilter.username)
        bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWINGS_POSITION)

        findNavController().navigate(R.id.action_shopProfileFragment_to_userSubsFragment, bundle)
    }

    private fun selectGender() {
        ChangeGenderDialog.getNewInstance(
            universalViewClickListener = this,
            selectedGender = when (currentFilter.gender) {
                GenderEnum.MALE.gender -> ChangeGenderDialog.MALE_GENDER
                else -> ChangeGenderDialog.FEMALE_GENDER
            }
        ).show(childFragmentManager, EMPTY_STRING)
    }

    private fun handleCollectionRecyclerView() {
        collectionsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!collectionsRecyclerView.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!currentFilter.isLastPage) {
                        getCollections()
                    }
                }
            }
        })
    }

    private fun getCollections() {
        when (collectionMode) {
            OUTFITS_MODE -> getOutfits()
            CLOTHES_MODE -> getClothes()
        }
    }

    private fun getOutfits() {
        presenter.getOutfits(
            token = currentActivity.getTokenFromSharedPref(),
            filterModel = currentFilter
        )
    }

    private fun getClothes() {
        presenter.getClothes(
            token = currentActivity.getTokenFromSharedPref(),
            filterModel = currentFilter
        )
    }

    private fun getFollowers(userModel: UserModel) {
        presenter.getFollowers(
            token = currentActivity.getTokenFromSharedPref(),
            userId = userModel.id
        )
    }

    private fun processShopAvatar(userModel: UserModel) {
        if (userModel.avatar.isBlank()) {
            shopShortNameTextView.text = getShortName(
                firstName = userModel.firstName,
                lastName = userModel.lastName
            )
            shopAvatarShapeableImageView.hide()
            shopShortNameTextView.show()
        } else {
            imageLoader.load(
                url = userModel.avatar,
                target = shopAvatarShapeableImageView
            )
            shopShortNameTextView.hide()
            shopAvatarShapeableImageView.show()
        }
    }

    private fun showFilterResults(item: Any?) {
        currentFilter = item as FilterModel

        collectionsRecyclerView.adapter = clothesAdapter
        clothesAdapter.clearList()

        getCollections()
        resetPages(mode = CLOTHES_MODE)
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

    private fun getIdFromArgs(): Int = arguments?.getInt(PROFILE_ID_KEY) ?: 0
}