package kz.eztech.stylyts.presentation.dialogs.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.dialog_bottom_message_problem.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseRoundedBottomSheetDialog
import kz.eztech.stylyts.presentation.interfaces.MessageProblemViewListener


class MessageProblemDialog(
    private val problemTitle: String,
    private val messageProblemViewListener: MessageProblemViewListener
) : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_message_problem),
    View.OnClickListener {

    companion object {

        fun getNewInstance(
            problemTitle: String,
            messageProblemViewListener: MessageProblemViewListener
        ): MessageProblemDialog {
            return MessageProblemDialog(problemTitle, messageProblemViewListener)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initializeViewsData()
        initializeListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_bottom_message_problem_cancel_text_view -> dismiss()
            R.id.dialog_bottom_message_problem_send_text_view -> {
                if (dialog_bottom_message_problem_edit_text.text.isNotBlank()) {
                    messageProblemViewListener.writeProblem(
                        dialog_bottom_message_problem_edit_text.text.toString()
                    )
                    dismiss()
                }
            }
        }
    }

    private fun initializeViewsData() {
        dialog_bottom_message_problem_title_text_view.text = problemTitle
        dialog_bottom_message_problem_send_text_view.isClickable = false
    }

    private fun initializeListeners() {
        dialog_bottom_message_problem_cancel_text_view.setOnClickListener(this)
        dialog_bottom_message_problem_send_text_view.setOnClickListener(this)

        dialog_bottom_message_problem_edit_text.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                changeSendTextColor(s)
            }
        })
    }

    private fun changeSendTextColor(s: Editable?) {
        s?.let {
            if (it.isNotBlank()) {
                dialog_bottom_message_problem_send_text_view.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.app_light_orange)
                )
                dialog_bottom_message_problem_send_text_view.isClickable = true
            } else {
                dialog_bottom_message_problem_send_text_view.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.app_gray_hint)
                )
                dialog_bottom_message_problem_send_text_view.isClickable = false
            }
        }
    }
}