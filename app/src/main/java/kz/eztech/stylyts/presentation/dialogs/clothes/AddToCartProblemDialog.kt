package kz.eztech.stylyts.presentation.dialogs.clothes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_add_to_cart_problem.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

class AddToCartProblemDialog : DialogFragment() {

    companion object {
        private const val USERNAME_KEY = "username"

        fun getNewInstance(username: String): AddToCartProblemDialog {
            val dialog = AddToCartProblemDialog()
            val bundle = Bundle()

            bundle.putString(USERNAME_KEY, username)
            dialog.arguments = bundle

            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customizeDialog()

        return inflater.inflate(R.layout.dialog_add_to_cart_problem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        initializeListeners()
    }

    private fun customizeDialog() {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext().applicationContext,
                    R.drawable.bg_rounded_dialog
                )
            )
            dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    private fun initializeViews() {
        dialog_add_cart_problem_message_text_view.text = HtmlCompat.fromHtml(
            getString(R.string.add_to_cart_problem_message, getUsernameFromArgs()),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun initializeListeners() {
        dialog_add_cart_problem_close_text_view.setOnClickListener {
            dismiss()
        }
    }

    private fun getUsernameFromArgs(): String = arguments?.getString(USERNAME_KEY) ?: EMPTY_STRING
}