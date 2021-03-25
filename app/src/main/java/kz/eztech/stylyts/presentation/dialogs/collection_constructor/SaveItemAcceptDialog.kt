package kz.eztech.stylyts.presentation.dialogs.collection_constructor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_save_item_accept.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 24.02.2021.
 */
class SaveItemAcceptDialog : DialogFragment() {

    private var listener: DialogChooserListener? = null
    private var currentModel: CollectionPostCreateModel? = null
    private var currentBitmap: Bitmap? = null
    private var currentPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_save_item_accept, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeArguments()
        processPhotos()
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    fun setChoiceListener(listener: DialogChooserListener) {
        this.listener = listener
    }

    private fun customizeActionBar() {
        with(include_toolbar_dialog_save_item) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_back_text_view.setOnClickListener {
                listener?.onChoice(it, null)
                dismiss()
            }
            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.save_item_accept_add_item)

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.save_item_accept_ready)
            toolbar_right_text_text_view.setOnClickListener {
                listener?.onChoice(it, edit_text_view_dialog_save_item_accept_sign.text.toString())
                dismiss()
            }
        }
    }

    private fun initializeArguments() {
        arguments?.let {
            if (it.containsKey("photoBitmap")) {
                currentBitmap = it.getParcelable("photoBitmap")
            }

            if (it.containsKey("photoUri")) {
                currentPhotoUri = it.getParcelable("photoUri")
            }
        }
    }

    private fun processPhotos() {
        currentPhotoUri?.let {
            Glide.with(this)
                .load(it)
                .into(this.image_view_dialog_save_item_accept)
        }
        currentBitmap?.let {
            Glide.with(this)
                .load(it)
                .into(this.image_view_dialog_save_item_accept)
        }
    }
}