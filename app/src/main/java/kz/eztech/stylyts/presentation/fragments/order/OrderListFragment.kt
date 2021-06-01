package kz.eztech.stylyts.presentation.fragments.order

import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_order_list.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.OrderListContract
import kz.eztech.stylyts.presentation.presenters.ordering.OrderListPresenter
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class OrderListFragment : BaseFragment<MainActivity>(), OrderListContract.View,
    View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var presenter: OrderListPresenter

    override fun getLayoutId(): Int = R.layout.fragment_order_list

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_order_list_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@OrderListFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.order_list_title)
            toolbar_title_text_view.show()
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
        fragment_order_list_swipe_refresh_layout.setOnRefreshListener(this)
    }

    override fun processPostInitialization() {
        presenter.getOrderList(token = currentActivity.getTokenFromSharedPref())
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
        }
    }

    override fun onRefresh() {
        presenter.getOrderList(token = currentActivity.getTokenFromSharedPref())
    }

    override fun processOrderList(resultsModel: ResultsModel<OrderModel>) {
        resultsModel.results.map {
            Log.d("TAG4", "order - $it")
        }
    }
}