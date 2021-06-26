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
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.ShopOrderAdapter
import kz.eztech.stylyts.presentation.adapters.ordering.UserOrderAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.OrderListContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.ordering.OrderListPresenter
import kz.eztech.stylyts.presentation.utils.Paginator
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class OrderListFragment : BaseFragment<MainActivity>(), OrderListContract.View,
    SwipeRefreshLayout.OnRefreshListener, UniversalViewClickListener {

    @Inject lateinit var presenter: OrderListPresenter
    private lateinit var userOrderAdapter: UserOrderAdapter
    private lateinit var shopOrderAdapter: ShopOrderAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

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
        userOrderAdapter = UserOrderAdapter()
        userOrderAdapter.setOnClickListener(listener = this)

        shopOrderAdapter = ShopOrderAdapter()
        shopOrderAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recyclerView = fragment_order_list_recycler_view

        if (currentActivity.getIsBrandFromSharedPref()) {
            recyclerView.adapter = shopOrderAdapter
        } else {
            recyclerView.adapter = userOrderAdapter
        }
    }

    override fun initializeListeners() {
        fragment_order_list_swipe_refresh_layout.setOnRefreshListener(this)
    }

    override fun processPostInitialization() {
        presenter.getOrders()
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
        userOrderAdapter.clearList()
        shopOrderAdapter.clearList()
        presenter.getOrders()
    }

    override fun getToken(): String = currentActivity.getTokenFromSharedPref()

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processOrders(state.data)
            is Paginator.State.NewPageProgress<*> -> processOrders(state.data)
            else -> hideProgress()
        }
    }

    override fun processOrders(list: List<Any?>) {
        list.map { it!! }.let {
            if (currentActivity.getIsBrandFromSharedPref()) {
                shopOrderAdapter.updateList(list = it.filter { order ->
                    (order as OrderModel).seller.id == currentActivity.getUserIdFromSharedPref()
                })
            } else {
                userOrderAdapter.updateList(list = it)
            }
        }

        hideProgress()
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
        val bundle = Bundle()
        bundle.putInt(ShopOrderDetailFragment.ORDER_ID_KEY, orderModel.id)

        findNavController().navigate(R.id.action_orderListFragment_to_shopOrderDetailFragment, bundle)
    }

    private fun handleRecyclerView() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMorePage()
                }
            }
        })
    }
}