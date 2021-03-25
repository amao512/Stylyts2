package kz.eztech.stylyts.profile.presentation.dialogs

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_bottom_creator_chooser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.base.BaseRoundedBottomSheetDialog

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
class CreatorChooserDialog : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_creator_chooser),
    View.OnClickListener {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
    }

    private fun initializeListeners() {
        dialog_bottom_creator_chooser_barcode.setOnClickListener(this)
        dialog_bottom_creator_chooser_photo.setOnClickListener(this)
        dialog_bottom_creator_chooser_create_publish.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_bottom_creator_chooser_barcode -> {
                listener?.onChoice(v, 1)
            }
            R.id.dialog_bottom_creator_chooser_photo -> {
                listener?.onChoice(v, 2)
            }
            R.id.dialog_bottom_creator_chooser_create_publish -> {
                listener?.onChoice(v, 3)
            }
        }
    }
}