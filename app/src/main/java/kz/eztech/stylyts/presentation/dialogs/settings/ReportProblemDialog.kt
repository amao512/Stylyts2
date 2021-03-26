package kz.eztech.stylyts.presentation.dialogs.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_report_problem.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

class ReportProblemDialog(
    private val universalViewClickListener: UniversalViewClickListener
) : DialogFragment(), View.OnClickListener {

    companion object {

        fun getNewInstance(universalViewClickListener: UniversalViewClickListener): ReportProblemDialog {
            return ReportProblemDialog(universalViewClickListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customizeDialog()

        return inflater.inflate(R.layout.dialog_report_problem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_report_problem_cancel_text_view -> dismiss()
            R.id.dialog_report_problem_spam_text_view -> {
                universalViewClickListener.onViewClicked(
                    view = dialog_report_problem_spam_text_view,
                    position = 0,
                    item = null
                )
                dismiss()
            }
            R.id.dialog_report_problem_something_wrong_text_view -> {
                universalViewClickListener.onViewClicked(
                    view = dialog_report_problem_something_wrong_text_view,
                    position = 0,
                    item = null
                )
                dismiss()
            }
        }
    }

    private fun customizeDialog() {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    private fun initializeListeners() {
        dialog_report_problem_cancel_text_view.setOnClickListener(this)
        dialog_report_problem_spam_text_view.setOnClickListener(this)
        dialog_report_problem_something_wrong_text_view.setOnClickListener(this)
    }
}