package kz.eztech.stylyts.presentation.dialogs.collection_constructor

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_bottom_create_collection_choser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseRoundedBottomSheetDialog
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 11.02.2021.
 */
class CreateCollectionChooserDialog(
    private val isPublication: Boolean
) : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_create_collection_choser),
    View.OnClickListener {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        initializeListeners()
    }

    private fun initializeViews() {
        if (isPublication) {
            dialog_bottom_create_collection_chooser_common_line.show()
            dialog_bottom_create_collection_chooser_wardrobe.show()
            dialog_bottom_create_collection_chooser_divider_one.show()
            dialog_bottom_create_collection_chooser_create.hide()
        } else {
            dialog_bottom_create_collection_chooser_common_line.hide()
            dialog_bottom_create_collection_chooser_wardrobe.hide()
            dialog_bottom_create_collection_chooser_divider_one.hide()
            dialog_bottom_create_collection_chooser_create.show()
        }
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
            R.id.dialog_bottom_create_collection_chooser_create -> {
                listener?.onChoice(v, null)
            }
        }

        dismiss()
    }
}