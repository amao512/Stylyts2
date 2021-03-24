package kz.eztech.stylyts.settings.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_exit.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener

class ExitDialog(
    private val universalViewClickListener: UniversalViewClickListener
) : DialogFragment(), View.OnClickListener {

    companion object {
        private const val USERNAME_BUNDLE_KEY = "username_bundle"

        fun getNewInstance(
            universalViewClickListener: UniversalViewClickListener,
            username: String
        ): ExitDialog {
            val exitDialog = ExitDialog(universalViewClickListener)
            val bundle = Bundle()

            bundle.putString(USERNAME_BUNDLE_KEY, username)
            exitDialog.arguments = bundle

            return exitDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customizeDialog()

        return inflater.inflate(R.layout.dialog_exit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        initializeDependency()
        initializeViewsData()
        initializeListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_exit_positive_text_view -> onExit()
            R.id.dialog_exit_cancel_text_view -> dismiss()
        }
    }

    private fun customizeDialog() {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext().applicationContext,
                    R.drawable.bg_exit_dialog
                )
            )
            dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    private fun initializeDependency() {
        (requireContext().applicationContext as StylytsApp).applicationComponent.inject(dialog = this)
    }

    private fun initializeViewsData() {
        dialog_exit_warn_title.text = getString(
            R.string.dialog_exit_title,
            arguments?.getString(USERNAME_BUNDLE_KEY)
        )
    }

    private fun initializeListeners() {
        dialog_exit_positive_text_view.setOnClickListener(this)
        dialog_exit_cancel_text_view.setOnClickListener(this)
    }

    private fun onExit() {
        universalViewClickListener.onViewClicked(
            view = dialog_exit_positive_text_view,
            item = null,
            position = 0
        )
    }
}