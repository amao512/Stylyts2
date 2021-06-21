package kz.eztech.stylyts.presentation.fragments.order

import android.view.View
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_shop_order_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.ShopOrderClothesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.show

class ShopOrderDetailFragment : BaseFragment<MainActivity>(), EmptyContract.View, UniversalViewClickListener {

    private lateinit var shopOrderClothesAdapter: ShopOrderClothesAdapter

    override fun getLayoutId(): Int = R.layout.fragment_shop_order_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_shop_order_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(
                toolbar = this,
                title = getString(R.string.order_number_text_format, "0")
            )
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        shopOrderClothesAdapter = ShopOrderClothesAdapter()
        shopOrderClothesAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {}

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onViewClicked(view: View, position: Int, item: Any?) {

    }
}