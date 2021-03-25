package kz.eztech.stylyts.collection_constructor.presentation.dialogs

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_bottom_create_collection_choser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.base.BaseRoundedBottomSheetDialog

/**
 * Created by Ruslan Erdenoff on 11.02.2021.
 */
class CreateCollectionChooserDialog : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_create_collection_choser),
    View.OnClickListener {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
    }

    private fun initializeListeners() {
        dialog_bottom_create_collection_chooser_common_line.setOnClickListener(this)
        dialog_bottom_create_collection_chooser_wardrobe.setOnClickListener(this)
        dialog_bottom_photo_chooser_photo_as_collection_buy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
			R.id.dialog_bottom_create_collection_chooser_common_line -> {
				listener?.onChoice(v, item = 1)
			}
			R.id.dialog_bottom_create_collection_chooser_wardrobe -> {
				listener?.onChoice(v, item = 2)
			}
			R.id.dialog_bottom_photo_chooser_photo_as_collection_buy -> {
				listener?.onChoice(v, item = 3)
			}
        }

        dismiss()
    }
}