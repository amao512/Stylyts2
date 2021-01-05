package kz.eztech.stylyts.presentation.fragments.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.include_toolbar_profile
import kotlinx.android.synthetic.main.fragment_profile_income.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.profile.ProfileIncomeContract

class ProfileIncomeFragment : BaseFragment<MainActivity>(),ProfileIncomeContract.View {
    override fun getLayoutId(): Int {
        return R.layout.fragment_profile_income
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_income){
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.visibility = android.view.View.GONE
            image_button_right_corner_action.visibility = android.view.View.GONE
            elevation = 0f
            customizeActionToolBar(this)
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
        include_item_profile_income_dates.setOnClickListener {
            findNavController().navigate(R.id.action_profileIncomeFragment_to_profileIncomeDetailFragment)
        }
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