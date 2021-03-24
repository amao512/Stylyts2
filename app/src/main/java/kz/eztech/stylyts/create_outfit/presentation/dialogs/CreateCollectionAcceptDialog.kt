package kz.eztech.stylyts.create_outfit.presentation.dialogs

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
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.common.domain.models.UserSearchModel
import kz.eztech.stylyts.common.presentation.base.DialogChooserListener
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CreateCollectionAcceptDialog:DialogFragment(),View.OnClickListener, DialogChooserListener {

    private lateinit var chooserDialog: CreateCollectionChooserDialog
    private lateinit var userSearchDialog: UserSearchDialog

    internal var listener: DialogChooserListener? = null

    private var currentModel: CollectionPostCreateModel? = null
    private var currentBitmap: Bitmap? = null
    private var currentPhotoUri: Uri? = null
    private var isPhotoChooser = false
    private var selectedList:List<ClothesMainModel>? = null
    private var selectedUsers:ArrayList<UserSearchModel>? = null
    private var hashMap = HashMap<String,Any>()

    fun setChoiceListener(listener: DialogChooserListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_create_collection_accept, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeViewsData()
        initializeListeners()
        initializeArguments()
        processPhotos()
        processPhotoChooser()
        updateUserView()
    }
    
    override fun onChoice(v: View?, item: Any?) {
        when(item){
            is Int -> {
                currentModel?.let {
                    hashMap["model"] = it
                    when(item) {
                        1 -> hashMap["mode"] = 1
                        2 -> hashMap["mode"] = 2
                        3 -> hashMap["mode"] = 3
                    }

                    listener?.onChoice(
                        v = include_toolbar_dialog_create_collection.toolbar_right_text_text_view,
                        item = hashMap
                    )
                    dismiss()
                }
            }
            is UserSearchModel -> selectedUsers?.let {
                if(!it.contains(item)) it.add(item)

                updateUserView()
            }
        }
    }
    
    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frame_layout_dialog_create_collection_accept_user_search -> {
                userSearchDialog.show(childFragmentManager,"UserSearchDialog")
            }
        }
    }

    private fun customizeActionBar() {
        with(include_toolbar_dialog_create_collection){
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_back_text_view.setOnClickListener {
                listener?.onChoice(it,null)
                dismiss()
            }

            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.create_collection_create_outfit)

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.create_collection_ready)
            toolbar_right_text_text_view.setOnClickListener{
                currentModel?.text = edit_text_view_dialog_create_collection_accept_sign.text.toString()
                currentModel?.title =  edit_text_view_dialog_create_collection_accept_sign.text.toString()
                chooserDialog.show(childFragmentManager,"ChooserDialog")
            }
        }
    }

    private fun initializeViewsData() {
        chooserDialog = CreateCollectionChooserDialog()
        selectedUsers = ArrayList()
        userSearchDialog = UserSearchDialog()
    }

    private fun initializeListeners() {
        chooserDialog.setChoiceListener(this)
        userSearchDialog.setChoiceListener(this)
        frame_layout_dialog_create_collection_accept_user_search.setOnClickListener(this)
    }

    private fun initializeArguments() {
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
    }

    private fun processPhotos() {
        currentPhotoUri?.let {
            Glide.with(this)
                .load(it)
                .into(this.image_view_dialog_create_collection_accept)
        }

        currentBitmap?.let {
            Glide.with(this)
                .load(it)
                .into(this.image_view_dialog_create_collection_accept)
        }
    }

    private fun processPhotoChooser() {
        if(isPhotoChooser){
            frame_layout_dialog_create_collection_accept_choose_clothes.show()
            frame_layout_dialog_create_collection_accept_choose_clothes.setOnClickListener {
                listener?.onChoice(it,null)
                dismiss()
            }
            selectedList?.let {
                text_view_dialog_create_collection_items_count.text = "${it.count()} вещ."
            }
        }
    }

    private fun updateUserView(){
        text_view_dialog_create_collection_user_count.show()

        selectedUsers?.let {
            if(it.isNotEmpty())
                text_view_dialog_create_collection_user_count.text = "${it.count()} юзер."
        }
    }
}