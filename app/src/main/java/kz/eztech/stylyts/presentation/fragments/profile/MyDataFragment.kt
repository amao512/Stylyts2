package kz.eztech.stylyts.presentation.fragments.profile

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_my_data.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.profile.MyDataContract
import kz.eztech.stylyts.presentation.presenters.profile.MyDataPresenter
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class MyDataFragment : BaseFragment<MainActivity>(), MyDataContract.View, View.OnClickListener {

    @Inject lateinit var presenter: MyDataPresenter

    private lateinit var avatarShapeableImageView: ShapeableImageView
    private lateinit var userShortNameTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var ordersItemFrameLayout: FrameLayout
    private lateinit var paymentWayItemFrameLayout: FrameLayout
    private lateinit var addressesItemFrameLayout: FrameLayout
    private lateinit var incomesItemFrameLayout: FrameLayout

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_my_data

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_my_data_toolbar) {
            toolbar_title_text_view.show()
            toolbar_title_text_view.textSize = 14f

            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_drawer)
            toolbar_right_corner_action_image_button.show()

            customizeActionToolBar(toolbar = this)
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

    override fun initializeViews() {
        avatarShapeableImageView = fragment_my_data_avatar_shapeable_image_view
        userShortNameTextView = fragment_my_data_user_short_name_text_view
        userNameTextView = fragment_my_data_user_name_text_view
        ordersItemFrameLayout = fragment_my_data_orders_holder_frame_layout
        paymentWayItemFrameLayout = fragment_my_data_payment_way_frame_layout
        addressesItemFrameLayout = fragment_my_data_addresses_frame_layout
        incomesItemFrameLayout = fragment_my_data_incomes_frame_layout
    }

    override fun initializeListeners() {
        ordersItemFrameLayout.setOnClickListener(this)
        paymentWayItemFrameLayout.setOnClickListener(this)
        addressesItemFrameLayout.setOnClickListener(this)
        incomesItemFrameLayout.setOnClickListener(this)

        fragment_my_data_toolbar.toolbar_right_corner_action_image_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getProfile()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processProfile(userModel: UserModel) {
        fragment_my_data_toolbar.toolbar_title_text_view.text = userModel.username
        userNameTextView.text = userModel.firstName

        if (userModel.avatar.isBlank()) {
            avatarShapeableImageView.hide()
            userShortNameTextView.show()
            userShortNameTextView.text = getShortName(
                firstName = userModel.firstName,
                lastName = userModel.lastName
            )
        } else {
            userShortNameTextView.hide()
            userModel.avatar.loadImageWithCenterCrop(target = avatarShapeableImageView)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_my_data_payment_way_frame_layout -> {
                findNavController().navigate(R.id.action_myDataFragment_to_cardFragment)
            }
            R.id.fragment_my_data_addresses_frame_layout -> {
                findNavController().navigate(R.id.action_myDataFragment_to_addressProfileFragment)
            }
            R.id.fragment_my_data_incomes_frame_layout -> {
                findNavController().navigate(R.id.action_myDataFragment_to_profileIncomeFragment)
            }
            R.id.toolbar_right_corner_action_image_button -> {
                findNavController().navigate(R.id.action_myDataFragment_to_settingsFragment)
            }
            R.id.fragment_my_data_orders_holder_frame_layout -> {
                findNavController().navigate(R.id.action_myDataFragment_to_orderListFragment)
            }
        }
    }
}