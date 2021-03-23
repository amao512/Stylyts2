package kz.eztech.stylyts.settings.presentation.dialogs

import android.content.Intent
import android.content.SharedPreferences
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
import kz.eztech.stylyts.auth.presentation.AuthorizationActivity
import kz.eztech.stylyts.data.models.SharedConstants
import javax.inject.Inject

class ExitDialog : DialogFragment(), View.OnClickListener {

    @Inject lateinit var sharedPreferences: SharedPreferences

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

    private fun initializeListeners() {
        dialog_exit_positive_text_view.setOnClickListener(this)
        dialog_exit_cancel_text_view.setOnClickListener(this)
    }

    private fun onExit() {
        sharedPreferences.edit().apply {
            remove(SharedConstants.TOKEN_KEY)
            remove(SharedConstants.USER_ID_KEY)
            remove(SharedConstants.USERNAME_KEY)
            apply()
        }

        startActivity(Intent(context, AuthorizationActivity::class.java))
        activity?.finish()
    }
}