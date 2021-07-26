package kz.eztech.stylyts.presentation.fragments.incomes

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
import kz.eztech.stylyts.domain.models.income.IncomeModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.incomes.IncomeListItem
import kz.eztech.stylyts.presentation.adapters.incomes.IncomesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.incomes.IncomeContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.incomes.IncomesPresenter
import kz.eztech.stylyts.presentation.utils.Paginator
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class IncomesFragment : BaseFragment<MainActivity>(), IncomeContract.View,
    UniversalViewClickListener {

    @Inject
    lateinit var presenter: IncomesPresenter
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
            is IncomeModel -> navigateToIncome(item)
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
        val incomes: MutableList<IncomeListItem> = mutableListOf()
        var referralCost: Int = 0

        list.map { it!! }.let {
            it.map { referral ->
                referral as ReferralModel

                referralCost += referral.referralCost

                val income = IncomeModel(
                    id = referral.id,
                    date = referral.createdAt,
                    price = referral.referralCost
                )

                incomes.add(IncomeListItem(data = income))
            }
        }

        fragment_incomes_referral_cost_text_view.text = getString(
            R.string.price_tenge_text_format,
            referralCost.toString()
        )
        incomesAdapter.updateList(incomes)
    }

    private fun navigateToIncome(income: IncomeModel) {
        val bundle = Bundle()
        bundle.putInt(IncomeDetailFragment.REFERRAL_ID_KEY, income.id)

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