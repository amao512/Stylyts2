package kz.eztech.stylyts.presentation.fragments.order

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_order_list.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.common.PageFilterModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.ShopOrderAdapter
import kz.eztech.stylyts.presentation.adapters.ordering.UserOrderAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.OrderListContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.ordering.OrderListPresenter
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class OrderListFragment : BaseFragment<MainActivity>(), OrderListContract.View,
    SwipeRefreshLayout.OnRefreshListener, UniversalViewClickListener {

    @Inject lateinit var presenter: OrderListPresenter
    private lateinit var userOrderAdapter: UserOrderAdapter
    private lateinit var shopOrderAdapter: ShopOrderAdapter
    private lateinit var pageFilterModel: PageFilterModel

    private lateinit var recyclerView: RecyclerView

    override fun getLayoutId(): Int = R.layout.fragment_order_list

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_order_list_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.order_list_title))
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
        pageFilterModel = PageFilterModel()
        userOrderAdapter = UserOrderAdapter()
        userOrderAdapter.setOnClickListener(listener = this)

        shopOrderAdapter = ShopOrderAdapter()
        shopOrderAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recyclerView = fragment_order_list_recycler_view
    }

    override fun initializeListeners() {
        fragment_order_list_swipe_refresh_layout.setOnRefreshListener(this)
    }

    override fun processPostInitialization() {
        getOrders()
        handleRecyclerView()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_order_list_swipe_refresh_layout.isRefreshing = true
    }

    override fun hideProgress() {
        fragment_order_list_swipe_refresh_layout.isRefreshing = false
    }

    override fun onRefresh() {
        pageFilterModel.page = 1
        pageFilterModel.isLastPage = false
        userOrderAdapter.clearList()
        shopOrderAdapter.clearList()

        getOrders()
    }

    override fun processUserOrders(resultsModel: ResultsModel<OrderModel>) {
        recyclerView.adapter = userOrderAdapter
        userOrderAdapter.updateMoreList(list = resultsModel.results)

        if (resultsModel.totalPages != pageFilterModel.page) {
            pageFilterModel.page++
        } else {
            pageFilterModel.isLastPage = true
        }
    }

    override fun processShopOrders(resultsModel: ResultsModel<OrderModel>) {
        recyclerView.adapter = shopOrderAdapter
        shopOrderAdapter.updateMoreList(list = resultsModel.results.filter {
            it.seller.id == currentActivity.getUserIdFromSharedPref()
        })

        if (resultsModel.totalPages != pageFilterModel.page) {
            pageFilterModel.page++
        } else {
            pageFilterModel.isLastPage = true
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.item_order_detail_linear_layout -> navigateToUserOrderDetails(item as OrderModel)
            R.id.item_shop_order_root_view -> navigateToShopOrderDetails(item as OrderModel)
        }
    }

    private fun navigateToUserOrderDetails(orderModel: OrderModel) {
        val bundle = Bundle()
        bundle.putInt(UserOrderDetailFragment.ORDER_ID_KEY, orderModel.id)

        findNavController().navigate(R.id.action_orderListFragment_to_orderDetailFragment, bundle)
    }

    private fun navigateToShopOrderDetails(orderModel: OrderModel) {

    }

    private fun getOrders() {
        presenter.getOrderList(
            token = currentActivity.getTokenFromSharedPref(),
            pageFilterModel = pageFilterModel
        )
    }

    private fun handleRecyclerView() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!pageFilterModel.isLastPage) {
                        getOrders()
                    }
                }
            }
        })
    }
}