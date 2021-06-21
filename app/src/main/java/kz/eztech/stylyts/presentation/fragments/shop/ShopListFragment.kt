package kz.eztech.stylyts.presentation.fragments.shop

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_shop_list.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.shop.ShopListItem
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.shop.ShopAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.shop.ShopListContract
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.ShopListPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ShopListFragment : BaseFragment<MainActivity>(), ShopListContract.View, View.OnClickListener,
    UniversalViewClickListener {

    @Inject lateinit var presenter: ShopListPresenter

    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var shopAdapter: ShopAdapter
    private lateinit var filterDialog: FilterDialog

    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var shopsRecyclerView: RecyclerView

    override fun getLayoutId(): Int = R.layout.fragment_shop_list

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_shop_list_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@ShopListFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.setOnClickListener(this@ShopListFragment)
            toolbar_right_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.search_item_shops)
            toolbar_title_text_view.show()

            toolbar_bottom_border_view.hide()
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
        filterAdapter = CollectionsFilterAdapter()
        filterAdapter.setOnClickListener(listener = this)

        shopAdapter = ShopAdapter()
        shopAdapter.setOnClickListener(listener = this)

        filterDialog = FilterDialog.getNewInstance(
            token = currentActivity.getTokenFromSharedPref(),
            itemClickListener = this,
            gender = getCurrentGenderFromArgs(),
            isShowWardrobe = false,
            isShowDiscount = false
        )
    }

    override fun initializeViews() {
        filterRecyclerView = fragment_shop_list_filter_recycler_view
        filterRecyclerView.adapter = filterAdapter

        searchView = fragment_chats_search_view
        shopsRecyclerView = fragment_shop_list_recycler_view
        shopsRecyclerView.adapter = shopAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        filterAdapter.updateList(list = getFilterList())
        presenter.getShops(
            token = currentActivity.getTokenFromSharedPref(),
            currentId = currentActivity.getUserIdFromSharedPref()
        )

        handleSearchView()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.toolbar_right_corner_action_image_button -> findNavController().navigate(R.id.nav_ordering)
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is CollectionFilterModel -> onFilterClicked(item, position)
            is ShopListItem -> navigateToShopFragment(item)
        }
    }

    override fun processShops(shopList: List<ShopListItem>) {
        shopAdapter.updateMoreList(list = shopList)
    }

    override fun processCharacter(character: List<String>) {
        fragment_shop_list_right_character_list_linear_layout.show()
        fragment_shop_list_right_character_list_linear_layout.removeAllViews()

        character.map {
            val textView = TextView(requireContext())
            textView.text = it
            textView.gravity = Gravity.CENTER

            fragment_shop_list_right_character_list_linear_layout.addView(textView)
        }
    }

    private fun getFilterList(): List<CollectionFilterModel> {
        val filterList: MutableList<CollectionFilterModel> = mutableListOf()

        filterList.add(
            CollectionFilterModel(
                id = 1,
                name = getString(R.string.filter_list_filter),
                icon = R.drawable.ic_filter
            )
        )

        filterList.add(
            CollectionFilterModel(
                id = 2,
                name = getString(R.string.filter_favorite_brands),
                icon = R.drawable.ic_baseline_favorite_border_24
            )
        )

        return filterList
    }

    private fun handleSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchShop(username = query ?: EMPTY_STRING)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchShop(username = newText ?: EMPTY_STRING)

                return false
            }
        })
    }

    private fun searchShop(username: String) {
        shopAdapter.clearList()

        if (username.isBlank()) {
            presenter.getShops(
                token = currentActivity.getTokenFromSharedPref(),
                currentId = currentActivity.getUserIdFromSharedPref()
            )
        } else {
            presenter.searchShop(
                token = currentActivity.getTokenFromSharedPref(),
                username = username
            )
            fragment_shop_list_right_character_list_linear_layout.hide()
        }
    }

    private fun onFilterClicked(
        item: CollectionFilterModel,
        position: Int
    ) {
        when (position) {
            0 -> filterDialog.show(childFragmentManager, EMPTY_STRING)
            1 -> filterAdapter.onChooseItem(position, isDisabledFirstPosition = false)
        }
    }

    private fun navigateToShopFragment(item: ShopListItem) {
        val bundle = Bundle()

        bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, item.id)

        findNavController().navigate(R.id.nav_shop_profile, bundle)
    }

    private fun getCurrentGenderFromArgs(): String {
        return when (arguments?.getInt(ShopCategoryListFragment.CLOTHES_TYPE_GENDER)) {
            0 -> GenderEnum.MALE.gender
            else -> GenderEnum.FEMALE.gender
        }
    }
}