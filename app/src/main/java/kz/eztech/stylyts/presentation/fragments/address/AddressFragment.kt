package kz.eztech.stylyts.presentation.fragments.address

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_address_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.address.AddressAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.SwipeToDeleteAddressCallback
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.address.AddressContract
import kz.eztech.stylyts.presentation.interfaces.AddressViewClickListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.address.AddressPresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class AddressFragment : BaseFragment<MainActivity>(), AddressContract.View,
    View.OnClickListener, UniversalViewClickListener, AddressViewClickListener {

    @Inject lateinit var presenter: AddressPresenter
    private lateinit var addressAdapter: AddressAdapter

    private lateinit var recyclerView: RecyclerView

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
        addressAdapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recyclerView = recycler_view_fragment_address_profile
        recyclerView.adapter = addressAdapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteAddressCallback(addressAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun initializeListeners() {
        linear_layout_fragment_address_profile_add_address.setOnClickListener(this)
        linear_layout_fragment_address_profile_create_address.setOnClickListener(this)
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
        presenter.getAddresses()
        handleRecyclerView()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

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
        include_toolbar_addresses.toolbar_title_text_view.text =
            getString(R.string.address_profile_title)
        linear_layout_fragment_address_profile_addresses.show()
        scroll_view_fragment_address_profile.hide()

    }

    override fun displayForm() {
        isForm = true
        include_toolbar_addresses.toolbar_title_text_view.text =
            getString(R.string.address_profile_add)
        scroll_view_fragment_address_profile.show()
        linear_layout_fragment_address_profile_addresses.hide()
        edit_text_fragment_address_profile_phone.setText(EMPTY_STRING)
        edit_text_fragment_address_profile_country.setText(EMPTY_STRING)
        edit_text_fragment_address_profile_address.setText(EMPTY_STRING)
        edit_text_fragment_address_profile_point.setText(EMPTY_STRING)
        edit_text_fragment_address_profile_post.setText(EMPTY_STRING)
    }

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processAddressList(state.data)
            is Paginator.State.NewPageProgress<*> -> processAddressList(state.data)
            else -> {
            }
        }

        hideProgress()
    }

    override fun processAddressList(list: List<Any?>) {
        list.map {
            (it as AddressModel).apply {
                isDefaultAddress =
                    id == currentActivity.getSharedPrefByKey(SharedConstants.DEFAULT_ADDRESS_ID_KEY) ?: 0
            }
        }.let {
            addressAdapter.updateList(it)
        }
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
        currentActivity.saveSharedPrefByKey(SharedConstants.DEFAULT_ADDRESS_ID_KEY, addressModel.id)

        processPostInitialization()
    }

    override fun onSwipeDelete(addressModel: AddressModel) {
        presenter.deleteAddress(addressId = addressModel.id.toString())
    }

    override fun displayDeletedAddress() {
        fragment_address_profile_success_deleted_text_view.show()
        Handler(Looper.myLooper()!!)
            .postDelayed({
                fragment_address_profile_success_deleted_text_view.hide()
            }, 1000)
    }

    private fun handleRecyclerView() {
        recycler_view_fragment_address_profile.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recycler_view_fragment_address_profile.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMorePage()
                }
            }
        })
    }

    private fun displayExistForm(addressModel: AddressModel) = with(addressModel) {
        isForm = true
        scroll_view_fragment_address_profile.show()
        linear_layout_fragment_address_profile_addresses.hide()

        edit_text_fragment_address_profile_country.setText(country)
        edit_text_fragment_address_profile_address.setText(displayCityAndStreet)
        edit_text_fragment_address_profile_post.setText(postalCode)
    }

    private fun createAddress() {
        if (checkValidation()) {
            presenter.createAddress(data = getAddressData())
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
        data["house"] = edit_text_fragment_address_profile_house.text.toString()

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