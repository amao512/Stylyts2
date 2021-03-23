package kz.eztech.stylyts.profile.presentation.fragments

import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile_income_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.profile.presentation.contracts.ProfileIncomeDetailContract
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class ProfileIncomeDetailFragment : BaseFragment<MainActivity>(), ProfileIncomeDetailContract.View {

    override fun getLayoutId(): Int = R.layout.fragment_profile_income_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_income_detail){
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_title_text_view.show()
            toolbar_right_corner_action_image_button.show()

            customizeActionToolBar(this,"Заказ №1231231")
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {}

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}