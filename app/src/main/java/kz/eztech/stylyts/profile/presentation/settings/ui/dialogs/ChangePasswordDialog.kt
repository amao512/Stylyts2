package kz.eztech.stylyts.profile.presentation.settings.ui.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.dialog_change_password.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.utils.extensions.show

class ChangePasswordDialog : DialogFragment(R.layout.dialog_change_password), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeListeners()
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> dismiss()
            R.id.toolbar_right_text_text_view -> {}
        }
    }

    private fun customizeActionBar() {
        with (dialog_change_password_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_right_text_text_view.text = getString(R.string.saved_settings_save)
            toolbar_right_text_text_view.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_gray_hint))
            toolbar_right_text_text_view.isClickable = false
            toolbar_right_text_text_view.show()

            toolbar_title_text_view.text = getString(R.string.password)
            toolbar_title_text_view.show()

            background = ContextCompat.getDrawable(requireContext(), R.color.toolbar_bg_gray)
        }
    }

    private fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
        toolbar_right_text_text_view.setOnClickListener(this)
    }
}