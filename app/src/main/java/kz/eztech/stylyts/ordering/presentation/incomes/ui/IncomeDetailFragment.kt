package kz.eztech.stylyts.ordering.presentation.incomes.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_income_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.ordering.domain.models.referrals.ReferralItemModel
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.ordering.presentation.incomes.ui.adapters.ReferralItemsAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.ordering.presentation.incomes.contracts.IncomeDetailContract
import kz.eztech.stylyts.global.presentation.clothes.ui.ClothesDetailFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.IncomeListItem
import kz.eztech.stylyts.ordering.presentation.incomes.data.models.ReferralItem
import kz.eztech.stylyts.ordering.presentation.incomes.presenters.IncomeDetailPresenter
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class IncomeDetailFragment : BaseFragment<MainActivity>(), IncomeDetailContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: IncomeDetailPresenter
    private lateinit var referralItemsAdapter: ReferralItemsAdapter

    companion object {
        const val REFERRAL_KEY = "referral"
    }

    override fun getLayoutId(): Int = R.layout.fragment_income_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_income_detail){
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            customizeActionToolBar(this)
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
        referralItemsAdapter = ReferralItemsAdapter()
        referralItemsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        fragment_income_detail_recycler_view.adapter = referralItemsAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        getIncome()?.let {
            presenter.getReferralList(incomeListItem = it)
        }
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

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
            is ReferralItemModel -> navigateToClothesDetail(item)
        }
    }

    override fun processReferralList(list: List<ReferralItem>) {
        referralItemsAdapter.updateList(list = list)
    }

    private fun navigateToClothesDetail(referralItem: ReferralItemModel) {
        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, referralItem.id)

        findNavController().navigate(
            R.id.action_profileIncomeDetailFragment_to_clothesDetailFragment,
            bundle
        )
    }

    private fun getIncome(): IncomeListItem? = arguments?.getParcelable(REFERRAL_KEY)
}