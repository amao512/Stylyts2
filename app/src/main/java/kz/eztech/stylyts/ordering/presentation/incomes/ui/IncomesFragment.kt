package kz.eztech.stylyts.ordering.presentation.incomes.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_incomes.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.IncomesAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.ordering.presentation.incomes.contracts.IncomeContract
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.INCOME_TYPE
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeListItem
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomesItem
import kz.eztech.stylyts.ordering.presentation.incomes.presenters.IncomesPresenter
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class IncomesFragment : BaseFragment<MainActivity>(), IncomeContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: IncomesPresenter
    private lateinit var incomesAdapter: IncomesAdapter

    private lateinit var refreshLayout: TwinklingRefreshLayout

    override fun getLayoutId(): Int = R.layout.fragment_incomes

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_income) {
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
        refreshLayout = fragment_incomes_refresh_layout
        refreshLayout.setHeaderView(ProgressLayout(requireContext()))
        refreshLayout.setBottomView(LoadingView(requireContext()))

        fragment_incomes_recycler_view.adapter = incomesAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getReferrals()
        handleRefreshLayout()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        refreshLayout.startRefresh()
    }

    override fun hideProgress() {
        refreshLayout.finishRefreshing()
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is IncomeListItem -> navigateToIncome(item)
        }
    }

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> {
                processReferrals(state.data)
                refreshLayout.finishRefreshing()
            }
            is Paginator.State.NewPageProgress<*> -> {
                processReferrals(state.data)
                refreshLayout.finishLoadmore()
            }
            else -> {
                hideProgress()
                refreshLayout.finishLoadmore()
            }
        }
    }

    override fun processReferrals(list: List<Any?>) {
        val incomes: MutableList<IncomesItem> = mutableListOf()
        var referralCost = 0

        list.map { it!! }.map { referral ->
            referral as IncomesItem

            if (referral.type == INCOME_TYPE) {
                referralCost += (referral as IncomeListItem).getReferralList().sumBy { it.totalProfit }
            }

            incomes.add(referral)
        }

        fragment_incomes_referral_cost_text_view.text = getString(
            R.string.price_tenge_text_format,
            referralCost.toString()
        )
        incomesAdapter.updateList(incomes)
    }

    private fun navigateToIncome(income: IncomeListItem) {
        val bundle = Bundle()
        bundle.putParcelable(IncomeDetailFragment.REFERRAL_KEY, income)

        findNavController().navigate(
            R.id.action_profileIncomeFragment_to_profileIncomeDetailFragment,
            bundle
        )
    }

    private fun handleRefreshLayout() {
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)

                refreshLayout?.startRefresh()
                incomesAdapter.clearList()
                presenter.getReferrals()
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)

                refreshLayout?.startLoadMore()
                presenter.loadMorePage()
            }
        })
    }
}