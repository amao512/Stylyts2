package kz.eztech.stylyts.presentation.dialogs.collection_constructor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_clothes_grid.*
import kotlinx.android.synthetic.main.dialog_photo_chooser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.MotionItemModel
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.GridImageItemFilteredAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection.PhotoChooserContract
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CreateCollectionAcceptFragment
import kz.eztech.stylyts.presentation.interfaces.MotionViewTapListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection_constructor.PhotoChooserPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.ViewUtils.createBitmapScreenshot
import kz.eztech.stylyts.presentation.utils.extensions.displayToast
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import kz.eztech.stylyts.presentation.utils.stick.ImageEntity
import kz.eztech.stylyts.presentation.utils.stick.Layer
import kz.eztech.stylyts.presentation.utils.stick.MotionEntity
import javax.inject.Inject

class PhotoChooserDialog(
    private val chooserListener: DialogChooserListener? = null,
    private val selectedList: ArrayList<ClothesModel>,
    private val selectedUsers: ArrayList<UserModel>,
    private val currentMode: Int
) : DialogFragment(), PhotoChooserContract.View,
    UniversalViewClickListener,
    DialogChooserListener, MotionViewTapListener, View.OnClickListener {

    @Inject
    lateinit var presenter: PhotoChooserPresenter

    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var filteredAdapter: GridImageItemFilteredAdapter
    private lateinit var selectedAdapter: MainImagesAdditionalAdapter
    private lateinit var filterDialog: ConstructorFilterDialog

    private val filterMap = HashMap<String, Any>()

    private var photoUri: Uri? = null
    private var photoBitmap: Bitmap? = null
    private var mode: Int = CLOTHES_MODE

    companion object {
        private const val TOKEN_KEY = "token"
        const val CLOTHES_MODE = 0
        const val USERS_MODE = 1

        fun getNewInstance(
            token: String,
            chooserListener: DialogChooserListener?,
            clothesList: ArrayList<ClothesModel>,
            usersList: ArrayList<UserModel>,
            mode: Int
        ): PhotoChooserDialog {
            val dialog = PhotoChooserDialog(chooserListener, clothesList, usersList, mode)
            val bundle = Bundle()

            bundle.putString(TOKEN_KEY, token)
            dialog.arguments = bundle

            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_photo_chooser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeDependency()
        initializePresenter()
        initializeViews()
        initializeListeners()
        processPostInitialization()
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun customizeActionBar() {
        with(include_toolbar_photo_chooser) {
            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.done)
            toolbar_right_text_text_view.setOnClickListener(this@PhotoChooserDialog)

            toolbar_title_text_view.text = context.getString(R.string.photo_chooser_title)
            toolbar_title_text_view.show()
        }
    }

    override fun initializeDependency() {
        (activity?.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        // Request camera permissions
        filterDialog = ConstructorFilterDialog()
        filterDialog.setChoiceListener(this)

        photoUri?.let {
            updatePhoto(it)
        }

        photoBitmap?.let {
            Glide.with(image_view_fragment_photo_chooser.context)
                .load(it)
                .into(image_view_fragment_photo_chooser)
        }

        filteredAdapter = GridImageItemFilteredAdapter()
        filterAdapter = CollectionsFilterAdapter()
        selectedAdapter = MainImagesAdditionalAdapter()

        recycler_view_fragment_photo_chooser_filter_list.adapter = filterAdapter
        recycler_view_fragment_photo_chooser.adapter = filteredAdapter
        recycler_view_fragment_photo_chooser.addItemDecoration(
            GridSpacesItemDecoration(space = 16)
        )

        recycler_view_fragment_photo_chooser_selected_list.adapter = selectedAdapter

        motion_view_fragment_photo_chooser_tags_container.apply {
            setCustomDeleteIcon(R.drawable.ic_baseline_close_20)
            setFlexible(isFlexible = false)
            attachView(motionViewContract = this@PhotoChooserDialog)
            setTapListener(listener = this@PhotoChooserDialog)
        }

        dialog_photo_chooser_tap_text_view.text = when (mode) {
            CLOTHES_MODE -> getString(R.string.photo_chooser_touch)
            else -> getString(R.string.photo_user_choose_touch)
        }

        selectedList.map {
            Log.d("TAG", "clothes - ${it.id}")
            setClothesTag(clothesModel = it)
        }

        selectedUsers.map {
            Log.d("TAG", "users - ${it.id}")
            setUserTag(userModel = it)
        }

        hideBottomSheet()
    }

    override fun deleteSelectedView(motionEntity: MotionEntity) {
        when (motionEntity.item.item) {
            is ClothesModel -> {
                selectedList.remove(motionEntity.item.item)
                selectedAdapter.updateList(selectedList)
            }
            is UserModel -> selectedUsers.remove(motionEntity.item.item)
        }
        checkEmptyList()
    }

    override fun updatePhoto(path: Uri?) {
        path?.let {
            Glide.with(image_view_fragment_photo_chooser.context)
                .load(path)
                .into(image_view_fragment_photo_chooser)
        } ?: run {
            displayMessage(msg = getString(R.string.send_problem_something_went_wrong))
        }
    }

    override fun initializeListeners() {
        filterAdapter.setOnClickListener(this)
        filteredAdapter.setOnClickListener(this)
        selectedAdapter.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getCategory(token = getTokenFromArgs())
        presenter.getClothes(token = getTokenFromArgs())
    }

    override fun processTypesResults(resultsModel: ResultsModel<ClothesTypeModel>) {
        resultsModel.results.let {
            val preparedResults: MutableList<CollectionFilterModel> = mutableListOf()

            preparedResults.add(
                CollectionFilterModel(
                    name = getString(R.string.filter_list_filter),
                    id = 0,
                    mode = 1,
                    icon = R.drawable.ic_filter
                )
            )

            it.forEach { category ->
                preparedResults.add(
                    CollectionFilterModel(name = category.title, id = category.id, mode = 0)
                )
            }

            filterAdapter.updateList(preparedResults)
        }
    }

    override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
        filteredAdapter.updateList(list = resultsModel.results)
    }

    override fun getFilterMap(): HashMap<String, Any> = filterMap

    override fun onSingleTapUp() {
        when (mode) {
            CLOTHES_MODE -> showBottomSheet()
            USERS_MODE -> UserSearchDialog.getNewInstance(
                token = getTokenFromArgs(),
                chooserListener = this
            ).show(childFragmentManager, EMPTY_STRING)
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is CollectionFilterModel -> onFilterModelClicked(position, item)
            is ClothesModel -> onClothesClicked(view, item)
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        when (v?.id) {
            R.id.frame_layout_dialog_create_collection_accept_choose_clothes -> {
                linear_layout_fragment_photo_chooser_container.show()
                bottom_sheet_clothes.show()
            }
            R.id.toolbar_back_text_view -> {
                findNavController().navigateUp()
            }
            R.id.button_dialog_filter_constructor_submit -> {
                val map = item as HashMap<String, Any>
                val clothesType = filterMap["clothes_type"]

                filterMap.clear()
                filterMap["clothes_type"] = clothesType as String
                filterMap.putAll(map)

            }
        }

        when (item) {
            is UserModel -> setUserTag(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_right_text_text_view -> showCompleteDialog()
        }
    }

    fun setMode(mode: Int) {
        this.mode = mode
    }

    fun setPhotoUri(uri: Uri?) {
        this.photoUri = uri
    }

    fun setPhotoBitmap(bitmap: Bitmap?) {
        this.photoBitmap = bitmap
    }

    private fun onFilterModelClicked(
        position: Int,
        collectionFilterModel: CollectionFilterModel
    ) {
        when (collectionFilterModel.mode) {
            0 -> {
                collectionFilterModel.id?.let {
                    presenter.getClothes(
                        token = getTokenFromArgs(),
                        typeIdList = listOf(it)
                    )
                }

                if (position != 0) {
                    filterAdapter.onChooseItem(position)
                }
            }
            1 -> filterDialog.show(childFragmentManager, "FilterDialog")
        }
    }

    private fun onClothesClicked(
        view: View,
        clothesModel: ClothesModel
    ) {
        when (view.id) {
            R.id.shapeable_image_view_item_collection_image -> {
                setClothesTag(clothesModel)
                hideBottomSheet()
            }
            R.id.frame_layout_item_main_image_holder_container -> {
            }
        }
    }

    private fun setClothesTag(clothesModel: ClothesModel) {
        val layer = Layer()
        val textView = layoutInflater.inflate(
            R.layout.text_view_tag_element,
            motion_view_fragment_photo_chooser_tags_container,
            false
        ) as TextView

        textView.text = clothesModel.title
        motion_view_fragment_photo_chooser_tags_container.addView(textView)

        val observer = textView.viewTreeObserver

        if (observer.isAlive) {
            observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val resource = createBitmapScreenshot(textView)
                    val entity = ImageEntity(
                        layer,
                        resource,
                        item = MotionItemModel(item = clothesModel),
                        canvasHeight = motion_view_fragment_photo_chooser_tags_container.width,
                        canvasWidth = motion_view_fragment_photo_chooser_tags_container.height
                    )
                    motion_view_fragment_photo_chooser_tags_container.addEntityAndPosition(
                        entity,
                        true
                    )
                    textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    motion_view_fragment_photo_chooser_tags_container.removeView(textView)

                    selectedList.add(clothesModel)
                    selectedAdapter.updateList(selectedList)
                    checkEmptyList()
                }
            })
        } else {
            Log.wtf("observer", "not alive")
        }
    }

    private fun setUserTag(userModel: UserModel) {
        val layer = Layer()
        val textView = layoutInflater.inflate(
            R.layout.text_view_tag_element,
            motion_view_fragment_photo_chooser_tags_container,
            false
        ) as TextView

        textView.text = userModel.username
        motion_view_fragment_photo_chooser_tags_container.addView(textView)

        val observer = textView.viewTreeObserver

        if (observer.isAlive) {
            observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val resource = createBitmapScreenshot(textView)

                    val entity = ImageEntity(
                        layer,
                        resource,
                        item = MotionItemModel(item = userModel),
                        canvasHeight = motion_view_fragment_photo_chooser_tags_container.width,
                        canvasWidth = motion_view_fragment_photo_chooser_tags_container.height
                    )
                    motion_view_fragment_photo_chooser_tags_container.addEntityAndPosition(
                        entity,
                        true
                    )
                    textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    motion_view_fragment_photo_chooser_tags_container.removeView(textView)

                    selectedUsers.add(userModel)
                }
            })
        } else {
            Log.wtf("observer", "not alive")
        }
    }

    private fun showBottomSheet() {
        BottomSheetBehavior.from(bottom_sheet_clothes).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isHideable = false
        }
        recycler_view_fragment_photo_chooser_selected_list.hide()
        linear_layout_fragment_photo_chooser_desc_container.hide()
        text_view_fragment_photo_chooser_empty_text.hide()
        bottom_sheet_clothes.show()
    }

    private fun hideBottomSheet() {
        BottomSheetBehavior.from(bottom_sheet_clothes).apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        checkEmptyList()
    }

    private fun checkEmptyList() {
        if (selectedList.isEmpty()) {
            recycler_view_fragment_photo_chooser_selected_list.hide()
            linear_layout_fragment_photo_chooser_desc_container.hide()
        } else {
            linear_layout_fragment_photo_chooser_desc_container.show()
            recycler_view_fragment_photo_chooser_selected_list.show()
        }

        if (selectedList.isEmpty() && selectedUsers.isEmpty()) {
            text_view_fragment_photo_chooser_empty_text.show()
        } else {
            text_view_fragment_photo_chooser_empty_text.hide()
        }
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        view?.let {
            displayToast(it.context, msg)
        }
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    private fun showCompleteDialog() {
        val bundle = Bundle()

        bundle.putParcelable(CreateCollectionAcceptFragment.PHOTO_URI_KEY, photoUri)
        bundle.putBoolean(CreateCollectionAcceptFragment.IS_CHOOSER_KEY, true)

        bundle.putParcelableArrayList(CreateCollectionAcceptFragment.CLOTHES_KEY, selectedList)
        bundle.putParcelableArrayList(CreateCollectionAcceptFragment.USERS_KEY, selectedUsers)

        bundle.putInt(CreateCollectionAcceptFragment.MODE_KEY, currentMode)

        chooserListener?.onChoice(null, bundle)
        dismiss()
    }

    private fun getTokenFromArgs(): String = arguments?.getString(TOKEN_KEY) ?: EMPTY_STRING
}