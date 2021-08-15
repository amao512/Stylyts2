package kz.eztech.stylyts.ordering.presentation.order_constructor.ui.fragments.self_pickup

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_collection_constructor.*
import kotlinx.android.synthetic.main.fragment_pickup_points.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.ordering.data.models.order.DeliveryCreateApiModel
import kz.eztech.stylyts.ordering.data.models.order.OrderCreateApiModel
import kz.eztech.stylyts.global.domain.models.address.AddressModel
import kz.eztech.stylyts.ordering.domain.models.order.ShopPointModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.PickupPointsAdapter
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.ShopPointsAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.global.presentation.base.DialogChooserListener
import kz.eztech.stylyts.ordering.presentation.order_constructor.contracts.PickupPointsContract
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.dialogs.PickupPointInfoDialog
import kz.eztech.stylyts.ordering.presentation.order_constructor.enums.DeliveryTypeEnum
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.fragments.OrderingFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.ordering.presentation.order_constructor.presenters.PickupPointsPresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.getLocationFromAddress
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class PickupPointsFragment : BaseFragment<MainActivity>(), PickupPointsContract.View,
    UniversalViewClickListener, View.OnClickListener, DialogChooserListener, OnMapReadyCallback {

    @Inject
    lateinit var presenter: PickupPointsPresenter
    private lateinit var shopPointsAdapter: ShopPointsAdapter
    private lateinit var pickupPointsAdapter: PickupPointsAdapter

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView

    private lateinit var currentMap: GoogleMap
    private var orderList = ArrayList<OrderCreateApiModel>()
    private var shopList = ArrayList<ShopPointModel>()
    private var currentShopId: Int = 0

    companion object {
        const val ORDER_KEY = "order"
        const val DELIVERY_CONDITION_KEY = "deliveryCondition"
    }

    override fun getLayoutId(): Int = R.layout.fragment_pickup_points

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_pickup_points_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@PickupPointsFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.pickup_points)
            toolbar_title_text_view.show()

            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.setOnClickListener(this@PickupPointsFragment)
            if (orderList.size > 1) {
                toolbar_right_text_text_view.show()
            }
            changeNextButtonColor()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_pickup_points_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(ORDER_KEY)) {
                orderList.clear()
                it.getParcelableArrayList<OrderCreateApiModel>(ORDER_KEY)?.map { order ->
                    orderList.add(order)
                }
            }
        }
    }

    override fun initializeViewsData() {
        shopPointsAdapter = ShopPointsAdapter()
        shopPointsAdapter.setOnClickListener(listener = this)

        pickupPointsAdapter = PickupPointsAdapter()
        pickupPointsAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        searchView = fragment_pickup_points_search_view
        recyclerView = fragment_pickup_points_recycler_view
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        if (orderList.size == 1) {
            currentShopId = orderList[0].ownerId
            presenter.getPickupPoints()
        } else {
            orderList.map {
                presenter.getShop(id = it.ownerId)
            }
        }

        initializeBottomSheetBehaviorItems()
        handleRecyclerView()
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_pickup_points_progress_bar.show()
    }

    override fun hideProgress() {
        fragment_pickup_points_progress_bar.hide()
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is ShopPointModel -> onShopPointClicked(item)
            is AddressModel -> onAddressClicked(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> {
                if (recyclerView.adapter == shopPointsAdapter || orderList.size == 1) {
                    findNavController().navigateUp()
                } else {
                    recyclerView.adapter = shopPointsAdapter
                    currentMap.clear()
                    setCurrentListCondition(isPoints = false)
                }
            }
            R.id.toolbar_right_text_text_view -> onNextClick()
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        when (item) {
            is AddressModel -> onAddressChoice(item)
        }
    }

    override fun processShop(userModel: UserModel) {
        shopList.add(
            ShopPointModel(
                id = userModel.id,
                firstName = userModel.firstName,
                lastName = userModel.lastName,
                title = userModel.username,
                avatar = userModel.avatar
            )
        )
        shopPointsAdapter.updateList(list = shopList)
        setCurrentListCondition(isPoints = false)
    }

    override fun getShopId(): Int = currentShopId

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processPoints(state.data)
            is Paginator.State.NewPageProgress<*> -> processPoints(state.data)
            else -> {}
        }

        hideProgress()
    }

    override fun processPoints(list: List<Any?>) {
        if (list.isEmpty()) {
            displayMessage(msg = getString(R.string.no_addresses))

            currentMap.clear()
            pickupPointsAdapter.clearList()
        } else {
            list.map { it!! }.let {
                pickupPointsAdapter.updateList(list = it)

                it.map { item ->
                    item as AddressModel

                    val address = "${item.city}, ${item.street} ${item.house}"

                    getLocationFromAddress(
                        context = requireContext(),
                        address = address
                    )?.let { latLng ->
                        currentMap.addMarker(MarkerOptions().position(latLng).title(address))
                        currentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
                    }
                }
            }
        }

        setCurrentListCondition(isPoints = true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        currentMap = googleMap
//        val almaty = getLocationFromAddress(requireContext(), "Алматы, проспект Назарбаева 120")
//        val almaty2 = getLocationFromAddress(requireContext(), "Алматы, проспект Абая 10")
//
//        almaty?.let {
//            currentMap.addMarker(MarkerOptions().position(it).title("Алматы, проспект Назарбаева 120"))
//            currentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 12.0f))
//        }
//
//        almaty2?.let {
//            currentMap.addMarker(MarkerOptions().position(it).title("Алматы, проспект Абая 10"))
//        }
    }

    private fun onShopPointClicked(shopPointModel: ShopPointModel) {
        currentShopId = shopPointModel.id
        presenter.getPickupPoints()
    }

    private fun onAddressClicked(addressModel: AddressModel) {
        PickupPointInfoDialog.getNewInstance(
            dialogChooserListener = this,
            addressModel = addressModel,
            deliveryCondition = arguments?.getString(DELIVERY_CONDITION_KEY) ?: EMPTY_STRING
        ).show(childFragmentManager, EMPTY_STRING)
    }

    private fun onAddressChoice(addressModel: AddressModel) {
        if (orderList.size > 1) {
            val shop = shopList.find { it.title == addressModel.user }

            shop?.let {
                it.selectedAddress = getDeliveryCreateModel(addressModel)

                it.isSelected = true
                shopPointsAdapter.onSelect(id = it.id, isSelected = true)
            }

            setCurrentListCondition(isPoints = false)
        } else {
            orderList.map {
                it.delivery = getDeliveryCreateModel(addressModel)
            }

            onNavigateOrderingFragment()
        }
    }

    private fun getDeliveryCreateModel(addressModel: AddressModel): DeliveryCreateApiModel {
        return DeliveryCreateApiModel(
            city = addressModel.city,
            street = addressModel.street,
            house = addressModel.house,
            apartment = addressModel.apartment,
            deliveryType = DeliveryTypeEnum.SELF_PICKUP.type
        )
    }

    private fun onNextClick() {
        var flag = false

        orderList.map {
            flag = it.delivery != null
        }

        if (orderList.size > 1 && flag) {
            orderList.map {
                val address = shopPointsAdapter.getItemById(it.ownerId).selectedAddress

                it.delivery = address
            }

            onNavigateOrderingFragment()
        }
    }

    private fun changeNextButtonColor() {
        var flag = false

        orderList.map {
            flag = it.delivery != null
        }

        toolbar_right_text_text_view.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                when (flag) {
                    true -> R.color.app_light_orange
                    false -> R.color.app_dark_blue_gray
                }
            )
        )
    }

    private fun onNavigateOrderingFragment() {
        val bundle = Bundle()
        bundle.putParcelableArrayList(OrderingFragment.ORDER_KEY, orderList)

        findNavController().navigate(
            R.id.action_pickupPointsFragment_to_orderingFragment,
            bundle
        )
    }

    private fun setCurrentListCondition(isPoints: Boolean) {
        if (isPoints) {
            recyclerView.adapter = pickupPointsAdapter
            searchView.show()
            toolbar_right_text_text_view.hide()
        } else {
            recyclerView.adapter = shopPointsAdapter
            searchView.hide()
            toolbar_right_text_text_view.show()
        }

        changeNextButtonColor()
    }

    private fun initializeBottomSheetBehaviorItems() {
        val bottomSheetBehavior =
            BottomSheetBehavior.from(fragment_pickup_points_addresses_bottom_behavior)

        view?.viewTreeObserver
            ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    bottomSheetBehavior.peekHeight = fragment_pickup_points_bottom_view.height

                    view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
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