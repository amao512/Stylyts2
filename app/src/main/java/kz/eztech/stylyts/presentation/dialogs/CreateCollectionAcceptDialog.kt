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
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.domain.models.UserSearchModel
import kz.eztech.stylyts.presentation.base.DialogChooserListener

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CreateCollectionAcceptDialog:DialogFragment(),View.OnClickListener,DialogChooserListener{
    internal var listener: DialogChooserListener? = null
    private var currentModel: CollectionPostCreateModel? = null
    private var currentBitmap: Bitmap? = null
    private var currentPhotoUri: Uri? = null
    private var isPhotoChooser = false
    private var selectedList:List<ClothesMainModel>? = null
    private var selectedUsers:ArrayList<UserSearchModel>? = null
    private var hashMap = HashMap<String,Any>()
    private lateinit var chooserDialog:CreateCollectionChooserDialog
    private lateinit var userSearchDialog:UserSearchDialog
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
        chooserDialog = CreateCollectionChooserDialog()
        chooserDialog.setChoiceListener(this)
        selectedUsers = ArrayList<UserSearchModel>()
        userSearchDialog = UserSearchDialog()
        userSearchDialog.setChoiceListener(this)
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
            toolbar_left_corner_action_image_button.visibility = android.view.View.GONE
            toolbar_back_text_view.visibility = android.view.View.VISIBLE
            toolbar_back_text_view.setOnClickListener {
                listener?.onChoice(it,null)
                dismiss()
            }
            toolbar_title_text_view.visibility = android.view.View.VISIBLE
            toolbar_title_text_view.text = "Создать образ"
            toolbar_right_text_text_view.visibility = android.view.View.VISIBLE
            toolbar_right_text_text_view.text = "Готово"
            toolbar_right_text_text_view.setOnClickListener{
                currentModel?.text = edit_text_view_dialog_create_collection_accept_sign.text.toString()
                currentModel?.title =  edit_text_view_dialog_create_collection_accept_sign.text.toString()
                chooserDialog.show(childFragmentManager,"ChooserDialog")
            }
            elevation = 0f
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
        updateUserView()
    
        frame_layout_dialog_create_collection_accept_user_search.setOnClickListener {
            userSearchDialog.show(childFragmentManager,"UserSearchDialog")
        }
    }
    
    override fun onChoice(v: View?, item: Any?) {
        when(item){
            is Int -> {
                currentModel?.let {
                    hashMap["model"] = it
                    when(item as Int){
                        1 -> {
                            hashMap["mode"] = 1
                        }
                        2 -> {
                            hashMap["mode"] = 2
                        }
                        3 -> {
                            hashMap["mode"] = 3
                        }
                    }
                    listener?.onChoice(include_toolbar_dialog_create_collection.toolbar_right_text_text_view,hashMap)
                    dismiss()
                }
            }
            is UserSearchModel -> {
                selectedUsers?.let {
                    if(!it.contains(item)){
                        it.add(item)
                    }
                    updateUserView()
                }
            }
        }
        
    }
    
    private fun updateUserView(){
        text_view_dialog_create_collection_user_count.visibility = View.VISIBLE
        selectedUsers?.let {
            if(it.isNotEmpty())
                text_view_dialog_create_collection_user_count.text = "${it.count()} юзер."
        }
        
    }
    
    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

    override fun onClick(v: View?) {
    
    }
}