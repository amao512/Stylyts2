package kz.eztech.stylyts.presentation.dialogs.collection

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_bottom_collection_context.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseRoundedBottomSheetDialog
import kz.eztech.stylyts.utils.extensions.hide

class CollectionContextDialog(
    private val isOwn: Boolean = false,
    private val item: Any? = null
) : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_collection_context) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        initializeListeners()
    }

    private fun initializeViews() {
        if (!isOwn) {
            dialog_bottom_collection_context_delete_text_view.hide()
            dialog_bottom_collection_context_change_text_view.hide()
            dialog_bottom_collection_context_divider_one.hide()
            dialog_bottom_collection_context_divider_two.hide()
        } else {
            dialog_bottom_collection_context_support_text_view.hide()
            dialog_bottom_collection_context_divider_three.hide()
        }
    }

    private fun initializeListeners() {
        dialog_bottom_collection_context_delete_text_view.setOnClickListener {
            listener?.onChoice(it, item)
            dismiss()
        }

        dialog_bottom_collection_cancel_archive_text_view.setOnClickListener {
            dismiss()
        }

        dialog_bottom_collection_context_change_text_view.setOnClickListener {
            listener?.onChoice(it, item)
            dismiss()
        }
    }
}