package kz.eztech.stylyts.presentation.dialogs.collection

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_bottom_collection_context.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseRoundedBottomSheetDialog

class CollectionContextDialog : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_collection_context) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
    }

    private fun initializeListeners() {
        dialog_bottom_collection_context_delete_text_view.setOnClickListener {
            listener?.onChoice(it, null)
        }

        dialog_bottom_collection_cancel_archive_text_view.setOnClickListener {
            dismiss()
        }
    }
}