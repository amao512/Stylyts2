package kz.eztech.stylyts.presentation.fragments.main.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.SearchViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.search.SearchContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.SearchListener
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class SearchFragment : BaseFragment<MainActivity>(), SearchContract.View,
    View.OnClickListener, SearchListener {

    private lateinit var searchViewPagerAdapter: SearchViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentActivity.removeFromSharedPrefByKey(SharedConstants.QUERY_KEY)
    }

    override fun onResume() {
        super.onResume()

        initTab()
        initializeViews()
    }

    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_search_toolbar) {
            image_button_left_corner_action.hide()
            text_view_toolbar_back.hide()
            text_view_toolbar_title.hide()
            image_button_right_corner_action.show()
            image_button_right_corner_action.setImageResource(R.drawable.ic_shop)
            image_button_right_corner_action.setOnClickListener {
                val cartDialog = CartDialog()
                cartDialog.show(childFragmentManager, "Cart")
            }
            elevation = 0f
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        searchViewPagerAdapter = SearchViewPagerAdapter(
            fragment = this,
            searchListener = this
        )

        fragment_search_view_pager.isSaveEnabled = false
    }

    override fun initializeListeners() {
        fragment_search_cancel_text_view.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_search_cancel_text_view -> findNavController().navigateUp()
        }
    }

    override fun onQuery(query: (String) -> Unit) {
        fragment_search_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query(query ?: EMPTY_STRING)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                query(newText ?: EMPTY_STRING)

                return false
            }
        })
    }

    private fun initTab() {
        fragment_search_view_pager.adapter = searchViewPagerAdapter

        TabLayoutMediator(fragment_search_tab_layout, fragment_search_view_pager) { tab, position ->
            tab.text = when (position) {
                SearchItemFragment.USERS_POSITION -> getString(R.string.search_item_accounts)
                SearchItemFragment.SHOPS_POSITION -> getString(R.string.search_item_shops)
                else -> getString(R.string.search_item_items)
            }
        }.attach()
    }
}
