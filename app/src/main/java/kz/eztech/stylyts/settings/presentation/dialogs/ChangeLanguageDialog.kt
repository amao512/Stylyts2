package kz.eztech.stylyts.settings.presentation.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.dialog_change_language.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.common.presentation.utils.extensions.show
import kz.eztech.stylyts.common.presentation.utils.helpers.LocaleHelper

class ChangeLanguageDialog : DialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_change_language, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeListeners()
        initializeLanguageRadioGroup()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> dismiss()
            R.id.toolbar_right_text_text_view -> {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun customizeActionBar() {
        with (dialog_change_language_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_right_text_text_view.text = getString(R.string.ready)
            toolbar_right_text_text_view.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_light_orange))
            toolbar_right_text_text_view.show()

            toolbar_title_text_view.text = context.getString(R.string.language)
            toolbar_title_text_view.show()
        }
    }

    private fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
        toolbar_right_text_text_view.setOnClickListener(this)
    }

    private fun initializeLanguageRadioGroup() {
        dialog_change_language_radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.dialog_change_language_kazakh_radio_button -> {
                    LocaleHelper.setLocale(requireContext(), "kz")
                }
                R.id.dialog_change_language_english_radio_button -> {
                    LocaleHelper.setLocale(requireContext(), "en")
                }
                R.id.dialog_change_language_russian_radio_button -> {
                    LocaleHelper.setLocale(requireContext(), "ru")
                }
            }
        }
    }
}