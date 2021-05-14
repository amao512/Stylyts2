package kz.eztech.stylyts.presentation.dialogs.shop

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.dialog_bottom_change_gender.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseRoundedBottomSheetDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

class ChangeGenderDialog(
    private val universalViewClickListener: UniversalViewClickListener,
    private val selectedGender: Int = MALE_GENDER
) : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_change_gender), View.OnClickListener {

    companion object {
        const val MALE_GENDER = 0
        const val FEMALE_GENDER = 1

        fun getNewInstance(
            universalViewClickListener: UniversalViewClickListener,
            selectedGender: Int
        ): ChangeGenderDialog {
            return ChangeGenderDialog(
                universalViewClickListener,
                selectedGender
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
        initializeViews()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_bottom_change_gender_for_his_text_view -> {
                universalViewClickListener.onViewClicked(
                    view = dialog_bottom_change_gender_for_his_text_view,
                    position = MALE_GENDER,
                    item = null
                )

                dismiss()
            }
            R.id.dialog_bottom_change_gender_for_her_text_view -> {
                universalViewClickListener.onViewClicked(
                    view = dialog_bottom_change_gender_for_her_text_view,
                    position = FEMALE_GENDER,
                    item = null
                )

                dismiss()
            }
            R.id.dialog_bottom_change_gender_cancel_text_view -> dismiss()
        }
    }

    private fun initializeListeners() {
        dialog_bottom_change_gender_for_his_text_view.setOnClickListener(this)
        dialog_bottom_change_gender_for_her_text_view.setOnClickListener(this)
        dialog_bottom_change_gender_cancel_text_view.setOnClickListener(this)
    }

    private fun initializeViews() {
        when (selectedGender) {
            MALE_GENDER -> {
                dialog_bottom_change_gender_for_his_text_view.setEnableGender(isEnabled = true)
                dialog_bottom_change_gender_for_her_text_view.setEnableGender(isEnabled = false)
            }
            FEMALE_GENDER -> {
                dialog_bottom_change_gender_for_his_text_view.setEnableGender(isEnabled = false)
                dialog_bottom_change_gender_for_her_text_view.setEnableGender(isEnabled = true)
            }
        }
    }

    private fun TextView.setEnableGender(isEnabled: Boolean) {
        if (isEnabled) {
            this.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.app_gray_hint)
            )
            this.isClickable = false
            this.isFocusable = false
        } else {
            this.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.app_dark_blue_gray)
            )
            this.isClickable = true
            this.isFocusable = true
        }
    }
}