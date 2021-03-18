package kz.eztech.stylyts.presentation.fragments.main.search

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.SearchViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.search.SearchContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.SearchListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class SearchFragment : BaseFragment<MainActivity>(), SearchContract.View,
    UniversalViewClickListener, View.OnClickListener, SearchListener {

    private lateinit var searchViewPagerAdapter: SearchViewPagerAdapter

    override fun onResume() {
        super.onResume()

        initTab()
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
            itemClickListener = this,
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

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_search_cancel_text_view -> findNavController().navigateUp()
        }
    }

    private fun initTab() {
        fragment_search_view_pager.adapter = searchViewPagerAdapter
        TabLayoutMediator(fragment_search_tab_layout, fragment_search_view_pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.search_item_accounts)
                1 -> tab.text = getString(R.string.search_item_shops)
                2 -> tab.text = getString(R.string.search_item_items)
            }
        }.attach()
    }

    override fun onQuery(query: (String) -> Unit) {
        fragment_search_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query(query.toString())

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                query(newText.toString())

                return false
            }
        })
    }
}
