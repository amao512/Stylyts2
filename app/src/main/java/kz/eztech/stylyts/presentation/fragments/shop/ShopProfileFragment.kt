package kz.eztech.stylyts.presentation.fragments.shop

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_shop_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitFilterModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
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
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopProfileContract
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.dialogs.shop.ChangeGenderDialog
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection.CollectionDetailFragment
import kz.eztech.stylyts.presentation.fragments.users.UserSubsFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.ShopProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.Paginator
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ShopProfileFragment : BaseFragment<MainActivity>(), ShopProfileContract.View,
    UniversalViewClickListener, View.OnClickListener {

    @Inject lateinit var presenter: ShopProfilePresenter

    private lateinit var publicationsAdapter: GridImageAdapter
    private lateinit var clothesAdapter: ClothesDetailAdapter
    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var outfitsAdapter: OutfitsAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var outfitFilterModel: OutfitFilterModel
    private lateinit var clothesFilterModel: ClothesFilterModel

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
    private lateinit var refreshLayout: TwinklingRefreshLayout

    private var collectionMode = POSTS_MODE
    private var currentUsername: String = EMPTY_STRING
    private var currentGender: String = GenderEnum.MALE.gender

    companion object {
        const val PROFILE_ID_KEY = "profileId"
        const val POSTS_MODE = 0
        const val OUTFITS_MODE = 1
        const val CLOTHES_MODE = 2
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
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
                findNavController().navigate(R.id.action_shopProfileFragment_to_nav_ordering)
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
        outfitFilterModel = OutfitFilterModel()
        clothesFilterModel = ClothesFilterModel()

        filterDialog = FilterDialog.getNewInstance(
            itemClickListener = this,
            gender = clothesFilterModel.gender
        ).apply {
            setFilter(filterModel = clothesFilterModel)
        }

        filterAdapter = CollectionsFilterAdapter()
        filterAdapter.setOnClickListener(listener = this)

        publicationsAdapter = GridImageAdapter()
        publicationsAdapter.setOnClickListener(listener = this)

        clothesAdapter = ClothesDetailAdapter()
        clothesAdapter.setOnClickListener(listener = this)

        outfitsAdapter = OutfitsAdapter()
        outfitsAdapter.setOnClickListener(listener = this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_shop_profile_follow_text_view -> presenter.onFollow()
            R.id.fragment_shop_profile_already_followed_text_view -> presenter.onUnFollow()
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
            R.id.dialog_bottom_change_gender_for_his_text_view -> onChangeGender(position)
            R.id.dialog_bottom_change_gender_for_her_text_view -> onChangeGender(position)
        }

        when (item) {
            is ClothesFilterModel -> showFilterResults(item)
            is PostModel -> onNavigateToPost(item)
            is OutfitModel -> onNavigateToOutfit(item)
            is ClothesModel -> onNavigateToClothes(item)
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

        refreshLayout = fragment_shop_profile_swipe_refresh_layout
        refreshLayout.setHeaderView(ProgressLayout(requireContext()))
        refreshLayout.setBottomView(LoadingView(requireContext()))
    }

    override fun initializeListeners() {
        followTextView.setOnClickListener(this)
        alreadyFollowedTextView.setOnClickListener(this)
        selectGenderTextView.setOnClickListener(this)

        fragment_shop_profile_followers_linear_layout.setOnClickListener(this)
        fragment_shop_profile_followings_linear_layout.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getProfile()
        presenter.getTypes()

        handleRefreshLayout()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg = msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        refreshLayout.startRefresh()
    }

    override fun hideProgress() {
        refreshLayout.finishRefreshing()
    }

    override fun processProfile(userModel: UserModel) = with (userModel) {
        currentUsername = username

        shopNameTextView.text = username
        followersCountTextView.text = followersCount.toString()
        followingsCountTextView.text = followingsCount.toString()
        fragment_shop_profile_toolbar.toolbar_title_text_view.text = username

        processShopAvatar(userModel)
        getFollowers()

        publicationsAdapter.clearList()
        clothesAdapter.clearList()
        outfitsAdapter.clearList()

        getCollections()
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
//            writeMessageTextView.show()
            followTextView.hide()
        } else {
            alreadyFollowedTextView.hide()
//            writeMessageTextView.hide()
            followTextView.show()
        }
    }

    override fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>) {
        val filterList = ArrayList<CollectionFilterModel>()

        filterList.add(
            CollectionFilterModel(
                id = 1,
                name = getString(R.string.filter_list_filter),
                icon = R.drawable.ic_filter,
                isDisabled = true
            )
        )
        filterList.add(
            CollectionFilterModel(
                id = 2,
                name = getString(R.string.filter_list_publishes),
                isChosen = true
            )
        )
        filterList.add(
            CollectionFilterModel(
                id = 3,
                name = getString(R.string.filter_list_photo_outfits)
            )
        )
        filterList.add(
            CollectionFilterModel(
                id = 4,
                name = getString(R.string.filter_list_all_positions)
            )
        )

        var counter = 5

        resultsModel.results.map {
            filterList.add(
                CollectionFilterModel(id = counter, name = it.title, item = it)
            )
            counter++
        }

        filterAdapter.updateList(filterList)
    }

    override fun getUserId(): Int = arguments?.getInt(PROFILE_ID_KEY) ?: 0

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
                hideProgress()
                refreshLayout.finishLoadmore()
            }
        }
    }

    override fun processCollections(list: List<Any?>) {
        when (list[0]) {
            is PostModel -> list.map { it!! }.let {
                publicationsAdapter.updateList(list = it)
                collectionsRecyclerView.adapter = publicationsAdapter
            }
            is ClothesModel -> list.map { it!! }.let {
                clothesAdapter.updateList(list = it)
                collectionsRecyclerView.adapter = clothesAdapter
            }
            is OutfitModel -> list.map { it!! }.let {
                outfitsAdapter.updateList(list = it)
                collectionsRecyclerView.adapter = outfitsAdapter
            }
        }
    }

    override fun getCollectionMode(): Int = collectionMode

    override fun getOutfitFilter(): OutfitFilterModel = outfitFilterModel

    override fun getClothesFilter(): ClothesFilterModel = clothesFilterModel

    override fun processSuccessFollowing(followSuccessModel: FollowSuccessModel) {
        followTextView.hide()
        alreadyFollowedTextView.show()
//        writeMessageTextView.show()
    }

    override fun processSuccessUnfollowing() {
        followTextView.show()
        alreadyFollowedTextView.hide()
//        writeMessageTextView.hide()
    }

    private fun onFilterItemClicked(
        item: Any?,
        position: Int
    ) {
        item as CollectionFilterModel

        when (position) {
            0 -> onFilterClick()
            1 -> onFilterPublicationsClick()
            2 -> onFilterOutfitsClick()
            3 -> onFilterAllPositionsClick(position)
            else -> onFilterClothesClick(position, item)
        }
    }

    private fun onFilterClick() {
        filterDialog.apply {
            setFilter(filterModel = clothesFilterModel)
        }.show(childFragmentManager, EMPTY_STRING)
    }

    private fun onFilterPublicationsClick() {
        collectionMode = POSTS_MODE

        setFilterPosition()
    }

    private fun onFilterOutfitsClick() {
        collectionMode = OUTFITS_MODE

        setFilterPosition()
    }

    private fun onFilterAllPositionsClick(position: Int) {
        filterAdapter.onChooseItem(position = position, isDisabledFirstPosition = false)
        clothesFilterModel.typeIdList = emptyList()
        collectionMode = CLOTHES_MODE

        setFilterPosition()
    }

    private fun onFilterClothesClick(
        position: Int,
        collectionFilterModel: CollectionFilterModel
    ) {
        filterAdapter.onChooseItem(position, isDisabledFirstPosition = false)
        clothesFilterModel.typeIdList = listOf(collectionFilterModel.item as ClothesTypeModel)
        collectionMode = CLOTHES_MODE

        setFilterPosition()
    }

    private fun onNavigateToPost(postModel: PostModel) {
        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, postModel.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.POST_MODE)

        findNavController().navigate(
            R.id.action_shopProfileFragment_to_collectionDetailFragment,
            bundle
        )
    }

    private fun onNavigateToOutfit(outfitModel: OutfitModel) {
        val bundle = Bundle()
        bundle.putInt(CollectionDetailFragment.ID_KEY, outfitModel.id)
        bundle.putInt(CollectionDetailFragment.MODE_KEY, CollectionDetailFragment.OUTFIT_MODE)

        findNavController().navigate(
            R.id.action_shopProfileFragment_to_collectionDetailFragment,
            bundle
        )
    }

    private fun onNavigateToClothes(clothesModel: ClothesModel) {
        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, clothesModel.id)

        findNavController().navigate(
            R.id.action_shopProfileFragment_to_clothesDetailFragment,
            bundle
        )
    }

    private fun onChangeGender(gender: Int) {
        when (gender) {
            ChangeGenderDialog.MALE_GENDER -> {
                selectGenderTextView.text = getString(R.string.for_his)
                currentGender = GenderEnum.MALE.gender
            }
            ChangeGenderDialog.FEMALE_GENDER -> {
                selectGenderTextView.text = getString(R.string.for_her)
                currentGender = GenderEnum.FEMALE.gender
            }
        }

        setFilterPosition()
    }

    private fun navigateToFollowers() {
        val bundle = Bundle()

        bundle.putInt(UserSubsFragment.USER_ID_ARGS, getUserId())
        bundle.putString(UserSubsFragment.USERNAME_ARGS, currentUsername)
        bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWERS_POSITION)

        findNavController().navigate(R.id.action_shopProfileFragment_to_userSubsFragment, bundle)
    }

    private fun navigateToFollowings() {
        val bundle = Bundle()

        bundle.putInt(UserSubsFragment.USER_ID_ARGS, getUserId())
        bundle.putString(UserSubsFragment.USERNAME_ARGS, currentUsername)
        bundle.putInt(UserSubsFragment.POSITION_ARGS, UserSubsFragment.FOLLOWINGS_POSITION)

        findNavController().navigate(R.id.action_shopProfileFragment_to_userSubsFragment, bundle)
    }

    private fun selectGender() {
        ChangeGenderDialog.getNewInstance(
            universalViewClickListener = this,
            selectedGender = when (currentGender) {
                GenderEnum.MALE.gender -> ChangeGenderDialog.MALE_GENDER
                else -> ChangeGenderDialog.FEMALE_GENDER
            }
        ).show(childFragmentManager, EMPTY_STRING)
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
                presenter.loadMorePage()
            }
        })
    }

    private fun getCollections() {
        when (collectionMode) {
            POSTS_MODE -> presenter.getCollections()
            OUTFITS_MODE -> getOutfits()
            CLOTHES_MODE -> getClothes()
        }
    }

    private fun getOutfits() {
        outfitFilterModel.userId = getUserId()
        outfitFilterModel.gender = currentGender

        presenter.getCollections()
    }

    private fun getClothes() {
        clothesFilterModel.owner = currentUsername
        clothesFilterModel.gender = currentGender

        presenter.getCollections()
    }

    private fun getFollowers() {
        presenter.getFollowers()
    }

    private fun processShopAvatar(userModel: UserModel) = with (userModel) {
        if (avatar.isBlank()) {
            shopShortNameTextView.text = displayShortName
            shopAvatarShapeableImageView.hide()
            shopShortNameTextView.show()
        } else {
            avatar.loadImageWithCenterCrop(target = shopAvatarShapeableImageView)
            shopShortNameTextView.hide()
            shopAvatarShapeableImageView.show()
        }
    }

    private fun showFilterResults(item: Any?) {
        clothesFilterModel = item as ClothesFilterModel

        collectionMode = CLOTHES_MODE
        filterAdapter.onChooseItem(position = 3, isDisabledFirstPosition = false)

        setFilterPosition()
    }

    private fun setFilterPosition() {
        when (collectionMode) {
            POSTS_MODE -> {
                filterAdapter.onChooseItem(position = 1)
                publicationsAdapter.clearList()
                collectionsRecyclerView.adapter = publicationsAdapter
            }
            OUTFITS_MODE -> {
                filterAdapter.onChooseItem(position = 2)
                outfitsAdapter.clearList()
                collectionsRecyclerView.adapter = outfitsAdapter
            }
            CLOTHES_MODE -> {
                clothesAdapter.clearList()
                collectionsRecyclerView.adapter = clothesAdapter
            }
        }

        getCollections()
    }
}