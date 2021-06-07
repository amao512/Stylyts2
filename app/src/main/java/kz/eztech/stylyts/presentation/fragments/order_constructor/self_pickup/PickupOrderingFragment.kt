package kz.eztech.stylyts.presentation.fragments.order_constructor.self_pickup

import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_pickup_ordering.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.utils.extensions.show

class PickupOrderingFragment : BaseFragment<MainActivity>(), EmptyContract.View {

    private lateinit var cityEditText: EditText
    private lateinit var pickupPointEditText: EditText
    private lateinit var deliveryConditionEditText: EditText
    private lateinit var deliveryDateEditText: EditText
    private lateinit var deliveryTimeTextView: TextView

    override fun getLayoutId(): Int = R.layout.fragment_pickup_ordering

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_pickup_ordering_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.button_ordering))
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        cityEditText = fragment_pickup_ordering_city_edit_text
        pickupPointEditText = fragment_pickup_ordering_points_edit_text
        deliveryConditionEditText = fragment_pickup_ordering_delivery_condition_edit_text
        deliveryDateEditText = fragment_pickup_ordering_delivery_date_edit_text
        deliveryTimeTextView = fragment_pickup_ordering_time_text_view
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
}