package kz.eztech.stylyts.constructor.presentation.dialogs

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
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.common.data.models.SharedConstants
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.common.domain.models.UserSearchModel
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.common.presentation.base.DialogChooserListener
import kz.eztech.stylyts.common.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.common.presentation.utils.FileUtils
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show
import kz.eztech.stylyts.constructor.domain.models.PostModel
import kz.eztech.stylyts.constructor.presentation.contracts.CreateCollectionAcceptContract
import kz.eztech.stylyts.constructor.presentation.presenters.CreateCollectionAcceptPresenter
import java.io.File
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CreateCollectionAcceptDialog : DialogFragment(), View.OnClickListener, DialogChooserListener,
    CreateCollectionAcceptContract.View {

    @Inject lateinit var presenter: CreateCollectionAcceptPresenter

    private lateinit var chooserDialog: CreateCollectionChooserDialog
    private lateinit var userSearchDialog: UserSearchDialog

    internal var listener: DialogChooserListener? = null

    private var currentModel: CollectionPostCreateModel? = null
    private var currentBitmap: Bitmap? = null
    private var currentPhotoUri: Uri? = null
    private var isPhotoChooser = false
    private var selectedList: List<ClothesMainModel>? = null
    private var selectedUsers: ArrayList<UserSearchModel>? = null
    private var hashMap = HashMap<String, Any>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_create_collection_accept, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeDependency()
        initializePresenter()
        initializeViewsData()
        initializeListeners()
        initializeArguments()
        processPhotos()
        processPhotoChooser()
        updateUserView()
    }

    override fun onChoice(v: View?, item: Any?) {
        when (item) {
            is Int -> {
                currentModel?.let {
                    hashMap["model"] = it
                    when (item) {
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
                if (!it.contains(item)) it.add(item)

                updateUserView()
            }
        }

        when (v?.id) {
            R.id.text_view_dialog_bottom_create_collection_chooser_public -> {
                if (currentBitmap != null && currentPhotoUri == null) {
                    try {
                        currentBitmap?.let {
                            val file: File? = FileUtils.createPngFileFromBitmap(requireContext(), it)

                            createPost(file)
                        }
                    } catch (e: Exception) {
                        Log.e("TAG", "bitmap = " + e.localizedMessage)
                    }
                }

                if (currentPhotoUri != null && currentBitmap == null) {
                    try {
                        currentPhotoUri?.let {
                            val file = File(it.path)

                            createPost(file)
                        }
                    } catch (e: Exception) {
                        Log.e("TAG", "uri = " + e.localizedMessage)
                    }
                }
            }
        }
    }

    private fun createPost(file: File?) {
        file?.let {
            presenter.createPost(
                token = getTokenFromSharedPref(),
                description = edit_text_view_dialog_create_collection_accept_sign.text.toString(),
                tags = EMPTY_STRING,
                hidden = false,
                file = file
            )
        }
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.frame_layout_dialog_create_collection_accept_user_search -> {
                userSearchDialog.show(childFragmentManager, "UserSearchDialog")
            }
        }
    }

    override fun customizeActionBar() {
        with(include_toolbar_dialog_create_collection) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_back_text_view.setOnClickListener {
                listener?.onChoice(it, null)
                dismiss()
            }

            toolbar_title_text_view.show()
            toolbar_title_text_view.text =
                context.getString(R.string.create_collection_create_outfit)

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.create_collection_ready)
            toolbar_right_text_text_view.setOnClickListener {
                currentModel?.text =
                    edit_text_view_dialog_create_collection_accept_sign.text.toString()
                currentModel?.title =
                    edit_text_view_dialog_create_collection_accept_sign.text.toString()
                chooserDialog.show(childFragmentManager, "ChooserDialog")
            }
        }
    }

    override fun initializeDependency() {
        (activity?.application as StylytsApp).applicationComponent.inject(dialog = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeViewsData() {
        chooserDialog = CreateCollectionChooserDialog().apply {
            setChoiceListener(listener = this@CreateCollectionAcceptDialog)
        }
        selectedUsers = ArrayList()
        userSearchDialog = UserSearchDialog()
    }

    override fun initializeViews() {}

    override fun initializeListeners() {
        chooserDialog.setChoiceListener(this)
        userSearchDialog.setChoiceListener(this)
        frame_layout_dialog_create_collection_accept_user_search.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey("collectionModel")) {
                currentModel = it.getParcelable("collectionModel")
            }
            if (it.containsKey("photoBitmap")) {
                currentBitmap = it.getParcelable("photoBitmap")
            }
            if (it.containsKey("photoUri")) {
                currentPhotoUri = it.getParcelable("photoUri")
            }
            if (it.containsKey("isChooser")) {
                isPhotoChooser = it.getBoolean("isChooser")
            }
            if (it.containsKey("clothes")) {
                selectedList = it.getParcelableArrayList("clothes")
            }
        }
    }

    override fun processPost(postModel: PostModel) {
        Log.d("TAG", postModel.toString())
    }

    fun setChoiceListener(listener: DialogChooserListener) {
        this.listener = listener
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
        if (isPhotoChooser) {
            frame_layout_dialog_create_collection_accept_choose_clothes.show()
            frame_layout_dialog_create_collection_accept_choose_clothes.setOnClickListener {
                listener?.onChoice(it, null)
                dismiss()
            }
            selectedList?.let {
                text_view_dialog_create_collection_items_count.text = "${it.count()} вещ."
            }
        }
    }

    private fun updateUserView() {
        text_view_dialog_create_collection_user_count.show()

        selectedUsers?.let {
            if (it.isNotEmpty())
                text_view_dialog_create_collection_user_count.text = "${it.count()} юзер."
        }
    }

    private fun getTokenFromSharedPref(): String {
        return (activity as MainActivity).getSharedPrefByKey(SharedConstants.TOKEN_KEY) ?: EMPTY_STRING
    }
}