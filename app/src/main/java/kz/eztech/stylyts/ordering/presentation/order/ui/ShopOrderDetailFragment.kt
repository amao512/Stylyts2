package kz.eztech.stylyts.ordering.presentation.order.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_shop_order_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.ordering.domain.models.order.OrderModel
import kz.eztech.stylyts.global.domain.models.user.UserShortModel
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.adapters.ShopOrderClothesAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.ordering.presentation.order.contracts.ShopOrderDetailContract
import kz.eztech.stylyts.ordering.presentation.order_constructor.enums.DeliveryStatusEnum
import kz.eztech.stylyts.ordering.presentation.order_constructor.enums.PaymentStatusEnum
import kz.eztech.stylyts.global.presentation.clothes.ui.ClothesDetailFragment
import kz.eztech.stylyts.profile.presentation.profile.ui.ProfileFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.ordering.presentation.order.presenters.ShopOrderDetailPresenter
import kz.eztech.stylyts.utils.extensions.getDayAndMonth
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class ShopOrderDetailFragment : BaseFragment<MainActivity>(), ShopOrderDetailContract.View,
    UniversalViewClickListener,
    View.OnClickListener {

    @Inject
    lateinit var presenter: ShopOrderDetailPresenter
    private lateinit var shopOrderClothesAdapter: ShopOrderClothesAdapter

    private lateinit var clientAvatarShapeableImageView: ShapeableImageView
    private lateinit var clientShortNameTextView: TextView
    private lateinit var clientUsernameTextView: TextView
    private lateinit var clientFullNameTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var notPaidTextView: TextView
    private lateinit var clothesRecyclerView: RecyclerView
    private lateinit var completeButton: Button
    private lateinit var applyButton: Button

    companion object {
        const val ORDER_ID_KEY = "orderId"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_shop_order_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_shop_order_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(
                toolbar = this,
                title = getString(R.string.order_number_text_format, "0")
            )
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
        shopOrderClothesAdapter = ShopOrderClothesAdapter()
        shopOrderClothesAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        clientAvatarShapeableImageView = fragment_shop_order_client_avatar_shapeable_image_view
        clientShortNameTextView = fragment_shop_order_client_short_name_text_view
        clientUsernameTextView = fragment_shop_order_username_text_view
        clientFullNameTextView = fragment_shop_order_detail_full_name_text_view
        dateTextView = fragment_shop_order_detail_date_text_view
        priceTextView = fragment_shop_order_detail_price_text_view
        completeButton = fragment_shop_order_detail_complete_button
        clothesRecyclerView = fragment_shop_order_detail_recycler_view
        clothesRecyclerView.adapter = shopOrderClothesAdapter
        notPaidTextView = fragment_shop_order_detail_not_paid_text_view
        applyButton = fragment_shop_order_detail_apply_button
    }

    override fun initializeListeners() {
        completeButton.setOnClickListener(this)
        applyButton.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getOrder(orderId = getOrderId())
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_shop_order_detail_progress_bar.show()
    }

    override fun hideProgress() {
        fragment_shop_order_detail_progress_bar.hide()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_shop_order_detail_complete_button -> presenter.setStatusDelivered(
                orderId = getOrderId()
            )
            R.id.fragment_shop_order_detail_apply_button -> presenter.setStatusInProgress(
                orderId = getOrderId()
            )
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is ClothesModel -> navigateToClothes(item)
        }
    }

    override fun processOrder(orderModel: OrderModel) = with(orderModel) {
        processOrderInfo(orderModel = orderModel)
        processOrderStatus(orderModel = orderModel)

        if (client.avatar.isEmpty()) {
            clientAvatarShapeableImageView.hide()
            clientShortNameTextView.text = client.displayShortName
        } else {
            clientShortNameTextView.hide()
            client.avatar.loadImageWithCenterCrop(target = clientAvatarShapeableImageView)
        }

        clientAvatarShapeableImageView.setOnClickListener {
            navigateToClient(client)
        }

        clientShortNameTextView.setOnClickListener {
            navigateToClient(client)
        }

        clientFullNameTextView.setOnClickListener {
            navigateToClient(client)
        }

        clientUsernameTextView.setOnClickListener {
            navigateToClient(client)
        }
    }

    private fun processOrderInfo(orderModel: OrderModel) = with(orderModel) {
        fragment_shop_order_toolbar.toolbar_title_text_view.text =
            displayOrderId(context = requireContext())

        clientUsernameTextView.text = client.username
        clientFullNameTextView.text = client.displayFullName
        dateTextView.text = createdAt.getDayAndMonth()
        priceTextView.text = displayPrice

        shopOrderClothesAdapter.updateList(list = itemObjects)
    }

    private fun processOrderStatus(orderModel: OrderModel) = with(orderModel) {
        Log.d("TAG4", "payment - ${invoice.paymentStatus}")
        Log.d("TAG4", "delivery - ${delivery.deliveryStatus}")

        if (invoice.paymentStatus == PaymentStatusEnum.NEW.status) {
            completeButton.hide()
            notPaidTextView.show()
            applyButton.hide()
        } else if (
            (invoice.paymentStatus == PaymentStatusEnum.PAID.status ||
                    invoice.paymentStatus == PaymentStatusEnum.PENDING.status) &&
            delivery.deliveryStatus == DeliveryStatusEnum.NEW.status
        ) {
            applyButton.show()
            completeButton.hide()
            notPaidTextView.hide()
        } else if (
            (invoice.paymentStatus == PaymentStatusEnum.PAID.status ||
                    invoice.paymentStatus == PaymentStatusEnum.PENDING.status) &&
            delivery.deliveryStatus == DeliveryStatusEnum.IN_PROGRESS.status
        ) {
            completeButton.show()
            notPaidTextView.hide()
            applyButton.hide()
        } else if (
            invoice.paymentStatus == PaymentStatusEnum.PAID.status &&
            delivery.deliveryStatus == DeliveryStatusEnum.DELIVERED.status
        ) {
            completeButton.hide()
            notPaidTextView.hide()
            applyButton.hide()
        }
    }

    private fun navigateToClient(userShortModel: UserShortModel) {
        val bundle = Bundle()
        bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, userShortModel.id)

        findNavController().navigate(R.id.action_shopOrderDetailFragment_to_profileFragment, bundle)
    }

    private fun navigateToClothes(clothes: ClothesModel) {
        val bundle = Bundle()
        bundle.putInt(ClothesDetailFragment.CLOTHES_ID, clothes.id)

        findNavController().navigate(
            R.id.action_shopOrderDetailFragment_to_clothesDetailFragment,
            bundle
        )
    }

    private fun getOrderId(): Int = arguments?.getInt(ORDER_ID_KEY) ?: 0
}