package kz.eztech.stylyts.collection_constructor.presentation.ui.dialogs

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
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_clothes_grid.*
import kotlinx.android.synthetic.main.dialog_tag_chooser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.global.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.global.domain.models.motion.MotionItemModel
import kz.eztech.stylyts.global.domain.models.outfits.ItemLocationModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.collections.presentation.ui.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.ClothesAdditionalAdapter
import kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.GridImageItemFilteredAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.global.presentation.base.DialogChooserListener
import kz.eztech.stylyts.collection_constructor.presentation.contracts.TagChooserContract
import kz.eztech.stylyts.global.presentation.filter.ui.FilterDialog
import kz.eztech.stylyts.global.presentation.camera.CameraFragment
import kz.eztech.stylyts.collection_constructor.presentation.ui.fragments.CreateCollectionAcceptFragment
import kz.eztech.stylyts.global.presentation.common.interfaces.MotionViewTapListener
import kz.eztech.stylyts.global.presentation.common.interfaces.OnStartDragListener
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.global.presentation.userSearch.ui.UserSearchDialog
import kz.eztech.stylyts.collection_constructor.presentation.presenters.TagChooserPresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.RelativeMeasureUtil
import kz.eztech.stylyts.utils.ViewUtils.createBitmapScreenshot
import kz.eztech.stylyts.utils.extensions.displayToast
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.loadImage
import kz.eztech.stylyts.utils.extensions.show
import kz.eztech.stylyts.utils.helpers.SimpleItemTouchHelperCallback
import kz.eztech.stylyts.utils.stick.ImageEntity
import kz.eztech.stylyts.utils.stick.Layer
import kz.eztech.stylyts.utils.stick.MotionEntity
import javax.inject.Inject

