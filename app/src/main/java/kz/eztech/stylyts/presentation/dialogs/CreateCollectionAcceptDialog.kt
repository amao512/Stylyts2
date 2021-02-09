package kz.eztech.stylyts.presentation.dialogs

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_create_collection_accept.*
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.presentation.base.DialogChooserListener

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CreateCollectionAcceptDialog:DialogFragment(),View.OnClickListener{
    internal var listener: DialogChooserListener? = null
    private var currentModel: CollectionPostCreateModel? = null
    private var currentBitmap: Bitmap? = null
    private var currentPhotoUri: Uri? = null
    private var isPhotoChooser = false
    private var selectedList:List<ClothesMainModel>? = null
    fun setChoiceListener(listener: DialogChooserListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_create_collection_accept, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        arguments?.let {
            if(it.containsKey("collectionModel")){
                currentModel = it.getParcelable("collectionModel")
            }
            if(it.containsKey("photoBitmap")){
                currentBitmap = it.getParcelable("photoBitmap")
            }
            if(it.containsKey("photoUri")){
                currentPhotoUri = it.getParcelable("photoUri")
            }
            if(it.containsKey("isChooser")){
                isPhotoChooser = it.getBoolean("isChooser")
            }
            if(it.containsKey("clothes")){
                selectedList = it.getParcelableArrayList("clothes")
            }
        }
        currentPhotoUri?.let {
            Glide.with(this).load(it).into(this.image_view_dialog_create_collection_accept)
        }
        currentBitmap?.let {
            Glide.with(this).load(it).into(this.image_view_dialog_create_collection_accept)
        }
        
        with(include_toolbar_dialog_create_collection){
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_back.setOnClickListener {
                listener?.onChoice(it,null)
                dismiss()
            }
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.text = "Создать образ"
            text_view_toolbar_right_text.visibility = android.view.View.VISIBLE
            text_view_toolbar_right_text.text = "Готово"
            text_view_toolbar_right_text.setOnClickListener{
                currentModel?.text = edit_text_view_dialog_create_collection_accept_sign.text.toString()
                currentModel?.title =  edit_text_view_dialog_create_collection_accept_sign.text.toString()
                listener?.onChoice(it,currentModel)
                dismiss()
            }
            elevation = 0f
        }

        text_view_save_collection.setOnClickListener {
            currentModel?.text = edit_text_view_dialog_create_collection_accept_sign.text.toString()
            currentModel?.title =  edit_text_view_dialog_create_collection_accept_sign.text.toString()
            listener?.onChoice(it,currentModel)
            dismiss()
        }
        if(isPhotoChooser){
            frame_layout_dialog_create_collection_accept_choose_clothes.visibility = View.VISIBLE
            frame_layout_dialog_create_collection_accept_choose_clothes.setOnClickListener {
                listener?.onChoice(it,null)
                dismiss()
            }
            selectedList?.let {
                text_view_dialog_create_collection_items_count.text = "${it.count()} вещ."
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

    override fun onClick(v: View?) {

    }
}