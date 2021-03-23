package kz.eztech.stylyts.presentation.fragments.main.users

import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_user_subs.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.UserSubViewPagerAdapter
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class UserSubsFragment : BaseFragment<MainActivity>(),EmptyContract.View {

    private lateinit var adapter: UserSubViewPagerAdapter

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

    override fun getLayoutId(): Int = R.layout.fragment_user_subs

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_user_subs){
            toolbar_left_corner_action_image_button.show()
            toolbar_back_text_view.show()
            toolbar_title_text_view.show()
            toolbar_right_corner_action_image_button.hide()

            customizeActionToolBar(this,"ruslan")
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        adapter = UserSubViewPagerAdapter(this)
        view_pager_fragment_user_subs.isSaveEnabled = false
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}