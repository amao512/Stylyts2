package kz.eztech.stylyts.presentation.fragments.main.search

import android.view.View
import kotlinx.android.synthetic.main.fragment_search_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.UserSearchAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.search.SearchItemContract
import kz.eztech.stylyts.presentation.interfaces.SearchListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.search.SearchItemPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

/**
 * Created by Asylzhan Seytbek on 17.03.2021.
 */
class SearchItemFragment(
    private val type: Int
) : BaseFragment<MainActivity>(), SearchItemContract.View, UniversalViewClickListener {

    @Inject lateinit var presenter: SearchItemPresenter

    private lateinit var userSearchAdapter: UserSearchAdapter

    private var itemClickListener: UniversalViewClickListener? = null
    private var searchListener: SearchListener? = null

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
    }

    override fun initializeViews() {
        when (type) {
            0 -> fragment_search_item_recycler_view.adapter = userSearchAdapter
            else -> {}
        }
    }

    override fun initializeListeners() {
        searchListener?.onQuery {
            if (it.isBlank()) {
                userSearchAdapter.updateList(emptyList())
            }

            presenter.getUserByUsername(
                token = currentActivity.getSharedPrefByKey(SharedConstants.TOKEN_KEY) ?: EMPTY_STRING,
                username = it
            )
        }
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {}

    override fun processUsers(list: List<ProfileModel>) {
        userSearchAdapter.updateList(list)
    }

    fun setOnClickListener(itemClickListener:UniversalViewClickListener?){
        this.itemClickListener = itemClickListener
    }

    fun setSearchListener(searchListener: SearchListener?) {
        this.searchListener = searchListener
    }
}