package kz.eztech.stylyts.presentation.fragments.incomes

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_income_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.ShopOrderClothesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.incomes.IncomeDetailContract
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.incomes.IncomeDetailPresenter
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class IncomeDetailFragment : BaseFragment<MainActivity>(), IncomeDetailContract.View, UniversalViewClickListener {

    @Inject lateinit var presenter: IncomeDetailPresenter
    private lateinit var clothesOrdersAdapter: ShopOrderClothesAdapter

    companion object {
        const val REFERRAL_ID_KEY = "referralId"
    }

    override fun getLayoutId(): Int = R.layout.fragment_income_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_income_detail){
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(this,"Заказ №1231231")
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
        clothesOrdersAdapter = ShopOrderClothesAdapter()
        clothesOrdersAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        fragment_income_detail_recycler_view.adapter = clothesOrdersAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getReferral(referralId = getReferralId())
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
            is ClothesModel -> navigateToClothesDetail(item)
        }
    }

    override fun processReferral(referralModel: ReferralModel) {
        presenter.getOrder(orderId = referralModel.order)

        fragment_income_detail_date_text_view.text = referralModel.createdAt
        fragment_income_detail_cost_text_view.text = getString(
            R.string.price_tenge_text_format,
            referralModel.referralCost.toString()
        )
    }

    override fun processOrder(orderModel: OrderModel) {
        clothesOrdersAdapter.updateList(list = orderModel.itemObjects)
    }

    private fun navigateToClothesDetail(clothesModel: ClothesModel) {
        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, clothesModel.id)

        findNavController().navigate(
            R.id.action_profileIncomeDetailFragment_to_clothesDetailFragment,
            bundle
        )
    }

    private fun getReferralId(): Int = arguments?.getInt(REFERRAL_ID_KEY) ?: 0
}