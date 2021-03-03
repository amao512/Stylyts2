package kz.eztech.stylyts.presentation.fragments.main.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_shop.*
import kotlinx.android.synthetic.main.fragment_user_comments.*
import kotlinx.android.synthetic.main.fragment_user_subs.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ShopViewPagerAdapter
import kz.eztech.stylyts.presentation.adapters.UserSubViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract

class UserSubsFragment : BaseFragment<MainActivity>(),EmptyContract.View {

    private lateinit var adapter: UserSubViewPagerAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_user_subs
    }

    override fun getContractView(): BaseView {
       return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_user_subs){
            image_button_left_corner_action.visibility = View.VISIBLE
            text_view_toolbar_back.visibility = View.VISIBLE
            text_view_toolbar_title.visibility = View.VISIBLE
            image_button_right_corner_action.visibility = View.GONE
            customizeActionToolBar(this,"ruslan")
        }
    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {

    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        adapter = UserSubViewPagerAdapter(this)
        view_pager_fragment_user_subs.isSaveEnabled = false
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
        view_pager_fragment_user_subs.adapter = adapter
        TabLayoutMediator(tab_bar_fragment_user_subs, view_pager_fragment_user_subs) { tab, position ->
            when(position){
                0 -> {tab.text = "250 Подписчики"}
                1 -> {tab.text = "131 Подписки"}
            }
        }.attach()
    }

    override fun initializeListeners() {

    }

    override fun processPostInitialization() {

    }

    override fun disposeRequests() {

    }

    override fun displayMessage(msg: String) {

    }

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun displayProgress() {

    }

    override fun hideProgress() {

    }
}