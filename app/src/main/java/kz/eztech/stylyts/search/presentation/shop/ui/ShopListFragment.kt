package kz.eztech.stylyts.search.presentation.shop.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search_item.*
import kotlinx.android.synthetic.main.fragment_shop_list.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.collections.presentation.ui.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.global.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.global.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.global.presentation.common.enums.GenderEnum
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.global.presentation.filter.ui.FilterDialog
import kz.eztech.stylyts.profile.presentation.profile.ui.ShopProfileFragment
import kz.eztech.stylyts.search.presentation.shop.contracts.ShopListContract
import kz.eztech.stylyts.search.presentation.shop.data.models.ShopListItem
import kz.eztech.stylyts.search.presentation.shop.presenters.ShopListPresenter
import kz.eztech.stylyts.search.presentation.shop.ui.adapters.ShopAdapter
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class ShopListFragment : BaseFragment<MainActivity>(), ShopListContract.View, View.OnClickListener,
    UniversalViewClickListener {

    @Inject
    lateinit var presenter: ShopListPresenter

    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var shopAdapter: ShopAdapter
    private lateinit var filterDialog: FilterDialog

    private lateinit var filterRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var shopsRecyclerView: RecyclerView
    private lateinit var filterModel: SearchFilterModel

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
            itemClickListener = this,
            gender = getCurrentGenderFromArgs(),
            isShowWardrobe = false,
            isShowDiscount = false
        )

        filterModel = SearchFilterModel()
        filterModel.isBrand = true
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
        presenter.getFilterList()
        presenter.getShops()

        handleSearchView()
        handleRecyclerView()
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
            R.id.toolbar_right_corner_action_image_button -> findNavController().navigate(R.id.action_shopListFragment_to_nav_ordering)
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

    override fun getCurrendId(): Int = currentActivity.getUserIdFromSharedPref()

    override fun getSearchFilter(): SearchFilterModel = filterModel

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processShops(state.data)
            is Paginator.State.NewPageProgress<*> -> processShops(state.data)
            else -> {
            }
        }

        hideProgress()
    }

    override fun processShops(list: List<Any?>) {
        list.map { it!! }.let {
            shopAdapter.updateList(list = it)
        }
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

    override fun processFilterList(filterList: List<CollectionFilterModel>) {
        filterAdapter.updateList(list = filterList)
    }

    private fun handleSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchShop(username = query.orEmpty())

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchShop(username = newText.orEmpty())

                return false
            }
        })
    }

    private fun handleRecyclerView() {
        shopsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!shopsRecyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMorePage()
                }
            }
        })
    }

    private fun searchShop(username: String) {
        shopAdapter.clearList()
        filterModel.query = username

        getShops()
    }

    private fun getShops() {
        presenter.getShops()

        if (filterModel.query.isNotBlank()) {
            fragment_shop_list_right_character_list_linear_layout.hide()
        }
    }

    private fun onFilterClicked(
        item: CollectionFilterModel,
        position: Int
    ) {
        when (position) {
//            0 -> filterDialog.show(childFragmentManager, EMPTY_STRING)
            0 -> filterAdapter.onChooseItem(position, isDisabledFirstPosition = false)
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