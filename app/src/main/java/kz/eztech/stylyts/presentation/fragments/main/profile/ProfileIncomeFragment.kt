package kz.eztech.stylyts.presentation.fragments.main.profile

import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile_income.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileIncomeContract
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class ProfileIncomeFragment : BaseFragment<MainActivity>(),ProfileIncomeContract.View {

    override fun getLayoutId(): Int = R.layout.fragment_profile_income

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_income){
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_title_text_view.hide()
            toolbar_right_corner_action_image_button.hide()

            customizeActionToolBar(toolbar = this)
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {}

    override fun initializeListeners() {
        include_item_profile_income_dates.setOnClickListener {
            findNavController().navigate(R.id.action_profileIncomeFragment_to_profileIncomeDetailFragment)
        }
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}