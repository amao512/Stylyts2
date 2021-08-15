package kz.eztech.stylyts.profile.presentation.profile.ui.dialogs

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_bottom_change_profile_photo.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.presentation.base.BaseRoundedBottomSheetDialog
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener

class ChangeProfilePhotoDialog(
    private val universalViewClickListener: UniversalViewClickListener
) : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_change_profile_photo),
    View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_bottom_change_profile_photo_cancel_text_view -> dismiss()
            R.id.dialog_bottom_change_profile_photo_choose_from_gallery_text_view -> {
                universalViewClickListener.onViewClicked(
                    dialog_bottom_change_profile_photo_choose_from_gallery_text_view,
                    0,
                    null
                )
            }
            R.id.dialog_bottom_change_profile_photo_take_photo_text_view -> {
                universalViewClickListener.onViewClicked(
                    dialog_bottom_change_profile_photo_take_photo_text_view,
                    0,
                    null
                )
            }
            R.id.dialog_bottom_change_profile_photo_delete_text_view -> {
                universalViewClickListener.onViewClicked(
                    dialog_bottom_change_profile_photo_delete_text_view,
                    0,
                    null
                )
            }
        }

        dismiss()
    }

    private fun initializeListeners() {
        dialog_bottom_change_profile_photo_cancel_text_view.setOnClickListener(this)
        dialog_bottom_change_profile_photo_choose_from_gallery_text_view.setOnClickListener(this)
        dialog_bottom_change_profile_photo_take_photo_text_view.setOnClickListener(this)
        dialog_bottom_change_profile_photo_delete_text_view.setOnClickListener(this)
    }
}