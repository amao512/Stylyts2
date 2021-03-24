package kz.eztech.stylyts.profile.presentation.fragments

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_address_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.common.data.models.SharedConstants
import kz.eztech.stylyts.profile.domain.models.AddressModel
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.profile.presentation.adapters.AddressAdapter
import kz.eztech.stylyts.common.presentation.adapters.helpers.SwipeToDeleteAddressCallback
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.profile.presentation.contracts.AddressProfileContract
import kz.eztech.stylyts.profile.presentation.interfaces.AddressViewClickListener
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.profile.presentation.presenters.AddressPresenter
import kz.eztech.stylyts.common.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show
import javax.inject.Inject

class AddressProfileFragment : BaseFragment<MainActivity>(), AddressProfileContract.View,
    View.OnClickListener, UniversalViewClickListener, AddressViewClickListener {

    @Inject lateinit var presenter: AddressPresenter
    private lateinit var addressAdapter: AddressAdapter

    private var isForm = false

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_address_profile

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_addresses) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_title_text_view.text = context.getString(R.string.address_profile_title)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()
            toolbar_left_corner_action_image_button.setOnClickListener {
                if (isForm) {
                    displayContent()
                } else {
                    findNavController().navigateUp()
                }
            }
            toolbar_right_corner_action_image_button.hide()

            elevation = 0f
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        addressAdapter = AddressAdapter(addressViewClickListener = this)
    }

    override fun initializeViews() {
        recycler_view_fragment_address_profile.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_address_profile.adapter = addressAdapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteAddressCallback(addressAdapter))
        itemTouchHelper.attachToRecyclerView(recycler_view_fragment_address_profile)
    }

    override fun initializeListeners() {
        linear_layout_fragment_address_profile_add_address.setOnClickListener(this)
        linear_layout_fragment_address_profile_create_address.setOnClickListener(this)
        addressAdapter.itemClickListener = this
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (item) {
            is AddressModel -> {
                displayExistForm(item)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.linear_layout_fragment_address_profile_add_address -> displayForm()
            R.id.linear_layout_fragment_address_profile_create_address -> createAddress()
        }
    }

    override fun processPostInitialization() {
        presenter.getAllAddress(token = getTokenFromSharedPref())
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) = displayToast(msg)

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        base_progress_address_profile?.show()
    }

    override fun hideProgress() {
        base_progress_address_profile?.hide()
    }

    override fun displayContent() {
        isForm = false
        include_toolbar_addresses.toolbar_title_text_view.text = getString(R.string.address_profile_title)
        linear_layout_fragment_address_profile_addresses.show()
        scroll_view_fragment_address_profile.hide()

    }

    override fun displayForm() {
        isForm = true
        include_toolbar_addresses.toolbar_title_text_view.text = getString(R.string.address_profile_add)
        scroll_view_fragment_address_profile.show()
        linear_layout_fragment_address_profile_addresses.hide()
        edit_text_fragment_address_profile_phone.setText(EMPTY_STRING)
        edit_text_fragment_address_profile_country.setText(EMPTY_STRING)
        edit_text_fragment_address_profile_address.setText(EMPTY_STRING)
        edit_text_fragment_address_profile_point.setText(EMPTY_STRING)
        edit_text_fragment_address_profile_post.setText(EMPTY_STRING)
    }

    override fun processAddressList(addressList: List<AddressModel>) {
        val defaultAddressId: Int = currentActivity.getSharedPrefByKey(SharedConstants.DEFAULT_ADDRESS_ID_KEY) ?: 0

        addressList.forEach {
            if (it.id?.toInt() == defaultAddressId) {
                it.isDefaultAddress = true
            }
        }

        addressAdapter.updateList(addressList)
        addressAdapter.notifyDataSetChanged()
    }

    override fun processAddress(addressModel: AddressModel) {
        processPostInitialization()
    }

    override fun showEmpty() {
        text_view_fragment_address_profile_empty?.show()
        base_progress_address_profile.hide()
    }

    override fun hideEmpty() {
        text_view_fragment_address_profile_empty?.hide()
        base_progress_address_profile.hide()
    }

    override fun setDefaultAddress(addressModel: AddressModel) {
        currentActivity.saveSharedPrefByKey(SharedConstants.DEFAULT_ADDRESS_ID_KEY, addressModel.id?.toInt())

        processPostInitialization()
    }

    override fun onSwipeDelete(addressModel: AddressModel) {
        presenter.deleteAddress(
            token = getTokenFromSharedPref(),
            addressId = addressModel.id ?: EMPTY_STRING
        )
    }

    override fun displayDeletedAddress() {
        fragment_address_profile_success_deleted_text_view.show()
        Handler(Looper.myLooper()!!)
            .postDelayed({
                fragment_address_profile_success_deleted_text_view.hide()
            }, 2000)
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.TOKEN_KEY) ?: EMPTY_STRING
    }

    private fun displayExistForm(addressModel: AddressModel) {
        isForm = true
        scroll_view_fragment_address_profile.show()
        linear_layout_fragment_address_profile_addresses.hide()

        edit_text_fragment_address_profile_country.setText(addressModel.country)
        edit_text_fragment_address_profile_address.setText("${addressModel.city}/${addressModel.street}")
        edit_text_fragment_address_profile_post.setText(addressModel.postalCode)
    }

    private fun createAddress() {
        if (checkValidation()) {
            presenter.createAddress(
                token = getTokenFromSharedPref(),
                data = getAddressData()
            )
        } else {
            displayToast(getString(R.string.empty_fields_message))
        }
    }

    private fun checkValidation(): Boolean {
        var isValidate = true

        if (edit_text_fragment_address_profile_address.text.isBlank()) {
            isValidate = false
        }

        if (edit_text_fragment_address_profile_country.text.isBlank()) {
            isValidate = false
        }

        if (edit_text_fragment_address_profile_post.text.isBlank()) {
            isValidate = false
        }

        return isValidate
    }

    private fun getAddressData(): HashMap<String, Any> {
        val data = HashMap<String, Any>()
        val addressList: MutableList<String> = mutableListOf()
        val addressArray = edit_text_fragment_address_profile_address.text.split("/", ", ", " ")

        addressList.addAll(addressArray)

        data["country"] = edit_text_fragment_address_profile_country.text.toString()
        data["postal_code"] = edit_text_fragment_address_profile_post.text.toString()

        when {
            addressList.size == 1 -> {
                data["city"] = addressList[0]
                data["street"] = EMPTY_STRING
            }
            addressList.size > 1 -> {
                data["city"] = addressList[0]
                data["street"] = addressList[1]
            }
            else -> {
                data["city"] = EMPTY_STRING
                data["street"] = EMPTY_STRING
            }
        }

        return data
    }
}