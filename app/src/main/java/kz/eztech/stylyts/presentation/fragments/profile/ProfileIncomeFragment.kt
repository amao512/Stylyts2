package kz.eztech.stylyts.presentation.fragments.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile_income.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.income.IncomeModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.incomes.IncomesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.profile.ProfileIncomeContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.profile.IncomesPresenter
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ProfileIncomeFragment : BaseFragment<MainActivity>(), ProfileIncomeContract.View, UniversalViewClickListener {

    @Inject lateinit var presenter: IncomesPresenter
    private lateinit var incomesAdapter: IncomesAdapter

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

    override fun initializeViewsData() {
        incomesAdapter = IncomesAdapter()
        incomesAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        fragment_profile_income_recycler_view.adapter = incomesAdapter

        val testList: MutableList<IncomeModel> = mutableListOf()

        testList.add(
            IncomeModel(id = 1, date = "19 July - 25 July", price = 1000)
        )
        testList.add(
            IncomeModel(id = 1, date = "26 July - 1 August", price = 100)
        )
        testList.add(
            IncomeModel(id = 1, date = "2 August - 8 August", price = 15000)
        )

        incomesAdapter.updateList(list = testList)
    }

    override fun initializeListeners() {}

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
    ) {
        when (item) {
            is IncomeModel -> findNavController().navigate(R.id.action_profileIncomeFragment_to_profileIncomeDetailFragment)
        }
    }
}