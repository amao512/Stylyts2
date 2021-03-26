package kz.eztech.stylyts.presentation.dialogs.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.dialog_push_notification.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.utils.extensions.show

class PushNotificationDialog : DialogFragment(R.layout.dialog_push_notification), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeListeners()
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> dismiss()
        }
    }

    private fun customizeActionBar() {
        with (dialog_push_notification_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.push_notification)
            toolbar_title_text_view.show()

            background = ContextCompat.getDrawable(requireContext(), R.color.toolbar_bg_gray)
        }
    }

    private fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
    }
}