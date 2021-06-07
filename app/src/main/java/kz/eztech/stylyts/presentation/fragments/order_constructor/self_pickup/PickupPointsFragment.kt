package kz.eztech.stylyts.presentation.fragments.order_constructor.self_pickup

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_pickup_points.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.PickupPointsAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.PickupPointsContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.ordering.PickupPointsPresenter
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class PickupPointsFragment : BaseFragment<MainActivity>(), PickupPointsContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: PickupPointsPresenter
    private lateinit var pickupPointsAdapter: PickupPointsAdapter

    private lateinit var recyclerView: RecyclerView

    override fun getLayoutId(): Int = R.layout.fragment_pickup_points

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_pickup_points_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.pickup_points))
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
        pickupPointsAdapter = PickupPointsAdapter()
        pickupPointsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recyclerView = fragment_pickup_points_recycler_view
        recyclerView.adapter = pickupPointsAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is AddressModel -> {}
        }
    }

    override fun processPoints(resultsModel: ResultsModel<AddressModel>) {
        pickupPointsAdapter.updateList(list = resultsModel.results)
    }
}