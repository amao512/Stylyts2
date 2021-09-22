package kz.eztech.stylyts.search.presentation.search.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.data.constants.SharedConstants
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.search.presentation.search.ui.adapters.SearchViewPagerAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.search.presentation.search.contracts.SearchContract
import kz.eztech.stylyts.global.presentation.common.interfaces.SearchListener
import kz.eztech.stylyts.search.presentation.search.viewModels.SearchViewModel
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import org.koin.android.ext.android.inject

class SearchFragment : BaseFragment<MainActivity>(), SearchContract.View,
    View.OnClickListener, SearchListener {

    private val searchViewModel: SearchViewModel by inject()

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
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.hide()
            toolbar_title_text_view.hide()
            toolbar_right_corner_action_image_button.show()

            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.setOnClickListener {
                findNavController().navigate(R.id.action_clothesDetailFragment_to_nav_ordering)
            }

            background = ContextCompat.getDrawable(context, R.color.toolbar_bg_gray)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        searchViewPagerAdapter = SearchViewPagerAdapter(fragment = this)

        fragment_search_view_pager.isSaveEnabled = false
    }

    override fun initializeListeners() {
        fragment_search_cancel_text_view.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        onSearchListener()
    }

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

    }

    private fun onSearchListener() {
        fragment_search_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.onSearch(query = query.orEmpty())

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel.onSearch(query = newText.orEmpty())

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