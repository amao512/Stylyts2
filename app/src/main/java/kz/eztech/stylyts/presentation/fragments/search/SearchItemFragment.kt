package kz.eztech.stylyts.presentation.fragments.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_search_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.clothes.ClothesDetailAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.UserSearchAdapter
import kz.eztech.stylyts.presentation.adapters.search.ShopsSearchAdapter
import kz.eztech.stylyts.presentation.adapters.search.UserSearchHistoryAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.search.SearchItemContract
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.fragments.shop.ShopProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.search.SearchItemPresenter
import kz.eztech.stylyts.presentation.presenters.search.SearchViewModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.Paginator
import org.koin.android.ext.android.inject
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 17.03.2021.
 */
class SearchItemFragment(
    private val position: Int
) : BaseFragment<MainActivity>(), SearchItemContract.View, UniversalViewClickListener {

    private val searchViewModel: SearchViewModel by inject()

    @Inject lateinit var presenter: SearchItemPresenter
    private lateinit var userSearchAdapter: UserSearchAdapter
    private lateinit var userSearchHistoryAdapter: UserSearchHistoryAdapter
    private lateinit var clothesAdapter: ClothesDetailAdapter
    private lateinit var shopsAdapter: ShopsSearchAdapter
    private lateinit var filterModel: SearchFilterModel

    private var isHistory: Boolean = true

    companion object {
        const val USERS_POSITION = 0
        const val SHOPS_POSITION = 1
        const val CLOTHES_POSITION = 2
    }

    override fun getLayoutId(): Int = R.layout.fragment_search_item

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() = presenter.attach(view = this)

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        userSearchAdapter = UserSearchAdapter()
        userSearchAdapter.setOnClickListener(listener = this)

        userSearchHistoryAdapter = UserSearchHistoryAdapter()
        userSearchHistoryAdapter.setOnClickListener(listener = this)

        clothesAdapter = ClothesDetailAdapter()
        clothesAdapter.setOnClickListener(listener = this)

        shopsAdapter = ShopsSearchAdapter()
        shopsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        filterModel = SearchFilterModel()
        initializeAdapter()
    }

    override fun initializeListeners() {
        searchViewModel.queryLiveData.observe(viewLifecycleOwner, {
            filterModel.query = it
            currentActivity.removeFromSharedPrefByKey(SharedConstants.QUERY_KEY)
            userSearchAdapter.clearList()
            clothesAdapter.clearList()
            shopsAdapter.clearList()

            if (filterModel.query.isNotBlank()) {
                onSearch(filterModel.query)
                isHistory = false
            } else if (filterModel.query.isBlank() && position == 0) {
                presenter.getUserFromLocaleDb()
                isHistory = true
            } else {
                onSearch(filterModel.query)
            }

            initializeAdapter()
        })
    }

    override fun processPostInitialization() {
        onSearch(query = currentActivity.getSharedPrefByKey(SharedConstants.QUERY_KEY) ?: EMPTY_STRING)
        handleRecyclerView()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun getCurrentPosition(): Int = position

    override fun getSearchFilter(): SearchFilterModel = filterModel

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processList(state.data)
            is Paginator.State.NewPageProgress<*> -> processList(state.data)
            else -> {}
        }

        hideProgress()
    }

    override fun processList(list: List<Any?>) {
        list.map { it!! }.let {
            when (position) {
                USERS_POSITION -> userSearchAdapter.updateList(
                    list = it.filter { user ->
                        user as UserModel

                        user.id != currentActivity.getUserIdFromSharedPref() && !user.isBrand
                    }
                )
                SHOPS_POSITION -> shopsAdapter.updateList(
                    list = it.filter { shop ->
                        shop as UserModel

                        shop.id != currentActivity.getUserIdFromSharedPref() && shop.isBrand
                    }
                )
                CLOTHES_POSITION -> clothesAdapter.updateList(list = it)
            }
        }
    }

    override fun processUserFromLocalDb(userList: List<UserSearchEntity>) {
        if (position == 0) {
            userSearchHistoryAdapter.updateList(list = userList)
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.linear_layout_item_user_info_container -> navigateToUserProfile(item)
            R.id.item_user_info_remove_image_view -> onRemoveUserFromHistory(item)
            R.id.item_clothes_detail_linear_layout -> navigateToClothes(item)
            R.id.item_shop_root_view_linear_layout -> navigateToShop(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        currentActivity.saveSharedPrefByKey(SharedConstants.QUERY_KEY, filterModel.query)
    }

    private fun handleRecyclerView() {
        fragment_search_item_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!fragment_search_item_recycler_view.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMorePage()
                }
            }
        })
    }

    private fun onSearch(query: String) {
        if (query.isBlank() && position == 0) {
            presenter.getUserFromLocaleDb()
            isHistory = true
        } else {
            filterModel.query = query
            presenter.getList()
        }
    }

    private fun navigateToUserProfile(item: Any?) {
        val bundle = Bundle()

        when (item) {
            is UserModel -> {
                bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.id)
                presenter.saveUserToLocaleDb(user = item)
            }
            is UserSearchEntity -> bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.id ?: 0)
        }

        findNavController().navigate(R.id.nav_profile, bundle)
    }

    private fun navigateToClothes(item: Any?) {
        item as ClothesModel

        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

        findNavController().navigate(
            R.id.action_searchFragment_to_clothesDetailFragment,
            bundle
        )
    }

    private fun navigateToShop(item: Any?) {
        item as UserModel

        val bundle = Bundle()
        bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, item.id)

        findNavController().navigate(R.id.nav_shop_profile, bundle)
    }

    private fun onRemoveUserFromHistory(item: Any?) {
        presenter.deleteUserFromLocalDb(user = item as UserSearchEntity)
    }

    private fun initializeAdapter() {
        when (position) {
            USERS_POSITION -> {
                fragment_search_item_recycler_view.layoutManager = LinearLayoutManager(requireContext())
                fragment_search_item_recycler_view.adapter = when (isHistory) {
                    true -> userSearchHistoryAdapter
                    false -> userSearchAdapter
                }
            }
            SHOPS_POSITION -> {
                fragment_search_item_recycler_view.layoutManager = LinearLayoutManager(requireContext())
                fragment_search_item_recycler_view.adapter = shopsAdapter
            }
            else -> {
                fragment_search_item_recycler_view.layoutManager = GridLayoutManager(requireContext(), 2)
                fragment_search_item_recycler_view.adapter = clothesAdapter
            }
        }
    }
}