package kz.eztech.stylyts.presentation.fragments.order

import android.os.Bundle
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
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.order.OrderModel
import kz.eztech.stylyts.domain.models.user.UserShortModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ordering.ShopOrderClothesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.ShopOrderDetailContract
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.ordering.ShopOrderDetailPresenter
import kz.eztech.stylyts.presentation.utils.DateFormatterHelper
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.presentation.utils.extensions.show
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
    private lateinit var clothesRecyclerView: RecyclerView
    private lateinit var completeButton: Button

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
    }

    override fun initializeListeners() {
        completeButton.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getOrder(
            token = currentActivity.getTokenFromSharedPref(),
            orderId = arguments?.getInt(ORDER_ID_KEY) ?: 0
        )
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
            R.id.fragment_shop_order_detail_complete_button -> {
            }
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

    override fun processOrder(orderModel: OrderModel) {
        fragment_shop_order_toolbar.toolbar_title_text_view.text = getString(
            R.string.order_number_text_format,
            orderModel.id.toString()
        )

        val client = orderModel.client

        clientUsernameTextView.text = client.username
        clientFullNameTextView.text = getString(
            R.string.full_name_text_format,
            client.firstName,
            client.lastName
        )
        dateTextView.text = DateFormatterHelper.formatISO_8601(
            orderModel.createdAt,
            DateFormatterHelper.FORMAT_DATE_DD_MMMM
        )
        priceTextView.text =
            getString(R.string.price_tenge_text_format, orderModel.price.toString())

        shopOrderClothesAdapter.updateList(list = orderModel.itemObjects)

        if (client.avatar.isEmpty()) {
            clientAvatarShapeableImageView.hide()
            clientShortNameTextView.text = getShortName(client.firstName, client.lastName)
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
}