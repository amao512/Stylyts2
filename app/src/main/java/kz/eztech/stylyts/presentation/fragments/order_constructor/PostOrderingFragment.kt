package kz.eztech.stylyts.presentation.fragments.order_constructor

import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_courtier_ordering.*
import kotlinx.android.synthetic.main.fragment_post_ordering.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class PostOrderingFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener {

    private lateinit var cityEditText: EditText
    private lateinit var streetEditText: EditText
    private lateinit var houseEditText: EditText
    private lateinit var apartmentEditText: EditText
    private lateinit var postIndexEditText: EditText

    companion object {
        const val CITY_KEY = "city"
        const val CUSTOMER_KEY = "customer"
    }

    override fun getLayoutId(): Int = R.layout.fragment_post_ordering

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with (fragment_post_ordering_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@PostOrderingFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.button_ordering)
            toolbar_title_text_view.show()

            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.show()

            toolbar_bottom_border_view.hide()
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        cityEditText = fragment_post_ordering_city_edit_text
        cityEditText.setText(arguments?.getString(CITY_KEY) ?: EMPTY_STRING)

        streetEditText = fragment_post_ordering_street_edit_text
        houseEditText = fragment_post_ordering_house_edit_text
        apartmentEditText = fragment_post_ordering_apartment_edit_text
        postIndexEditText = fragment_post_ordering_post_index_edit_text
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.toolbar_right_text_text_view -> {}
        }
    }
}