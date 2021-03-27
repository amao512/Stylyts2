package kz.eztech.stylyts.presentation.dialogs.profile

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_bottom_change_profile_photo.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseRoundedBottomSheetDialog

class ChangeProfilePhotoDialog : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_change_profile_photo),
    View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_bottom_change_profile_photo_cancel_text_view -> dismiss()
        }
    }

    private fun initializeListeners() {
        dialog_bottom_change_profile_photo_cancel_text_view.setOnClickListener(this)
    }
}