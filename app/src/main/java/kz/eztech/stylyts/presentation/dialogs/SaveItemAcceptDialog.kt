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
import kotlinx.android.synthetic.main.dialog_create_collection_accept.*
import kotlinx.android.synthetic.main.dialog_create_collection_accept.image_view_dialog_create_collection_accept
import kotlinx.android.synthetic.main.dialog_save_item_accept.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.presentation.base.DialogChooserListener

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
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_back.setOnClickListener {
                listener?.onChoice(it,null)
                dismiss()
            }
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.text = "Добавить вещь"
            text_view_toolbar_right_text.visibility = android.view.View.VISIBLE
            text_view_toolbar_right_text.text = "Готово"
            text_view_toolbar_right_text.setOnClickListener{
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