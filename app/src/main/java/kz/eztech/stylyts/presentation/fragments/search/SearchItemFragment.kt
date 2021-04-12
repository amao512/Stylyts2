package kz.eztech.stylyts.presentation.fragments.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.search.UserSearchEntity
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.UserSearchHistoryAdapter
import kz.eztech.stylyts.presentation.adapters.clothes.ClothesDetailAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.UserSearchAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.search.SearchItemContract
import kz.eztech.stylyts.presentation.fragments.collection.ItemDetailFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.search.SearchItemPresenter
import kz.eztech.stylyts.presentation.presenters.search.SearchViewModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
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

    private var query: String = EMPTY_STRING
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
        userSearchAdapter.itemClickListener = this

        userSearchHistoryAdapter = UserSearchHistoryAdapter()
        userSearchHistoryAdapter.itemClickListener = this

        clothesAdapter = ClothesDetailAdapter()
        clothesAdapter.itemClickListener = this
    }

    override fun initializeViews() {
        initializeAdapter()
    }

    override fun initializeListeners() {
        searchViewModel.queryLiveData.observe(viewLifecycleOwner, {
            query = it
            currentActivity.removeFromSharedPrefByKey(SharedConstants.QUERY_KEY)

            processPostInitialization()
        })
    }

    override fun processPostInitialization() {
        val oldQuery = currentActivity.getSharedPrefByKey(SharedConstants.QUERY_KEY) ?: EMPTY_STRING
        userSearchAdapter.clearList()
        clothesAdapter.clearList()

        if (query.isBlank() && oldQuery.isNotEmpty()) {
            onSearch(oldQuery)
            initializeAdapter()
        } else if (query.isBlank() && position == 0) {
            presenter.getUserFromLocaleDb()
            isHistory = true
            initializeAdapter()
        } else {
            onSearch(query)
            initializeAdapter()
        }
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_search_item_recycler_view.hide()
    }

    override fun hideProgress() {
        fragment_search_item_recycler_view.show()
    }

    override fun processUserResults(resultsModel: ResultsModel<UserModel>) {
        val userList: MutableList<UserModel> = mutableListOf()
        val currentUser = currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)

        resultsModel.results.map { user ->
            if (user.id != currentUser && !user.isBrand) {
                userList.add(user)
            }
        }

        userSearchAdapter.updateList(list = userList)
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
            R.id.item_user_info_remove_image_view -> {
                presenter.deleteUserFromLocalDb(userId = (item as UserSearchEntity).id ?: 0)
                presenter.getUserFromLocaleDb()
            }
            R.id.item_clothes_detail_linear_layout -> {
                item as ClothesModel

                val bundle = Bundle()
                bundle.putInt(ItemDetailFragment.CLOTHES_ID, item.id)

                findNavController().navigate(
                    R.id.action_searchFragment_to_itemDetailFragment,
                    bundle
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        currentActivity.saveSharedPrefByKey(SharedConstants.QUERY_KEY, query)
    }

    override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
        clothesAdapter.updateList(list = resultsModel.results)
    }

    private fun onSearch(query: String) {
        when (position) {
            0 -> {
                presenter.searchUserByUsername(
                    token = getTokenFromSharedPref(),
                    username = query
                )
                isHistory = false
            }
            2 -> presenter.searchClothesByTitle(
                token = getTokenFromSharedPref(),
                title = query
            )
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

        findNavController().navigate(R.id.action_searchFragment_to_profileFragment, bundle)
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
            else -> {
                fragment_search_item_recycler_view.layoutManager = GridLayoutManager(requireContext(), 2)
                fragment_search_item_recycler_view.adapter = clothesAdapter
            }
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}