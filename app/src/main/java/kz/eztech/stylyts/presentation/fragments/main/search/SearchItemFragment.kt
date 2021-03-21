package kz.eztech.stylyts.presentation.fragments.main.search

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_search_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.entities.UserSearchEntity
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.SearchModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.UserSearchAdapter
import kz.eztech.stylyts.presentation.adapters.UserSearchHistoryAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.search.SearchItemContract
import kz.eztech.stylyts.presentation.fragments.main.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.SearchListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.search.SearchItemPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 17.03.2021.
 */
class SearchItemFragment(
    private val position: Int
) : BaseFragment<MainActivity>(), SearchItemContract.View, UniversalViewClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var presenter: SearchItemPresenter
    private lateinit var userSearchAdapter: UserSearchAdapter
    private lateinit var userSearchHistoryAdapter: UserSearchHistoryAdapter

    private var searchListener: SearchListener? = null
    private var query: String = EMPTY_STRING
    private var isHistory: Boolean = true

    companion object {
        const val USERS_POSITION = 0
        const val SHOPS_POSITION = 1
        const val CLOTHES_POSITION = 2
    }

    override fun onRefresh() {
        processPostInitialization()
    }

    override fun getLayoutId(): Int = R.layout.fragment_search_item

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() = presenter.attach(view = this)

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        userSearchAdapter = UserSearchAdapter()
        userSearchAdapter.itemClickListener = this

        userSearchHistoryAdapter = UserSearchHistoryAdapter()
        userSearchHistoryAdapter.itemClickListener = this
    }

    override fun initializeViews() {
        setUserAdapter()
    }

    override fun initializeListeners() {
        searchListener?.onQuery {
            query = it
            currentActivity.removeFromSharedPrefByKey(SharedConstants.QUERY_KEY)

            processPostInitialization()
        }
    }

    override fun processPostInitialization() {
        val oldQuery = currentActivity.getSharedPrefByKey(SharedConstants.QUERY_KEY) ?: EMPTY_STRING
        userSearchAdapter.clearList()

        if (query.isBlank() && oldQuery.isNotEmpty()) {
            presenter.getUserByUsername(
                token = getTokenFromSharedPref(),
                username = oldQuery
            )
            isHistory = false
            setUserAdapter()
        } else if (query.isBlank()) {
            presenter.getUserFromLocaleDb()
            isHistory = true
            setUserAdapter()
        } else {
            presenter.getUserByUsername(
                token = getTokenFromSharedPref(),
                username = query
            )
            isHistory = false
            setUserAdapter()
        }
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_search_item_recycler_view.hide()
        fragment_search_item_swipe_refresh_layout.isRefreshing = true
    }

    override fun hideProgress() {
        fragment_search_item_recycler_view.show()
        fragment_search_item_swipe_refresh_layout.isRefreshing = false
    }

    override fun processSearch(searchModel: SearchModel<UserModel>) {
        searchModel.results?.let {
            val userList: MutableList<UserModel> = mutableListOf()

            it.map { user ->
                if (user.id != currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)) {
                    userList.add(user)
                }
            }

            userSearchAdapter.updateList(list = userList)
        }
    }

    override fun processUserFromLocalDb(userList: List<UserSearchEntity>) {
        userSearchHistoryAdapter.updateList(list = userList)
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        currentActivity.saveSharedPrefByKey(SharedConstants.QUERY_KEY, query)
    }

    fun setSearchListener(searchListener: SearchListener?) {
        this.searchListener = searchListener
    }

    private fun navigateToUserProfile(item: Any?) {
        val bundle = Bundle()

        when (item) {
            is UserModel -> {
                bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.id ?: 0)
                presenter.saveUserToLocaleDb(user = item)
            }
            is UserSearchEntity -> bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.id ?: 0)
        }

        findNavController().navigate(R.id.action_searchFragment_to_profileFragment, bundle)
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.TOKEN_KEY) ?: EMPTY_STRING
    }

    private fun setUserAdapter() {
        if (position == USERS_POSITION) {
            fragment_search_item_recycler_view.adapter = when (isHistory) {
                true -> userSearchHistoryAdapter
                false -> userSearchAdapter
            }
        }
    }
}