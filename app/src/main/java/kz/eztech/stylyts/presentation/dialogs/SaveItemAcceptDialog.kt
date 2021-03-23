package kz.eztech.stylyts.presentation.dialogs

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
import kz.eztech.stylyts.common.presentation.base.DialogChooserListener

/**
 * Created by Ruslan Erdenoff on 24.02.2021.
 */
class SaveItemAcceptDialog: DialogFragment(), View.OnClickListener {
    private var listener: DialogChooserListener? = null
    private var currentModel: CollectionPostCreateModel? = null
    private var currentBitmap: Bitmap? = null
    private var currentPhotoUri: Uri? = null
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_save_item_accept, container, false)
    }

    fun setChoiceListener(listener: DialogChooserListener){
        this.listener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            if(it.containsKey("photoBitmap")){
                currentBitmap = it.getParcelable("photoBitmap")
            }
            if(it.containsKey("photoUri")){
                currentPhotoUri = it.getParcelable("photoUri")
            }
        }
        currentPhotoUri?.let {
            Glide.with(this).load(it).into(this.image_view_dialog_save_item_accept)
        }
        currentBitmap?.let {
            Glide.with(this).load(it).into(this.image_view_dialog_save_item_accept)
        }

        with(include_toolbar_dialog_save_item){
            toolbar_left_corner_action_image_button.visibility = android.view.View.GONE
            toolbar_back_text_view.visibility = android.view.View.VISIBLE
            toolbar_back_text_view.setOnClickListener {
                listener?.onChoice(it,null)
                dismiss()
            }
            toolbar_title_text_view.visibility = android.view.View.VISIBLE
            toolbar_title_text_view.text = "Добавить вещь"
            toolbar_right_text_text_view.visibility = android.view.View.VISIBLE
            toolbar_right_text_text_view.text = "Готово"
            toolbar_right_text_text_view.setOnClickListener{
                listener?.onChoice(it,edit_text_view_dialog_save_item_accept_sign.text.toString())
                dismiss()
            }
            elevation = 0f
        }
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

    override fun onClick(v: View?) {

    }

}