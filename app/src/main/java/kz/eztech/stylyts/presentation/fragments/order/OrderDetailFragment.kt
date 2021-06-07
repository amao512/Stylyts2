package kz.eztech.stylyts.presentation.fragments.order

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.OrderAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.OrderDetailContract
import kz.eztech.stylyts.presentation.presenters.ordering.OrderDetailPresenter
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class OrderDetailFragment : BaseFragment<MainActivity>(), OrderDetailContract.View {

    @Inject lateinit var presenter: OrderDetailPresenter
    private lateinit var clothesAdapter: OrderAdapter

    private lateinit var recyclerView: RecyclerView

    companion object {
        const val ORDER_ID_KEY = "orderId"
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_order_detail_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.order_number_text_format))
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
        clothesAdapter = OrderAdapter()
    }

    override fun initializeViews() {
        recyclerView = fragment_order_detail_recycler_view
        recyclerView.adapter = clothesAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getOrderById(
            token = currentActivity.getTokenFromSharedPref(),
            orderId = arguments?.getInt(ORDER_ID_KEY) ?: 0
        )
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processOrder(orderModel: OrderModel) {
        fragment_order_detail_toolbar.toolbar_title_text_view.text = getString(
            R.string.order_number_text_format,
            orderModel.id.toString()
        )

        clothesAdapter.updateList(list = orderModel.itemObjects)
    }
}