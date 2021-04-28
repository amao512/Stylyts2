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
import android.widget.ArrayAdapter
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
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.models.motion.MotionItemModel
import kz.eztech.stylyts.domain.models.outfits.ItemLocationModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection_constructor.CollectionConstructorShopCategoryAdapter
import kz.eztech.stylyts.presentation.adapters.collection_constructor.CollectionConstructorShopItemAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection_constructor.CollectionConstructorContract
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.fragments.camera.CameraFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewDoubleClickListener
import kz.eztech.stylyts.presentation.presenters.collection_constructor.CollectionConstructorPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
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
    private lateinit var itemAdapter: CollectionConstructorShopItemAdapter
	private lateinit var filterDialog: FilterDialog
	private lateinit var currentFilter: FilterModel

    private val listOfItems = ArrayList<ClothesModel>()
    private val listOfEntities = ArrayList<ImageEntity>()
    private val listOfIdsChosen = ArrayList<Int>()
    private val listOfTypes = ArrayList<ClothesTypeModel>()

	private var isItems = false
	private var isStyle = false
	private var currentType = 0
	private var currentSaveMode = 1
	private var currentId: Int = -1
	private var currentStyle: ClothesStyleModel? = null
	private var currentCollectionBitmap: Bitmap? = null
	private var currentMainId: Int = -1

	companion object {
		const val CLOTHES_ITEMS_KEY = "items"
		const val MAIN_ID_KEY = "mainId"
		const val CURRENT_TYPE_KEY = "currentType"
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

            if (it.containsKey(MAIN_ID_KEY)) {
                currentMainId = it.getInt(MAIN_ID_KEY)
            }

            if (it.containsKey(CURRENT_TYPE_KEY)) {
                currentType = it.getInt(CURRENT_TYPE_KEY)
            }
        }
    }

    override fun initializeViewsData() {
        linearLayout2.layoutTransition.setAnimateParentHierarchy(false)

		currentFilter = FilterModel()
		filterDialog = FilterDialog.getNewInstance(
			token = getTokenFromSharedPref(),
			itemClickListener = this,
			gender = getGender(),
			isShowWardrobe = true
		)
		typesAdapter = CollectionConstructorShopCategoryAdapter(gender = currentType)
		itemAdapter = CollectionConstructorShopItemAdapter()

		typesAdapter.itemClickListener = this
		itemAdapter.itemClickListener = this
		itemAdapter.itemDoubleClickListener = this
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

    override fun processPostInitialization() = presenter.getTypes(token = getTokenFromSharedPref())

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
				filterDialog.show(childFragmentManager, "FilterDialog")
			}
		}
	}

    override fun onViewClicked(
		view: View,
		position: Int,
		item: Any?
	) {
        when (view.id) {
			R.id.image_view_item_collection_constructor_category_item_image_holder -> {
				onCategoryItemImageClick(item)
			}
			R.id.image_view_item_collection_constructor_clothes_item_image_holder -> {
				removeSelectedEntityFromMotionView(item)
			}
			R.id.toolbar_left_corner_action_image_button -> showFilterResults(item)
        }
    }

    override fun onViewDoubleClicked(
		view: View,
		position: Int,
		item: Any?,
		isDouble: Boolean?
	) {
        when (view.id) {
			R.id.image_view_item_collection_constructor_category_item_image_holder -> {
				onCategoryItemImageDoubleClick()
			}
			R.id.image_view_item_collection_constructor_clothes_item_image_holder -> {
				onCategoryItemImageClick(item)
			}
        }
    }

    override fun onChoice(v: View?, item: Any?) {
		when (item) {
			is Int -> {
				when (item) {
					1 -> {/* add category */}
					2 -> navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
					3 -> navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
				}
			}
		}
    }

	override fun processTypesResults(resultsModel: ResultsModel<ClothesTypeModel>) {
		processCategory(
			typesList = preparedTypes(list = resultsModel.results)
		)
	}

	override fun processClothesResults(resultsModel: ResultsModel<ClothesModel>) {
		if (listOfItems.isNotEmpty()) {
			listOfItems.forEach { clothes ->
				resultsModel.results.find { it.id == clothes.id }?.isChosen = true
			}
		}

		text_view_fragment_collection_constructor_category_next.hide()
		text_view_fragment_collection_constructor_category_filter.show()

		text_view_fragment_collection_constructor_category_back.show()
		text_view_fragment_collection_constructor_category_back.isClickable = true
		recycler_view_fragment_collection_constructor_list.adapter = itemAdapter

		itemAdapter.updateList(list = resultsModel.results)
		isItems = true
	}

	override fun processStylesResults(resultsModel: ResultsModel<ClothesStyleModel>) {
		val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
			currentActivity, R.layout.item_style, resultsModel.results.map { it.title }
		)
		recycler_view_fragment_collection_constructor_list.hide()

		list_view_fragment_collection_constructor_list_style.adapter = adapter
		list_view_fragment_collection_constructor_list_style.show()
		list_view_fragment_collection_constructor_list_style.setOnItemClickListener { _, _, position, _ ->
			currentStyle = resultsModel.results[position]
			processPostImages()
		}
	}

	override fun deleteSelectedView(motionEntity: MotionEntity) {
		val res = listOfEntities.remove(motionEntity)
		val res2 = listOfItems.remove(motionEntity.item.item as ClothesModel)
		val res3 = listOfIdsChosen.remove(currentId)

		processDraggedItems()
		typesAdapter.removeChosenPosition(typeId = (motionEntity.item.item).clothesCategory.clothesType.id)
		itemAdapter.removeChosenPosition(clothesId = motionEntity.item.item.id)

		Log.wtf("deletedSelectedEntity", "res1:$res res2:$res2 res3:$res3")
	}

	private fun processInputImageToPlace(item: Any?) {
		item as ClothesModel

		itemAdapter.choosePosition(clothesId = item.id)

		var photoUrl: String
		var currentSameObject: ImageEntity? = null

		item.constructorImage.let { coverPhoto ->
			photoUrl = when (coverPhoto.contains("http", true)) {
				true -> coverPhoto
				else -> "http://178.170.221.31${coverPhoto}"
			}
		}

		if (listOfItems.isNotEmpty() && listOfEntities.isNotEmpty()) {
			listOfEntities.forEach {
				if (item.clothesCategory.bodyPart == (it.item.item as ClothesModel).clothesCategory.bodyPart) {
					currentSameObject = it
				}
			}
		}

		loadWithGlide(
			photoUrl = photoUrl,
			currentSameObject = currentSameObject,
			clothesModel = item
		)
	}

	private fun checkWritePermission() {
		val writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE
		val readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE

		if (ContextCompat.checkSelfPermission(currentActivity, writeExternalStorage) != PackageManager.PERMISSION_GRANTED) {
			if (!ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, writeExternalStorage)) {
				ActivityCompat.requestPermissions(
					currentActivity,
					arrayOf(writeExternalStorage, readExternalStorage),
					2
				)
			}
		}
	}

	private fun initializeBottomSheetBehaviorItems() {
		val bottomSheetBehavior = BottomSheetBehavior.from(fragment_collection_constructor_frame_layout_bottom_holder)

		bottomSheetBehavior.peekHeight = 370
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

		bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
			override fun onStateChanged(bottomSheet: View, newState: Int) {
				when (newState) {
					BottomSheetBehavior.STATE_COLLAPSED -> {
						recycler_view_fragment_collection_constructor_list.layoutManager = LinearLayoutManager(
							requireContext(),
							RecyclerView.HORIZONTAL,
							false
						)
					}
					BottomSheetBehavior.STATE_EXPANDED -> {
						recycler_view_fragment_collection_constructor_list.layoutManager = GridLayoutManager(
							requireContext(),
							4,
							GridLayoutManager.VERTICAL,
							false
						)
					}
					else -> {}
				}
			}

			override fun onSlide(bottomSheet: View, slideOffset: Float) {}
		})
	}

	private fun processDraggedItems() {
		val price = NumberFormat.getInstance().format(listOfItems.sumBy { it.cost })
		text_view_fragment_collection_constructor_total_price.text = getString(R.string.price_tenge_text_format, price)

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
				presenter.getTypes(token = getTokenFromSharedPref())
			}
		}
	}

	private fun showStyles() {
		recycler_view_fragment_collection_constructor_list.adapter = typesAdapter
		recycler_view_fragment_collection_constructor_list.show()

		list_view_fragment_collection_constructor_list_style.hide()
		text_view_fragment_collection_constructor_category_back.visibility = View.INVISIBLE
		text_view_fragment_collection_constructor_category_back.isClickable = false

		isStyle = false
		isItems = false

		checkIsListEmpty()
		presenter.getTypes(token = getTokenFromSharedPref())
	}

	private fun showItems() {
		checkIsListEmpty()

		text_view_fragment_collection_constructor_category_filter.hide()
		recycler_view_fragment_collection_constructor_list.adapter = typesAdapter
		text_view_fragment_collection_constructor_category_back.visibility = View.INVISIBLE
		text_view_fragment_collection_constructor_category_back.isClickable = false

		isItems = false
		isStyle = false

		presenter.getTypes(token = getTokenFromSharedPref())
	}

	private fun onCategoryNextClick() {
		if (isStyle) {
			processPostImages()
		} else if (listOfItems.isNotEmpty()) {
			text_view_fragment_collection_constructor_category_back.show()
			text_view_fragment_collection_constructor_category_back.isClickable = true

			isStyle = true
			isItems = false

			recycler_view_fragment_collection_constructor_list.hide()

			presenter.getStyles(token = getTokenFromSharedPref())
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
					presenter.getClothesByType(
						token = getTokenFromSharedPref(),
						filterModel = currentFilter
					)
				}
			}
		}
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

			presenter.getTypes(token = getTokenFromSharedPref())
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

	private fun showFilterResults(item: Any?) {
		currentFilter = item as FilterModel

		presenter.getClothesByType(
			token = getTokenFromSharedPref(),
			filterModel = currentFilter
		)
	}

	private fun navigateToCameraFragment(mode: Int) {
		val bundle = Bundle()
		bundle.putInt("mode", mode)

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
				title = getString(R.string.collection_constructor_add_category),
				isExternal = true,
				externalType = 1,
				isChoosen = false,
				externalImageId = R.drawable.ic_add
			)
		)
		preparedResults.add(
			ClothesTypeModel(
				id = list.size + 1,
				title = getString(R.string.collection_constructor_add_by_barcode),
				isExternal = true,
				externalType = 2,
				isChoosen = false,
				externalImageId = R.drawable.ic_baseline_qr_code_2_24
			)
		)
		preparedResults.add(
			ClothesTypeModel(
				id = list.size + 1,
				title = getString(R.string.collection_constructor_add_by_photo),
				isExternal = true,
				externalType = 3,
				isChoosen = false,
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
			val constructorImagesContainer = frame_layout_fragment_collection_constructor_images_container
			val clothesList = ArrayList<Int>()
			val clothesCollectionList = getClothesCollectionList(clothesList = clothesList)

			val outfitCreateModel = OutfitCreateModel(
				style = currentStyle!!.id,
				clothes = clothesList,
				itemLocation = clothesCollectionList,
				author = currentActivity.getSharedPrefByKey(SharedConstants.USER_ID_KEY) ?: 0,
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

	private fun getClothesCollectionList(clothesList: ArrayList<Int>): ArrayList<ItemLocationModel> {
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

			clothesList.add(listOfItems[index].id)
		}

		return clothesCollectionList
	}

	private fun showCreateCollectionDialog(outfitCreateModel: OutfitCreateModel) {
		val bundle = Bundle()

		bundle.putParcelable(CreateCollectionAcceptFragment.OUTFIT_MODEL_KEY, outfitCreateModel)
		bundle.putParcelable(CreateCollectionAcceptFragment.PHOTO_BITMAP_KEY, currentCollectionBitmap)
		bundle.putInt(CreateCollectionAcceptFragment.MODE_KEY, CreateCollectionAcceptFragment.OUTFIT_MODE)

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

	private fun getTokenFromSharedPref(): String {
		return currentActivity.getSharedPrefByKey<String>(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
	}

	private fun getGender(): String = when (currentType) {
		0 -> "M"
		else -> "F"
	}
}