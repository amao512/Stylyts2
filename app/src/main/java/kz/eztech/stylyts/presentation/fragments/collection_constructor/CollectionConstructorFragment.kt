package kz.eztech.stylyts.presentation.fragments.collection_constructor

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_collection_constructor.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.motion.MotionItemModel
import kz.eztech.stylyts.domain.models.outfits.ItemLocationModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection_constructor.CollectionConstructorShopCategoryAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.CollectionConstructorShopItemAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.StyleAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection_constructor.CollectionConstructorContract
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.fragments.camera.CameraFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewDoubleClickListener
import kz.eztech.stylyts.presentation.presenters.collection_constructor.CollectionConstructorPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.Paginator
import kz.eztech.stylyts.presentation.utils.RelativeImageMeasurements
import kz.eztech.stylyts.presentation.utils.RelativeMeasureUtil
import kz.eztech.stylyts.presentation.utils.RelativeMeasureUtil.reMeasureEntity
import kz.eztech.stylyts.presentation.utils.ViewUtils.createBitmapScreenshot
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import kz.eztech.stylyts.presentation.utils.stick.ImageEntity
import kz.eztech.stylyts.presentation.utils.stick.Layer
import kz.eztech.stylyts.presentation.utils.stick.MotionEntity
import java.io.File
import java.text.NumberFormat
import javax.inject.Inject

