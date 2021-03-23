package kz.eztech.stylyts.presentation.dialogs

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_bottom_photo_chooser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.base.BaseRoundedBottomSheetDialog

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
class PhotoChooserDialog: BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_photo_chooser),View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeListeners()
    }

    private fun initializeListeners(){
        text_view_dialog_bottom_photo_chooser_photo.setOnClickListener(this)
        text_view_dialog_bottom_photo_chooser_barcode.setOnClickListener(this)
        text_view_dialog_bottom_photo_chooser_photo_as_collection.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.text_view_dialog_bottom_photo_chooser_photo -> {
                listener?.onChoice(v,1)
            }
            R.id.text_view_dialog_bottom_photo_chooser_barcode -> {
                listener?.onChoice(v,2)
            }
            R.id.text_view_dialog_bottom_photo_chooser_photo_as_collection -> {
                listener?.onChoice(v,3)
            }
        }
    }
}