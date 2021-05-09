package kz.eztech.stylyts.presentation.fragments.shop

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
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.clothes.ClothesDetailAdapter
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopProfileContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.ShopProfilePresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ShopProfileFragment : BaseFragment<MainActivity>(), ShopProfileContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: ShopProfilePresenter
    @Inject lateinit var imageLoader: DomainImageLoader

    private lateinit var adapter: ClothesDetailAdapter
    private lateinit var adapterFilter: CollectionsFilterAdapter

    private lateinit var shopAvatarShapeableImageView: ShapeableImageView
    private lateinit var shopShortNameTextView: TextView
    private lateinit var shopNameTextView: TextView
    private lateinit var followersCountTextView: TextView
    private lateinit var followingsCountTextView: TextView
    private lateinit var followTextView: TextView
    private lateinit var unFollowTextView: TextView
    private lateinit var alreadyFollowedTextView: TextView
    private lateinit var writeMessageTextView: TextView
    private lateinit var filterListRecyclerView: RecyclerView
    private lateinit var clothesRecyclerView: RecyclerView

    companion object {
        const val PROFILE_ID_KEY = "profileId"
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
        adapterFilter = CollectionsFilterAdapter()
        adapterFilter.setOnClickListener(listener = this)

        adapter = ClothesDetailAdapter()
        adapter.setOnClickListener(listener = this)

    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        findNavController().navigate(R.id.clothesDetailFragment)
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
        filterListRecyclerView = fragment_shop_profile_filter_recycler_view
        clothesRecyclerView = fragment_shop_profile_clothes_recycler_view

        filterListRecyclerView.adapter = adapterFilter
        clothesRecyclerView.adapter = adapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getProfile(
            token = currentActivity.getTokenFromSharedPref(),
            id = getIdFromArgs()
        )
        presenter.getTypes(token = currentActivity.getTokenFromSharedPref())
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

        shopNameTextView.text = userModel.username
        followersCountTextView.text = userModel.followersCount.toString()
        followingsCountTextView.text = userModel.followingsCount.toString()
        fragment_shop_profile_toolbar.toolbar_title_text_view.text = userModel.username
    }

    override fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>) {
        val filterList = ArrayList<CollectionFilterModel>()

        filterList.add(CollectionFilterModel(id = 1, name = "Фильтр"))
        filterList.add(CollectionFilterModel(id = 2, name = "Образы"))
        filterList.add(CollectionFilterModel(id = 3, name = "Все позиции"))

        resultsModel.results.map {
            filterList.add(
                CollectionFilterModel(id = it.id, name = it.title)
            )
        }

        adapterFilter.updateList(filterList)
    }

    private fun getIdFromArgs(): Int = arguments?.getInt(PROFILE_ID_KEY) ?: 0
}