class CollectionConstructorFragment : BaseFragment<MainActivity>(),
    CollectionConstructorContract.View,
    View.OnClickListener,
    DialogChooserListener,
    UniversalViewClickListener, UniversalViewDoubleClickListener {

    @Inject lateinit var presenter: CollectionConstructorPresenter

    private lateinit var typesAdapter: CollectionConstructorShopCategoryAdapter
    private lateinit var clothesAdapter: CollectionConstructorShopItemAdapter
    private lateinit var stylesAdapter: StyleAdapter
    private lateinit var filterDialog: FilterDialog
    private lateinit var currentFilter: ClothesFilterModel

    private val listOfItems = ArrayList<ClothesModel>()
    private val listOfEntities = ArrayList<ImageEntity>()
    private val listOfIdsChosen = ArrayList<Int>()
    private val listOfTypes = ArrayList<ClothesTypeModel>()

    private var isItems = false
    private var isStyle = false
    private var currentId: Int = -1
    private var currentStyle: ClothesStyleModel? = null
    private var currentCollectionBitmap: Bitmap? = null

    companion object {
        const val CLOTHES_ITEMS_KEY = "items"
        const val MAIN_ID_KEY = "mainId"
        const val CURRENT_TYPE_KEY = "currentType"
        const val IS_UPDATING_KEY = "isUpdating"
    }

    override fun getLayoutId(): Int = R.layout.fragment_collection_constructor

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(CLOTHES_ITEMS_KEY)) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    it.getParcelableArrayList<ClothesModel>(CLOTHES_ITEMS_KEY)?.let { it1 ->
                        it1.forEach { clothesModel ->
                            processInputImageToPlace(clothesModel)
                        }
                    }
                }, 500)
            }
        }
    }

    override fun initializeViewsData() {
        linearLayout2.layoutTransition.setAnimateParentHierarchy(false)

        currentFilter = ClothesFilterModel()
        currentFilter.onlyBrands = true

        filterDialog = FilterDialog.getNewInstance(
            token = currentActivity.getTokenFromSharedPref(),
            itemClickListener = this,
            gender = getGender(),
            isShowWardrobe = true
        ).apply {
            setFilter(filterModel = currentFilter)
        }
        typesAdapter = CollectionConstructorShopCategoryAdapter(gender = getTypeFromArgs())
        clothesAdapter = CollectionConstructorShopItemAdapter()
        stylesAdapter = StyleAdapter()

        typesAdapter.itemClickListener = this
        clothesAdapter.itemClickListener = this
        clothesAdapter.itemDoubleClickListener = this
        stylesAdapter.itemClickListener = this
    }

    override fun initializeViews() {
        checkWritePermission()

        recycler_view_fragment_collection_constructor_list.itemAnimator = DefaultItemAnimator()
        recycler_view_fragment_collection_constructor_list.adapter = typesAdapter

        frame_layout_fragment_collection_constructor_images_container.attachView(motionViewContract = this)

        text_view_fragment_collection_constructor_category_back.isClickable = false
        text_view_fragment_collection_constructor_category_next.isClickable = false

        initializeBottomSheetBehaviorItems()
        processDraggedItems()
    }

    override fun initializeListeners() {
        text_view_fragment_collection_constructor_total_price.setOnClickListener(this)
        text_view_fragment_collection_constructor_category_back.setOnClickListener(this)
        text_view_fragment_collection_constructor_category_next.setOnClickListener(this)
        text_view_fragment_collection_constructor_category_filter.setOnClickListener(this)

    }

    override fun processPostInitialization() {
        presenter.getTypes()

        handleRecyclerView()
    }

    override fun disposeRequests() = presenter.disposeRequests()

    override fun displayMessage(msg: String) = displayToast(msg)

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        progress_bar_fragment_collection_constructor.show()
    }

    override fun hideProgress() {
        progress_bar_fragment_collection_constructor.hide()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_view_fragment_collection_constructor_category_back -> onCategoryBackClick()
            R.id.text_view_fragment_collection_constructor_category_next -> onCategoryNextClick()
            R.id.text_view_fragment_collection_constructor_category_filter -> {
                currentFilter.page = 1
                currentFilter.isLastPage = false
                filterDialog.apply {
                    setFilter(filterModel = currentFilter)
                }.show(childFragmentManager, "FilterDialog")
            }
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.item_collection_constructor_category_root_view -> {
                onCategoryItemImageClick(item)
            }
            R.id.item_collection_constructor_category_wide_root_view -> {
                onCategoryItemImageClick(item)
            }
            R.id.image_view_item_collection_constructor_clothes_item_image_holder -> {
                onCategoryItemImageClick(item)
            }
        }

        when (item) {
            is ClothesFilterModel -> showFilterResults(item)
            is ClothesStyleModel -> showStyles(item)
        }
    }

    override fun onViewDoubleClicked(
        view: View,
        position: Int,
        item: Any?,
        isDouble: Boolean?
    ) {
        when (view.id) {
            R.id.item_collection_constructor_category_root_view -> {
                onCategoryItemImageDoubleClick()
            }
            R.id.image_view_item_collection_constructor_clothes_item_image_holder -> {
                onCategoryBackClick()
            }
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        when (item) {
            is Int -> {
                when (item) {
                    1 -> navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
                    2 -> navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
                }
            }
        }
    }

    override fun processTypesResults(resultsModel: ResultsModel<ClothesTypeModel>) {
        processCategory(
            typesList = preparedTypes(list = resultsModel.results)
        )
    }

    override fun getToken(): String = currentActivity.getTokenFromSharedPref()

    override fun getClothesFilter(): ClothesFilterModel = currentFilter

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processList(state.data)
            is Paginator.State.NewPageProgress<*> -> processList(state.data)
            is Paginator.State.Empty -> {
                if (isItems || isStyle) {
                    hideProgress()
                    text_view_fragment_collection_constructor_category_next.hide()
                    text_view_fragment_collection_constructor_category_filter.show()

                    text_view_fragment_collection_constructor_category_back.show()
                    text_view_fragment_collection_constructor_category_back.isClickable = true
                }
            }
            else -> {}
        }
    }

    override fun processList(list: List<Any?>) {
        list.map { it!! }.let {
            when (it[0]) {
                is ClothesModel -> processClothes(list as List<ClothesModel>)
                is ClothesStyleModel -> processStyles(list as List<ClothesStyleModel>)
            }
        }

        hideProgress()
    }

    override fun processClothes(clothes: List<ClothesModel>) {
        if (listOfItems.isNotEmpty()) {
            listOfItems.forEach { item ->
                clothes.find { it.id == item.id }?.isChosen = true
            }
        }

        text_view_fragment_collection_constructor_category_next.hide()
        text_view_fragment_collection_constructor_category_filter.show()

        text_view_fragment_collection_constructor_category_back.show()
        text_view_fragment_collection_constructor_category_back.isClickable = true

        clothesAdapter.updateList(list = clothes)
        isItems = true
    }

    override fun processStyles(styles: List<ClothesStyleModel>) {
        stylesAdapter.updateList(list = styles)

        fragment_collection_constructor_styles_recycler_view.show()
    }

    override fun deleteSelectedView(motionEntity: MotionEntity) {
        val res = listOfEntities.remove(motionEntity)
        val res2 = listOfItems.remove(motionEntity.item.item as ClothesModel)
        val res3 = listOfIdsChosen.remove(currentId)

        processDraggedItems()
        typesAdapter.removeChosenPosition(typeId = (motionEntity.item.item).clothesCategory.clothesType.id)
        clothesAdapter.removeChosenPosition(clothesId = motionEntity.item.item.id)

        Log.wtf("deletedSelectedEntity", "res1:$res res2:$res2 res3:$res3")
    }

    override fun isItems(): Boolean = isItems

    override fun isStyles(): Boolean = isStyle

    private fun processInputImageToPlace(item: Any?) {
        item as ClothesModel

        clothesAdapter.choosePosition(clothesId = item.id)

        var photoUrl: String = EMPTY_STRING
        var currentSameObject: ImageEntity? = null

        if (item.constructorImage.isNotBlank()) {
            photoUrl = when (item.constructorImage.contains("http", true)) {
                true -> item.constructorImage
                else -> "http://178.170.221.31${item.constructorImage}"
            }
        } else if (item.coverImages.isNotEmpty()) {
            photoUrl = when (item.coverImages[0].contains("http", true)) {
                true -> item.coverImages[0]
                else -> "http://178.170.221.31${item.coverImages[0]}"
            }
        }

        if (listOfItems.isNotEmpty() && listOfEntities.isNotEmpty()) {
            listOfEntities.forEach {
                if (item.clothesCategory.bodyPart == (it.item.item as ClothesModel).clothesCategory.bodyPart) {
                    currentSameObject = it
                }
            }
        }

        currentId = item.clothesCategory.clothesType.id

        loadWithGlide(
            photoUrl = photoUrl,
            currentSameObject = currentSameObject,
            clothesModel = item
        )
    }

    private fun checkWritePermission() {
        val writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                currentActivity,
                writeExternalStorage
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    currentActivity,
                    writeExternalStorage
                )
            ) {
                ActivityCompat.requestPermissions(
                    currentActivity,
                    arrayOf(writeExternalStorage, readExternalStorage),
                    2
                )
            }
        }
    }

    private fun initializeBottomSheetBehaviorItems() {
        val bottomSheetBehavior =
            BottomSheetBehavior.from(fragment_collection_constructor_frame_layout_bottom_holder)

        view?.viewTreeObserver
            ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    bottomSheetBehavior.peekHeight =
                        fragment_collection_constructor_bottom_view.height

                    view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        recycler_view_fragment_collection_constructor_list.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                            )
                        typesAdapter.onCollapsed()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        recycler_view_fragment_collection_constructor_list.layoutManager =
                            GridLayoutManager(
                                requireContext(),
                                4,
                                GridLayoutManager.VERTICAL,
                                false
                            )
                        typesAdapter.onExpanded()
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun processDraggedItems() {
        val price = NumberFormat.getInstance().format(listOfItems.sumBy { it.cost })
        text_view_fragment_collection_constructor_total_price.text =
            getString(R.string.price_tenge_text_format, price)

        if (!isItems) {
            checkIsListEmpty()
            checkIsChosen()
        }
    }

    private fun onCategoryBackClick() {
        when {
            isStyle -> showStyles()
            isItems -> showItems()
            else -> {
                presenter.getTypes()
            }
        }
    }

    private fun showStyles() {
        recycler_view_fragment_collection_constructor_list.adapter = typesAdapter
        recycler_view_fragment_collection_constructor_list.show()

        fragment_collection_constructor_styles_recycler_view.hide()
        text_view_fragment_collection_constructor_category_back.visibility = View.INVISIBLE
        text_view_fragment_collection_constructor_category_back.isClickable = false

        isStyle = false
        isItems = false

        checkIsListEmpty()
        presenter.getTypes()
    }

    private fun showItems() {
        checkIsListEmpty()

        text_view_fragment_collection_constructor_category_filter.hide()
        recycler_view_fragment_collection_constructor_list.adapter = typesAdapter
        text_view_fragment_collection_constructor_category_back.visibility = View.INVISIBLE
        text_view_fragment_collection_constructor_category_back.isClickable = false

        isItems = false
        isStyle = false

        presenter.getTypes()
    }

    private fun onCategoryNextClick() {
        if (isStyle) {
            processPostImages()
        } else if (listOfItems.isNotEmpty()) {
            text_view_fragment_collection_constructor_category_back.show()
            text_view_fragment_collection_constructor_category_back.isClickable = true

            fragment_collection_constructor_styles_recycler_view.adapter = stylesAdapter
            recycler_view_fragment_collection_constructor_list.hide()

            isStyle = true
            isItems = false
            presenter.getClothesAndStyles()
        }
    }

    private fun onCategoryItemImageClick(item: Any?) {
        if (isItems) {
            processInputImageToPlace(item)
        } else {
            (item as ClothesTypeModel).run {
                if (isExternal) {
                    onChoice(view, externalType)
                } else {
                    clothesAdapter.clearList()
                    currentFilter.typeIdList = listOf(item)
                    currentFilter.gender = when (getTypeFromArgs()) {
                        0 -> GenderEnum.MALE.gender
                        else -> GenderEnum.FEMALE.gender
                    }

                    recycler_view_fragment_collection_constructor_list.adapter = clothesAdapter

                    isItems = true
                    isStyle = false
                    presenter.getClothesAndStyles()
                }
            }
        }
    }

    private fun handleRecyclerView() {
        val recyclerView = recycler_view_fragment_collection_constructor_list

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isItems || isStyle) {
                        presenter.loadMorePage()
                    }
                }
            }
        })
    }

    private fun onCategoryItemImageDoubleClick() {
        if (isItems) {
            checkIsListEmpty()

            text_view_fragment_collection_constructor_category_filter.hide()
            recycler_view_fragment_collection_constructor_list.adapter = typesAdapter

            text_view_fragment_collection_constructor_category_back.hide()
            text_view_fragment_collection_constructor_category_back.isClickable = false

            isItems = false
            isStyle = false

            presenter.getTypes()
        }
    }

    private fun removeSelectedEntityFromMotionView(item: Any?) {
        item as ClothesModel

        frame_layout_fragment_collection_constructor_images_container.getEntities().map {
            if ((it.item.item as ClothesModel).id == item.id) {
                frame_layout_fragment_collection_constructor_images_container.removeEntity(it)
            }
        }
    }

    private fun showFilterResults(filterModel: ClothesFilterModel) {
        currentFilter = filterModel
        clothesAdapter.clearList()

        presenter.getClothesAndStyles()
    }

    private fun showStyles(clothesStyleModel: ClothesStyleModel) {
        currentStyle = clothesStyleModel
        processPostImages()
    }

    private fun navigateToCameraFragment(mode: Int) {
        val bundle = Bundle()
        bundle.putInt(CameraFragment.MODE_KEY, mode)

        findNavController().navigate(
            R.id.action_createCollectionFragment_to_cameraFragment,
            bundle
        )
    }

    private fun preparedTypes(list: List<ClothesTypeModel>): List<ClothesTypeModel> {
        val preparedResults: MutableList<ClothesTypeModel> = mutableListOf()
        preparedResults.addAll(list)

        preparedResults.add(
            ClothesTypeModel(
                id = list.size + 1,
                title = getString(R.string.collection_constructor_add_by_barcode),
                isExternal = true,
                externalType = 1,
                isChoosen = false,
                isWiden = true,
                externalImageId = R.drawable.ic_baseline_qr_code_2_24
            )
        )
        preparedResults.add(
            ClothesTypeModel(
                id = list.size + 1,
                title = getString(R.string.collection_constructor_add_by_photo),
                isExternal = true,
                externalType = 2,
                isChoosen = false,
                isWiden = true,
                externalImageId = R.drawable.ic_camera
            )
        )

        return preparedResults
    }

    private fun processCategory(typesList: List<ClothesTypeModel>) {
        if (listOfIdsChosen.isNotEmpty()) {
            listOfIdsChosen.forEach { type ->
                typesList.find { it.id == type }?.isChoosen = true
            }
        }

        listOfTypes.clear()
        listOfTypes.addAll(typesList)

        typesAdapter.updateList(listOfTypes)
    }

    private fun processPostImages() {
        if (listOfItems.isNotEmpty() && listOfEntities.isNotEmpty() && currentStyle != null) {
            val constructorImagesContainer =
                frame_layout_fragment_collection_constructor_images_container
            val clothesList = ArrayList<ClothesModel>()
            val clothesCollectionList = getClothesCollectionList(clothesList = clothesList)

            val outfitCreateModel = OutfitCreateModel(
                style = currentStyle!!.id,
                clothesIdList = clothesList.map { it.id },
                clothes = clothesList,
                itemLocation = clothesCollectionList,
                authorId = currentActivity.getUserIdFromSharedPref(),
                totalPrice = listOfItems.sumBy { it.cost }.toFloat(),
                text = EMPTY_STRING,
                title = EMPTY_STRING
            )

            constructorImagesContainer.unselectEntity()
            currentCollectionBitmap = createBitmapScreenshot(constructorImagesContainer)

            showCreateCollectionDialog(outfitCreateModel)
        } else {
            displayMessage(msg = getString(R.string.collection_constructor_not_all_data_filled))
        }
    }

    private fun successAddedNavigateUp() {
        displayMessage(msg = getString(R.string.collection_constructor_success_added))
        findNavController().navigateUp()
    }

    private fun loadWithGlide(
        photoUrl: String,
        currentSameObject: ImageEntity?,
        clothesModel: ClothesModel
    ) {
        Glide.with(currentActivity.applicationContext)
            .asFile()
            .load(photoUrl)
            .into(object : CustomTarget<File>() {

                override fun onResourceReady(
                    file: File,
                    transition: Transition<in File>?
                ) {
                    val options = BitmapFactory.Options()

                    frame_layout_fragment_collection_constructor_images_container_placeholder.hide()

                    options.inMutable = true

                    frame_layout_fragment_collection_constructor_images_container.post {
                        runImagesContainer(
                            currentSameObject = currentSameObject,
                            clothesModel = clothesModel,
                            resource = BitmapFactory.decodeFile(file.path, options)
                        )

                        processDraggedItems()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun checkIsListEmpty() {
        if (listOfItems.isNotEmpty()) {
            text_view_fragment_collection_constructor_category_next.show()
        }
    }

    private fun checkIsChosen() {
        if (listOfIdsChosen.isNotEmpty()) {
            listOfIdsChosen.forEach { type ->
                listOfTypes.find { it.id == type }?.isChoosen = true
            }
        }
    }

    private fun getClothesCollectionList(clothesList: ArrayList<ClothesModel>): ArrayList<ItemLocationModel> {
        val clothesCollectionList = ArrayList<ItemLocationModel>()

        listOfEntities.forEachIndexed { index, imageView ->
            val result = RelativeMeasureUtil.measureEntity(
                imageView,
                frame_layout_fragment_collection_constructor_images_container
            )

            clothesCollectionList.add(
                ItemLocationModel(
                    id = listOfItems[index].id,
                    pointX = result.point_x.toDouble(),
                    pointY = result.point_y.toDouble(),
                    width = result.width.toDouble(),
                    height = result.height.toDouble(),
                    degree = result.degree.toDouble()
                )
            )

            clothesList.add(listOfItems[index])
        }

        return clothesCollectionList
    }

    private fun showCreateCollectionDialog(outfitCreateModel: OutfitCreateModel) {
        val bundle = Bundle()

        bundle.apply {
            putParcelable(CreateCollectionAcceptFragment.OUTFIT_MODEL_KEY, outfitCreateModel)
            putInt(CreateCollectionAcceptFragment.ID_KEY, getMainIdFromArgs())
            putBoolean(CreateCollectionAcceptFragment.IS_UPDATING_KEY, isUpdating())
            putParcelable(
                CreateCollectionAcceptFragment.PHOTO_BITMAP_KEY,
                currentCollectionBitmap
            )
            putInt(
                CreateCollectionAcceptFragment.MODE_KEY,
                CreateCollectionAcceptFragment.OUTFIT_MODE
            )
        }
        findNavController().navigate(
            R.id.createCollectionAcceptFragment,
            bundle
        )
    }

    private fun runImagesContainer(
        currentSameObject: ImageEntity?,
        clothesModel: ClothesModel,
        resource: Bitmap
    ) {
        currentSameObject?.let {
            frame_layout_fragment_collection_constructor_images_container.deleteEntity(entity = it)
        }

        val measurements = getRelativeImageMeasurements(clothesModel)
        val entity = getImageEntity(resource, clothesModel)

        frame_layout_fragment_collection_constructor_images_container.addEntityAndPosition(entity = entity)

        currentSameObject?.let {
            entity.layer.scale = it.layer.scale
            entity.layer.rotationInDegrees = it.layer.rotationInDegrees
            entity.moveToPoint(PointF(it.absoluteCenterX(), it.absoluteCenterY()))
        }

        measurements?.let {
            entity.moveToPoint(PointF(it.point_x, it.point_y))
            entity.layer.postScaleRaw(it.width / resource.width)
            entity.layer.postRotate(it.degree)
        }

        listOfItems.add(clothesModel)
        listOfEntities.add(entity)
        listOfIdsChosen.add(currentId)

        checkIsChosen()
        typesAdapter.notifyDataSetChanged()
    }

    private fun getRelativeImageMeasurements(
        clothesModel: ClothesModel
    ): RelativeImageMeasurements? {
        var measurements: RelativeImageMeasurements? = null

        clothesModel.clothesLocation?.let {
            measurements = reMeasureEntity(
                RelativeImageMeasurements(
                    it.pointX.toFloat(),
                    it.pointY.toFloat(),
                    it.width.toFloat(),
                    it.height.toFloat(),
                    it.degree.toFloat()
                ),
                frame_layout_fragment_collection_constructor_images_container
            )
        }

        return measurements
    }

    private fun getImageEntity(
        resource: Bitmap,
        clothesModel: ClothesModel
    ): ImageEntity = ImageEntity(
        layer = Layer(),
        bitmap = resource,
        item = MotionItemModel(item = clothesModel),
        canvasWidth = frame_layout_fragment_collection_constructor_images_container.width,
        canvasHeight = frame_layout_fragment_collection_constructor_images_container.height
    )

    private fun getGender(): String = when (getTypeFromArgs()) {
        0 -> GenderEnum.MALE.gender
        else -> GenderEnum.FEMALE.gender
    }

    private fun isUpdating(): Boolean = arguments?.getBoolean(IS_UPDATING_KEY) ?: false

    private fun getTypeFromArgs(): Int = arguments?.getInt(CURRENT_TYPE_KEY) ?: 0

    private fun getMainIdFromArgs(): Int = arguments?.getInt(MAIN_ID_KEY) ?: -1
}