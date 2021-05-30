package kz.eztech.stylyts.presentation.fragments.ordering

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_ordering_data.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.order.DeliveryWayModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.DeliveryWayAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class OrderingDataFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener,
    UniversalViewClickListener {

    private lateinit var deliveryWayAdapter: DeliveryWayAdapter

    private lateinit var titleTextView: TextView
    private lateinit var cityEditText: EditText
    private lateinit var listTitleTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private var isSelectedDeliveryWay: Boolean = false

    override fun getLayoutId(): Int = R.layout.fragment_ordering_data

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_ordering_data_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@OrderingDataFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.button_ordering)
            toolbar_title_text_view.show()

            toolbar_bottom_border_view.hide()
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        deliveryWayAdapter = DeliveryWayAdapter()
        deliveryWayAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        titleTextView = fragment_ordering_data_title_text_view
        titleTextView.text = getString(R.string.delivery_data)

        cityEditText = fragment_ordering_data_city_edit_text

        listTitleTextView = fragment_ordering_data_list_title_text_view
        listTitleTextView.text = getString(R.string.delivery_title)

        recyclerView = fragment_ordering_data_recycler_view
        recyclerView.adapter = deliveryWayAdapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        deliveryWayAdapter.updateList(list = getDeliveryWayList())
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> {
                if (!isSelectedDeliveryWay) {
                    findNavController().navigateUp()
                } else {
                    enableDeliveryWays()
                }
            }
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is DeliveryWayModel -> disableDeliveryWays()
        }
    }

    private fun getDeliveryWayList(): List<DeliveryWayModel> {
        val deliveryList: MutableList<DeliveryWayModel> = mutableListOf()

        deliveryList.add(
            DeliveryWayModel(
                id = 1,
                icon = R.drawable.ic_delivery_car,
                title = getString(R.string.delivery_way_courier)
            )
        )

        deliveryList.add(
            DeliveryWayModel(
                id = 2,
                icon = R.drawable.ic_location_pin,
                title = getString(R.string.delivery_way_pickup)
            )
        )

        deliveryList.add(
            DeliveryWayModel(
                id = 3,
                icon = R.drawable.ic_mail,
                title = getString(R.string.delivery_way_post)
            )
        )

        return deliveryList
    }

    private fun getDressingWayList(): List<DeliveryWayModel> {
        val deliveryList: MutableList<DeliveryWayModel> = mutableListOf()

        deliveryList.add(
            DeliveryWayModel(
                id = 1,
                icon = R.drawable.ic_clothes_hanger,
                title = getString(R.string.dressing_way_need)
            )
        )

        deliveryList.add(
            DeliveryWayModel(
                id = 2,
                icon = R.drawable.ic_shopping_bag,
                title = getString(R.string.dressing_way_not_need)
            )
        )

        return deliveryList
    }

    private fun enableDeliveryWays() {
        isSelectedDeliveryWay = false

        titleTextView.text = getString(R.string.delivery_data)
        listTitleTextView.text = getString(R.string.delivery_title)
        deliveryWayAdapter.updateList(list = getDeliveryWayList())
    }

    private fun disableDeliveryWays() {
        isSelectedDeliveryWay = true

        titleTextView.text = getString(R.string.delivery_pickup)
        listTitleTextView.text = getString(R.string.dressing_title)
        deliveryWayAdapter.updateList(list = getDressingWayList())
    }
}