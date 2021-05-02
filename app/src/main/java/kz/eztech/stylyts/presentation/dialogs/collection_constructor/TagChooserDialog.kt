package kz.eztech.stylyts.presentation.dialogs.collection_constructor

import android.graphics.Bitmap
import android.graphics.PointF
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
import kotlinx.android.synthetic.main.dialog_tag_chooser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.motion.MotionItemModel
import kz.eztech.stylyts.domain.models.outfits.ItemLocationModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.adapters.collection.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.GridImageItemFilteredAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection_constructor.TagChooserContract
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CreateCollectionAcceptFragment
import kz.eztech.stylyts.presentation.interfaces.MotionViewTapListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection_constructor.TagChooserPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.RelativeMeasureUtil
import kz.eztech.stylyts.presentation.utils.ViewUtils.createBitmapScreenshot
import kz.eztech.stylyts.presentation.utils.extensions.displayToast
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import kz.eztech.stylyts.presentation.utils.stick.ImageEntity
import kz.eztech.stylyts.presentation.utils.stick.Layer
import kz.eztech.stylyts.presentation.utils.stick.MotionEntity
import javax.inject.Inject

class TagChooserDialog(
    private val chooserListener: DialogChooserListener? = null,
    private val currentMode: Int
) : DialogFragment(), TagChooserContract.View,
    UniversalViewClickListener,
    DialogChooserListener, MotionViewTapListener, View.OnClickListener {

    @Inject lateinit var presenter: TagChooserPresenter

    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var filteredAdapter: GridImageItemFilteredAdapter
    private lateinit var selectedAdapter: MainImagesAdditionalAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var currentFilter: FilterModel

    private val filterMap = HashMap<String, Any>()
    private val selectedClothesEntities: MutableList<ImageEntity> = mutableListOf()
    private val selectedUserEntities: MutableList<ImageEntity> = mutableListOf()
    private val selectedList: MutableList<ClothesModel> = mutableListOf()
    private val selectedUsers: MutableList<UserModel> = mutableListOf()

    private var photoUri: Uri? = null
    private var photoBitmap: Bitmap? = null
    private var mode: Int = CLOTHES_MODE

    companion object {
        private const val TOKEN_KEY = "token"
        private const val CLOTHES_KEY = "clothes"
        private const val USERS_KEY = "users"

        const val CLOTHES_MODE = 0
        const val USERS_MODE = 1

        fun getNewInstance(
            token: String,
            chooserListener: DialogChooserListener?,
            clothesList: ArrayList<ClothesModel>,
            usersList: ArrayList<UserModel>,
            mode: Int
        ): TagChooserDialog {
            val dialog = TagChooserDialog(chooserListener, mode)
            val bundle = Bundle()

            bundle.putString(TOKEN_KEY, token)
            bundle.putParcelableArrayList(CLOTHES_KEY, clothesList)
            bundle.putParcelableArrayList(USERS_KEY, usersList)
            dialog.arguments = bundle

            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_tag_chooser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customizeActionBar()
        initializeDependency()
        initializePresenter()
        initializeArguments()
        initializeViewsData()
        initializeViews()
        initializeListeners()
        processPostInitialization()
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun customizeActionBar() {
        with(include_toolbar_photo_chooser) {
            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.done)
            toolbar_right_text_text_view.setOnClickListener(this@TagChooserDialog)

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

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(CLOTHES_KEY)) {
                it.getParcelableArrayList<ClothesModel>(CLOTHES_KEY)?.map { clothes ->
                    setClothesTag(clothesModel = clothes)
                }
            }

            if (it.containsKey(USERS_KEY)) {
                it.getParcelableArrayList<UserModel>(USERS_KEY)?.map { users ->
                    setUserTag(userModel = users)
                }
            }
        }
    }

    override fun initializeViewsData() {
        // Request camera permissions
        currentFilter = FilterModel()
        filterDialog = FilterDialog.getNewInstance(
            token = getTokenFromArgs(),
            itemClickListener = this,
            gender = GenderEnum.MALE.gender,
            isShowWardrobe = true
        ).apply {
            setFilter(filterModel = currentFilter)
        }

        filteredAdapter = GridImageItemFilteredAdapter()
        filterAdapter = CollectionsFilterAdapter()
        selectedAdapter = MainImagesAdditionalAdapter()
    }

    override fun initializeViews() {
        photoUri?.let {
            updatePhoto(it)
        }

        photoBitmap?.let {
            Glide.with(image_view_fragment_photo_chooser.context)
                .load(it)
                .into(image_view_fragment_photo_chooser)
        }

        with (bottom_sheet_clothes) {
            recycler_view_fragment_photo_chooser_filter_list.adapter = filterAdapter
            recycler_view_fragment_photo_chooser.adapter = filteredAdapter
            recycler_view_fragment_photo_chooser.addItemDecoration(
                GridSpacesItemDecoration(space = 16)
            )
        }

        recycler_view_fragment_photo_chooser_selected_list.adapter = selectedAdapter

        motion_view_fragment_photo_chooser_tags_container.apply {
            setCustomDeleteIcon(R.drawable.ic_baseline_close_20)
            setFlexible(isFlexible = false)
            attachView(motionViewContract = this@TagChooserDialog)
            setTapListener(listener = this@TagChooserDialog)
        }

        dialog_photo_chooser_tap_text_view.text = when (mode) {
            CLOTHES_MODE -> getString(R.string.photo_chooser_touch)
            else -> getString(R.string.photo_user_choose_touch)
        }

        hideBottomSheet()
    }

    override fun deleteSelectedView(motionEntity: MotionEntity) {
        when (motionEntity.item.item) {
            is ClothesModel -> {
                selectedClothesEntities.remove(motionEntity)
                selectedList.remove(motionEntity.item.item)
                selectedAdapter.updateList(selectedList)
            }
            is UserModel -> {
                selectedUserEntities.remove(motionEntity)
                selectedUsers.remove(motionEntity.item.item)
            }
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

    override fun processPostInitialization() {}

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
            is FilterModel -> {
                currentFilter = item
                presenter.getClothes(
                    token = getTokenFromArgs(),
                    filterModel = currentFilter
                )
            }
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
//                val map = item as HashMap<String, Any>
//                val clothesType = filterMap["clothes_type"]
//
//                filterMap.clear()
//                filterMap["clothes_type"] = clothesType as String
//                filterMap.putAll(map)
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
                    currentFilter.typeIdList = listOf(it)

                    presenter.getClothes(
                        token = getTokenFromArgs(),
                        filterModel = currentFilter
                    )
                }

                if (position != 0) {
                    filterAdapter.onChooseItem(position)
                }
            }
            1 -> filterDialog.apply {
                currentFilter.page = 1
                currentFilter.isLastPage = false
                setFilter(filterModel = currentFilter)
            }.show(childFragmentManager, "FilterDialog")
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
            R.id.frame_layout_item_main_image_holder_container -> {}
        }
    }

    private fun setClothesTag(clothesModel: ClothesModel) {
        val layer = Layer()
        val textView = getTagTextView(motion_view_fragment_photo_chooser_tags_container)

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

                    clothesModel.clothesLocation?.let {
                        entity.setPoint(
                            PointF(it.pointX.toFloat(), it.pointY.toFloat())
                        )
                    }

                    textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    motion_view_fragment_photo_chooser_tags_container.removeView(textView)

                    selectedClothesEntities.add(entity)
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
        val textView = getTagTextView(motion_view_fragment_photo_chooser_tags_container)
        textView.backgroundTintList = resources.getColorStateList(R.color.app_dark_blue_gray)
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

                    userModel.userLocation?.let {
                        entity.setPoint(
                            PointF(it.pointX.toFloat(), it.pointY.toFloat())
                        )
                    }

                    textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    motion_view_fragment_photo_chooser_tags_container.removeView(textView)

                    selectedUserEntities.add(entity)
                    selectedUsers.add(userModel)
                    checkEmptyList()
                }
            })
        } else {
            Log.wtf("observer", "not alive")
        }
    }

    private fun getTagTextView(container: ViewGroup): TextView {
        return layoutInflater.inflate(
            R.layout.text_view_tag_element,
            container,
            false
        ) as TextView
    }

    private fun showBottomSheet() {
        presenter.getCategory(token = getTokenFromArgs())
        presenter.getClothes(
            token = getTokenFromArgs(),
            filterModel = currentFilter
        )

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
            dialog_photo_chooser_clothes_tags_icon.hide()
        } else {
            linear_layout_fragment_photo_chooser_desc_container.show()
            recycler_view_fragment_photo_chooser_selected_list.show()
            dialog_photo_chooser_clothes_tags_icon.show()
        }

        if (selectedUsers.isEmpty()) {
            dialog_photo_chooser_user_tags_icon.hide()
        } else {
            dialog_photo_chooser_user_tags_icon.show()
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

        bundle.putParcelableArrayList(CreateCollectionAcceptFragment.CLOTHES_KEY, setLocationToClothes())
        bundle.putParcelableArrayList(CreateCollectionAcceptFragment.USERS_KEY, setLocationToUsers())

        bundle.putInt(CreateCollectionAcceptFragment.MODE_KEY, currentMode)

        chooserListener?.onChoice(null, bundle)

        dismiss()
    }

    private fun setLocationToClothes(): ArrayList<ClothesModel> {
        val clothes = ArrayList<ClothesModel>()

        selectedClothesEntities.forEachIndexed { index, imageView ->
            val result = RelativeMeasureUtil.measureEntity(
                imageView,
                motion_view_fragment_photo_chooser_tags_container
            )

            selectedList[index].clothesLocation = ItemLocationModel(
                id = (selectedClothesEntities[index].item.item as ClothesModel).id,
                pointX = result.point_x.toDouble(),
                pointY = result.point_y.toDouble(),
                width = 0.0,
                height = 0.0,
                degree = 0.0
            )

            clothes.add(selectedList[index])
        }

        return clothes
    }

    private fun setLocationToUsers(): ArrayList<UserModel> {
        val users = ArrayList<UserModel>()

        selectedUserEntities.forEachIndexed { index, imageView ->
            val result = RelativeMeasureUtil.measureEntity(
                imageView,
                motion_view_fragment_photo_chooser_tags_container
            )

            selectedUsers[index].userLocation = ItemLocationModel(
                id = (selectedUserEntities[index].item.item as UserModel).id,
                pointX = result.point_x.toDouble(),
                pointY = result.point_y.toDouble(),
                width = 0.0,
                height = 0.0,
                degree = 0.0
            )

            users.add(selectedUsers[index])
        }

        return users
    }

    private fun getTokenFromArgs(): String = arguments?.getString(TOKEN_KEY) ?: EMPTY_STRING
}