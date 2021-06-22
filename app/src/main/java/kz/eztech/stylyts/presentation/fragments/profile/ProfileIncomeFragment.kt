package kz.eztech.stylyts.presentation.fragments.profile

import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile_income.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.profile.ProfileIncomeContract
import kz.eztech.stylyts.presentation.presenters.profile.IncomesPresenter
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ProfileIncomeFragment : BaseFragment<MainActivity>(), ProfileIncomeContract.View {

    @Inject lateinit var presenter: IncomesPresenter

    override fun getLayoutId(): Int = R.layout.fragment_profile_income

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_income){
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            customizeActionToolBar(toolbar = this)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

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