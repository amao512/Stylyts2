package kz.eztech.stylyts.presentation.dialogs.ordering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_pickup_point_info.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.address.AddressModel
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.utils.extensions.show

class PickupPointInfoDialog(
    private val dialogChooserListener: DialogChooserListener
) : DialogFragment(), View.OnClickListener {

    private lateinit var cityTextView: TextView
    private lateinit var pickupPointTextView: TextView
    private lateinit var deliveryConditionTextView: TextView
    private lateinit var deliveryDateTextView: TextView
    private lateinit var deliveryTimeTextView: TextView

    companion object {
        private const val ADDRESS_KEY = "address"
        private const val DELIVERY_CONDITION_KEY = "deliveryCondition"

        fun getNewInstance(
            dialogChooserListener: DialogChooserListener,
            addressModel: AddressModel,
            deliveryCondition: String
        ): PickupPointInfoDialog {
            val dialog = PickupPointInfoDialog(dialogChooserListener)
            val bundle = Bundle()

            bundle.putString(DELIVERY_CONDITION_KEY, deliveryCondition)
            bundle.putParcelable(ADDRESS_KEY, addressModel)
            dialog.arguments = bundle

            return dialog
        }
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_pickup_point_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeViews()
        initializeArguments()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> dismiss()
            R.id.toolbar_right_text_text_view -> {
                dialogChooserListener.onChoice(v, arguments?.getParcelable<AddressModel>(ADDRESS_KEY))
                dismiss()
            }
        }
    }

    private fun customizeActionBar() {
        with (dialog_pickup_point_info_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@PickupPointInfoDialog)
            toolbar_left_corner_action_image_button.show()

            toolbar_right_text_text_view.text = getString(R.string.next)
            toolbar_right_text_text_view.setOnClickListener(this@PickupPointInfoDialog)
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.app_light_orange)
            )
            toolbar_right_text_text_view.show()

            toolbar_title_text_view.text = getString(R.string.button_ordering)
            toolbar_title_text_view.show()
        }
    }

    private fun initializeViews() {
        cityTextView = dialog_pickup_point_info_city_text_view
        pickupPointTextView = dialog_pickup_point_info_points_text_view
        deliveryConditionTextView = dialog_pickup_point_info_delivery_condition_text_view
        deliveryDateTextView = dialog_pickup_point_info_delivery_date_text_view
        deliveryTimeTextView = dialog_pickup_point_info_time_text_view
    }

    private fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(ADDRESS_KEY)) {
                it.getParcelable<AddressModel>(ADDRESS_KEY)?.let { address ->
                    processAddress(addressModel = address)
                }
            }
        }
    }

    private fun processAddress(addressModel: AddressModel) {
        cityTextView.text = addressModel.city
        (addressModel.street + ", " + addressModel.apartment).also { pickupPointTextView.text = it }
        deliveryConditionTextView.text = arguments?.getString(DELIVERY_CONDITION_KEY)
    }
}