class TagChooserDialog(
    private val chooserListener: DialogChooserListener? = null,
    private val currentMode: Int
) : DialogFragment(), TagChooserContract.View,
    UniversalViewClickListener,
    DialogChooserListener, MotionViewTapListener, View.OnClickListener, OnStartDragListener {

    @Inject lateinit var presenter: TagChooserPresenter

    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var clothesAdapter: GridImageItemFilteredAdapter
    private lateinit var selectedClothesAdapter: ClothesAdditionalAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var currentFilter: ClothesFilterModel
    private lateinit var searchFilterModel: SearchFilterModel
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val filterMap = HashMap<String, Any>()
    private val clothesEntities: MutableList<ImageEntity> = mutableListOf()
    private val userEntities: MutableList<ImageEntity> = mutableListOf()
    private val clothesList: MutableList<ClothesModel> = mutableListOf()
    private val usersList: MutableList<UserModel> = mutableListOf()

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
        currentFilter = ClothesFilterModel()
        currentFilter.gender = EMPTY_STRING
        currentFilter.onlyBrands = false

        searchFilterModel = SearchFilterModel()

        filterDialog = FilterDialog.getNewInstance(
            itemClickListener = this,
            gender = EMPTY_STRING,
            isShowWardrobe = true
        ).apply {
            setFilter(filterModel = currentFilter)
        }

        clothesAdapter = GridImageItemFilteredAdapter()
        filterAdapter = CollectionsFilterAdapter()
        selectedClothesAdapter = ClothesAdditionalAdapter(onStartDragListener = this)

        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(selectedClothesAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recycler_view_fragment_photo_chooser_selected_list)
    }

    override fun initializeViews() {
        photoUri?.let {
            updatePhoto(it)
        }

        photoBitmap?.loadImage(target = image_view_fragment_photo_chooser)

        recycler_view_fragment_photo_chooser_filter_list.adapter = filterAdapter
        recycler_view_fragment_photo_chooser.adapter = clothesAdapter
        recycler_view_fragment_photo_chooser.addItemDecoration(
            GridSpacesItemDecoration(space = 16)
        )

        recycler_view_fragment_photo_chooser_selected_list.adapter = selectedClothesAdapter

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
                clothesEntities.remove(motionEntity)
                clothesList.remove(motionEntity.item.item)
                selectedClothesAdapter.updateList(clothesList)
            }
            is UserModel -> {
                userEntities.remove(motionEntity)
                usersList.remove(motionEntity.item.item)
            }
        }
        checkEmptyList()
    }

    override fun updatePhoto(path: Uri?) {
        path?.loadImage(target = image_view_fragment_photo_chooser)
    }

    override fun initializeListeners() {
        filterAdapter.setOnClickListener(this)
        clothesAdapter.setOnClickListener(this)
        selectedClothesAdapter.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        handleRecyclerViewScrolling()
        handleSearchView()
    }

    override fun processTypesResults(resultsModel: ResultsModel<ClothesTypeModel>) {
        resultsModel.results.let {
            val preparedResults: MutableList<CollectionFilterModel> = mutableListOf()

            preparedResults.add(
                CollectionFilterModel(
                    name = getString(R.string.filter_list_filter),
                    id = 0,
                    mode = 0,
                    icon = R.drawable.ic_filter
                )
            )

            it.forEach { type ->
                preparedResults.add(
                    CollectionFilterModel(name = type.title, id = type.id, mode = 1, item = type)
                )
            }

            preparedResults.add(
                CollectionFilterModel(
                    id = 6,
                    name = getString(R.string.profile_add_to_wardrobe_by_barcode),
                    icon = R.drawable.ic_baseline_qr_code_2_24,
                    mode = 2
                )
            )
            preparedResults.add(
                CollectionFilterModel(
                    id = 7,
                    name = getString(R.string.profile_add_to_wardrobe_by_photo),
                    icon = R.drawable.ic_camera,
                    mode = 3
                )
            )

            filterAdapter.updateList(preparedResults)
        }
    }

    override fun getClothesFilter(): ClothesFilterModel = currentFilter

    override fun getSearchFilter(): SearchFilterModel = searchFilterModel

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processList(state.data)
            is Paginator.State.NewPageProgress<*> -> processList(state.data)
            else -> {}
        }

        hideProgress()
    }

    override fun processList(list: List<Any?>) {
        list.map { it!! }.let {
            clothesAdapter.updateList(list = it)
        }
    }

    override fun getFilterMap(): HashMap<String, Any> = filterMap

    override fun onSingleTapUp() {
        when (mode) {
            CLOTHES_MODE -> showBottomSheet()
            USERS_MODE -> UserSearchDialog.getNewInstance(chooserListener = this)
                .show(childFragmentManager, EMPTY_STRING)
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
            is ClothesFilterModel -> showFilterResults(filterModel = item)
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

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onStopDrag() {
        clothesEntities.clear()
        clothesList.clear()

        selectedClothesAdapter.getClothesList().map {
            setClothesTag(clothesModel = it)
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

    private fun handleRecyclerViewScrolling() {
        recycler_view_fragment_photo_chooser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recycler_view_fragment_photo_chooser.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMorePage()
                }
            }
        })
    }

    private fun handleSearchView() {
        bottom_sheet_dialog_clothes_grid_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchClothes(title = query ?: EMPTY_STRING)

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchClothes(title = newText ?: EMPTY_STRING)

                return false
            }
        })
    }

    private fun searchClothes(title: String) {
        clothesAdapter.clearList()
        searchFilterModel.query = title
        presenter.getList()
    }

    private fun onFilterModelClicked(
        position: Int,
        collectionFilterModel: CollectionFilterModel
    ) {
        when (collectionFilterModel.mode) {
            0 -> filterDialog.apply {
                setFilter(filterModel = currentFilter)
            }.show(childFragmentManager, "FilterDialog")
            1 -> {
                currentFilter.typeIdList = listOf(collectionFilterModel.item as ClothesTypeModel)
                clothesAdapter.clearList()
                presenter.getList()

                if (position != 0) {
                    filterAdapter.onChooseItem(position, isDisabledFirstPosition = false)
                }
            }
            2 -> navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
            3 -> navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
        }
    }

    private fun navigateToCameraFragment(mode: Int) {
        val bundle = Bundle()
        bundle.putInt(CameraFragment.MODE_KEY, mode)


        findNavController().navigate(R.id.action_createCollectionAcceptFragment_to_cameraFragment, bundle)
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

                    clothesEntities.add(entity)
                    clothesList.add(clothesModel)
                    selectedClothesAdapter.updateList(clothesList)
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

                    userEntities.add(entity)
                    usersList.add(userModel)
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
        clothesAdapter.clearList()
        presenter.getCategory()
        presenter.getList()

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
        if (clothesList.isEmpty()) {
            recycler_view_fragment_photo_chooser_selected_list.hide()
            linear_layout_fragment_photo_chooser_desc_container.hide()
            dialog_photo_chooser_clothes_tags_icon.hide()
        } else {
            linear_layout_fragment_photo_chooser_desc_container.show()
            recycler_view_fragment_photo_chooser_selected_list.show()
            dialog_photo_chooser_clothes_tags_icon.show()
        }

        if (usersList.isEmpty()) {
            dialog_photo_chooser_user_tags_icon.hide()
        } else {
            dialog_photo_chooser_user_tags_icon.show()
        }

        if (clothesList.isEmpty() && usersList.isEmpty()) {
            text_view_fragment_photo_chooser_empty_text.show()
        } else {
            text_view_fragment_photo_chooser_empty_text.hide()
        }
    }

    private fun showFilterResults(filterModel: ClothesFilterModel) {
        currentFilter = filterModel
        clothesAdapter.clearList()
        presenter.getList()
    }

    private fun showCompleteDialog() {
        val bundle = Bundle()

        bundle.putParcelable(CreateCollectionAcceptFragment.PHOTO_URI_KEY, photoUri)
        bundle.putBoolean(CreateCollectionAcceptFragment.IS_CHOOSER_KEY, true)

        bundle.putParcelableArrayList(
            CreateCollectionAcceptFragment.CLOTHES_KEY,
            setLocationToClothes()
        )
        bundle.putParcelableArrayList(
            CreateCollectionAcceptFragment.USERS_KEY,
            setLocationToUsers()
        )

        bundle.putInt(CreateCollectionAcceptFragment.MODE_KEY, currentMode)

        chooserListener?.onChoice(null, bundle)

        dismiss()
    }

    private fun setLocationToClothes(): ArrayList<ClothesModel> {
        val clothes = ArrayList<ClothesModel>()

        clothesEntities.forEachIndexed { index, imageView ->
            val result = RelativeMeasureUtil.measureEntity(
                imageView,
                motion_view_fragment_photo_chooser_tags_container
            )

            clothesList[index].clothesLocation = ItemLocationModel(
                id = (clothesEntities[index].item.item as ClothesModel).id,
                pointX = result.point_x.toDouble(),
                pointY = result.point_y.toDouble(),
                width = 0.0,
                height = 0.0,
                degree = 0.0
            )

            clothes.add(clothesList[index])
        }

        return clothes
    }

    private fun setLocationToUsers(): ArrayList<UserModel> {
        val users = ArrayList<UserModel>()

        userEntities.forEachIndexed { index, imageView ->
            val result = RelativeMeasureUtil.measureEntity(
                imageView,
                motion_view_fragment_photo_chooser_tags_container
            )

            usersList[index].userLocation = ItemLocationModel(
                id = (userEntities[index].item.item as UserModel).id,
                pointX = result.point_x.toDouble(),
                pointY = result.point_y.toDouble(),
                width = 0.0,
                height = 0.0,
                degree = 0.0
            )

            users.add(usersList[index])
        }

        return users
    }
}