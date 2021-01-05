package kz.eztech.stylyts.presentation.fragments.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile_income.*
import kotlinx.android.synthetic.main.fragment_profile_income_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileIncomeDetailContract

class ProfileIncomeDetailFragment : BaseFragment<MainActivity>(),ProfileIncomeDetailContract.View {
    override fun getLayoutId(): Int {
        return R.layout.fragment_profile_income_detail
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_income_detail){
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            image_button_right_corner_action.visibility = android.view.View.GONE
            elevation = 0f
            customizeActionToolBar(this,"Заказ №1231231")
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