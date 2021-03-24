package kz.eztech.stylyts.constructor.presentation.dialogs

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
        text_view_dialog_bottom_create_collection_chooser_public.setOnClickListener(this)
        text_view_dialog_bottom_create_collection_chooser_wardrobe.setOnClickListener(this)
        text_view_dialog_bottom_photo_chooser_photo_as_collection_buy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
			R.id.text_view_dialog_bottom_create_collection_chooser_public -> {
				listener?.onChoice(v, item = 1)
			}
			R.id.text_view_dialog_bottom_create_collection_chooser_wardrobe -> {
				listener?.onChoice(v, item = 2)
			}
			R.id.text_view_dialog_bottom_photo_chooser_photo_as_collection_buy -> {
				listener?.onChoice(v, item = 3)
			}
        }
    }
}