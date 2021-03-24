package kz.eztech.stylyts.create_outfit.presentation.fragments

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.fragment_collection_constructor.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.base.DialogChooserListener
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewDoubleClickListener
import kz.eztech.stylyts.create_outfit.domain.models.FilteredItemsModel
import kz.eztech.stylyts.create_outfit.domain.models.GenderCategory
import kz.eztech.stylyts.create_outfit.domain.models.ShopCategoryModel
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.domain.models.MainResult
import kz.eztech.stylyts.common.data.models.SharedConstants
import kz.eztech.stylyts.common.domain.models.*
import kz.eztech.stylyts.create_outfit.presentation.adapters.CollectionConstructorShopCategoryAdapter
import kz.eztech.stylyts.create_outfit.presentation.adapters.CollectionConstructorShopItemAdapter
import kz.eztech.stylyts.create_outfit.presentation.contracts.CollectionConstructorContract
import kz.eztech.stylyts.create_outfit.presentation.dialogs.ConstructorFilterDialog
import kz.eztech.stylyts.create_outfit.presentation.dialogs.CreateCollectionAcceptDialog
import kz.eztech.stylyts.create_outfit.presentation.presenters.CollectionConstructorPresenter
import kz.eztech.stylyts.common.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.common.presentation.utils.FileUtils.createPngFileFromBitmap
import kz.eztech.stylyts.common.presentation.utils.RelativeImageMeasurements
import kz.eztech.stylyts.common.presentation.utils.RelativeMeasureUtil
import kz.eztech.stylyts.common.presentation.utils.RelativeMeasureUtil.reMeasureEntity
import kz.eztech.stylyts.common.presentation.utils.ViewUtils.createBitmapScreenshot
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show
import kz.eztech.stylyts.common.presentation.utils.stick.ImageEntity
import kz.eztech.stylyts.common.presentation.utils.stick.Layer
import kz.eztech.stylyts.common.presentation.utils.stick.MotionEntity
import java.io.File
import java.text.NumberFormat
import javax.inject.Inject

class CollectionConstructorFragment : BaseFragment<MainActivity>(),
    CollectionConstructorContract.View,
    View.OnClickListener,
    DialogChooserListener,
    UniversalViewClickListener, UniversalViewDoubleClickListener {

	@Inject lateinit var presenter: CollectionConstructorPresenter

    private lateinit var adapter: CollectionConstructorShopCategoryAdapter
    private lateinit var itemAdapter: CollectionConstructorShopItemAdapter
	private lateinit var filterDialog: ConstructorFilterDialog

    private val listOfItems = ArrayList<ClothesMainModel>()
    private val listOfEntities = ArrayList<ImageEntity>()
    private val listOfIdsChosen = ArrayList<Int>()
    private val listOfGenderCategory = ArrayList<GenderCategory>()
    private val filterMap = HashMap<String, Any>()

	private var isItems = false
	private var isStyle = false
	private var currentType = 0
	private var currentSaveMode = 1
	private var currentId: Int = -1
	private var currentStyle: Style? = null
	private var currentCollectionBitmap: Bitmap? = null
	private var currentMainId: Int = -1

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
            if (it.containsKey("items")) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed(Runnable {
					it.getParcelableArrayList<ClothesMainModel>("items")?.let { it1 ->
						it1.forEach { clothesMainModel ->
							processInputImageToPlace(clothesMainModel)
						}
					}
				}, 500)
            }

            if (it.containsKey("mainId")) {
                currentMainId = it.getInt("mainId")
            }

            if (it.containsKey("currentType")) {
                currentType = it.getInt("currentType")
            }
        }
    }

    override fun initializeViewsData() {
        linearLayout2.layoutTransition.setAnimateParentHierarchy(false)
    }

    override fun initializeViews() {
		checkWritePermission()

        filterDialog = ConstructorFilterDialog()
        adapter = CollectionConstructorShopCategoryAdapter()
        itemAdapter = CollectionConstructorShopItemAdapter()

		adapter.itemClickListener = this
		itemAdapter.itemClickListener = this
		itemAdapter.itemDoubleClickListener = this

        recycler_view_fragment_collection_constructor_list.adapter = adapter
        frame_layout_fragment_collection_constructor_images_container.attachView(motionViewContract = this)

        processDraggedItems()
    }

    override fun initializeListeners() {
        text_view_fragment_collection_constructor_total_price.setOnClickListener(this)
        text_view_fragment_collection_constructor_category_back.setOnClickListener(this)
        text_view_fragment_collection_constructor_category_next.setOnClickListener(this)
        text_view_fragment_collection_constructor_category_filter.setOnClickListener(this)
        filterDialog.setChoiceListener(this)
    }

    override fun processPostInitialization() = presenter.getCategory()

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
        }
    }

    override fun processFilteredItems(model: FilteredItemsModel) {
        text_view_fragment_collection_constructor_category_next.hide()
        text_view_fragment_collection_constructor_category_filter.show()

        text_view_fragment_collection_constructor_category_back.show()
        text_view_fragment_collection_constructor_category_back.isClickable = true

        model.results?.let {
            recycler_view_fragment_collection_constructor_list.adapter = itemAdapter

            itemAdapter.updateList(it)
            isItems = true
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        if (v?.id == R.id.button_dialog_filter_constructor_submit) {
			showFilterResults(item)
        } else {
            when (item) {
				is Map<*, *> -> {
					val currentModel = item["model"] as CollectionPostCreateModel
					currentSaveMode = item["mode"] as Int

					getTokenFromSharedPref().let {
						try {
							currentCollectionBitmap?.let { bitmap ->
								val file = createPngFileFromBitmap(requireContext(), bitmap)

								file?.let { _ ->
									if (currentMainId != -1) {
										presenter.updateCollection(
											it, currentMainId, currentModel, file
										)
									} else {
										presenter.saveCollection(
											it, currentModel, file
										)
									}
								} ?: run {
									errorLoadData()
								}

							} ?: run {
								errorLoadData()
							}
						} catch (e: Exception) {
							errorLoadData()
						}
					}
				}
				is Int -> {
					when (item) {
						1 -> {/* add category */}
						2 -> navigateToCameraFragment(mode = CameraFragment.BARCODE_MODE)
						3 -> navigateToCameraFragment(mode = CameraFragment.PHOTO_MODE)
					}
				}
            }
        }
    }

	override fun processShopCategories(shopCategoryModel: ShopCategoryModel) {
		when (currentType) {
			0 -> shopCategoryModel.menCategory?.let {
				addCameraCategories(genderCategoryList = it)
				processCategory(genderCategoryList = it)
			}
			1 -> shopCategoryModel.femaleCategory?.let {
				addCameraCategories(genderCategoryList = it)
				processCategory(genderCategoryList = it)
			}
		}
	}

	override fun processStyles(list: List<Style>) {
		val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
			currentActivity, R.layout.item_style, list.map { it.title }
		)
		recycler_view_fragment_collection_constructor_list.hide()

		list_view_fragment_collection_constructor_list_style.adapter = adapter
		list_view_fragment_collection_constructor_list_style.show()
		list_view_fragment_collection_constructor_list_style.setOnItemClickListener { _, _, position, _ ->
			currentStyle = list[position]
			processPostImages()
		}
	}

	override fun deleteSelectedView(motionEntity: MotionEntity) {
		val res = listOfEntities.remove(motionEntity)

		val res2 = listOfItems.remove(motionEntity.item)
		val res3 = listOfIdsChosen.remove(currentId)

		Log.wtf("deletedSelectedEntity", "res1:$res res2:$res2 res3:$res3")
		processDraggedItems()
	}

	override fun processSuccess(result: MainResult?) {
		result?.let {
			when (currentSaveMode) {
				1 -> successAddedNavigateUp()
				2 -> saveCollection(model = it)
				else -> successAddedNavigateUp()
			}
		} ?: run {
			successAddedNavigateUp()
		}
	}

	private fun processInputImageToPlace(item: Any?) {
		item as ClothesMainModel

		var photoUrl = EMPTY_STRING
		var currentSameObject: ImageEntity? = null

		item.cover_photo?.let { coverPhoto ->
			photoUrl = if (coverPhoto.contains("http", true)) {
				coverPhoto
			} else {
				"http://178.170.221.31:8000${coverPhoto}"
			}
		}

		if (listOfItems.isNotEmpty() && listOfEntities.isNotEmpty()) {
			listOfEntities.forEach {
				if (item.clothes_type?.body_part == it.item.clothes_type?.body_part) {
					currentSameObject = it
				}
			}
		}

		loadWithGlide(
			photoUrl = photoUrl,
			currentSameObject = currentSameObject,
			clothesMainModel = item
		)
	}

	private fun checkWritePermission() {
		val writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE
		val readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE

		if (ContextCompat.checkSelfPermission(currentActivity, writeExternalStorage) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, writeExternalStorage)) {

			} else {
				ActivityCompat.requestPermissions(
					currentActivity,
					arrayOf(writeExternalStorage, readExternalStorage),
					2
				)
			}
		}
	}

	private fun processDraggedItems() {
		val price = NumberFormat.getInstance().format(listOfItems.sumBy { it.cost ?: 0 })
		text_view_fragment_collection_constructor_total_price.text = getString(R.string.price_tenge_text_format, price)

		if (!isItems) {
			checkIsListEmpty()
			checkIsChosen()
		}
	}

	private fun onCategoryBackClick() {
		if (isStyle) {
			recycler_view_fragment_collection_constructor_list.adapter = adapter
			recycler_view_fragment_collection_constructor_list.show()

			list_view_fragment_collection_constructor_list_style.hide()
			text_view_fragment_collection_constructor_category_back.visibility = View.INVISIBLE
			text_view_fragment_collection_constructor_category_back.isClickable = false

			isStyle = false
			isItems = false

			checkIsListEmpty()
			presenter.getCategory()
		} else if (isItems) {
			checkIsListEmpty()

			text_view_fragment_collection_constructor_category_filter.hide()
			recycler_view_fragment_collection_constructor_list.adapter = adapter
			text_view_fragment_collection_constructor_category_back.visibility = View.INVISIBLE
			text_view_fragment_collection_constructor_category_back.isClickable = false

			isItems = false
			isStyle = false

			presenter.getCategory()
		} else {
			presenter.getCategory()
		}
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
			(item as GenderCategory).run {
				if (isExternal) {
					onChoice(view, externalType)
				} else {
					currentId = item.id ?: 0

					item.clothes_types?.let { clothes ->
						filterMap["clothes_type"] = clothes.map { it.id }.joinToString()

						when (currentType) {
							0 -> filterMap["gender"] = "M"
							1 -> filterMap["gender"] = "F"
						}

						presenter.getShopCategoryTypeDetail(
							token = getTokenFromSharedPref(),
							map = filterMap
						)
					}
				}
			}
		}
	}

	private fun onCategoryItemImageDoubleClick() {
		if (isItems) {
			checkIsListEmpty()

			text_view_fragment_collection_constructor_category_filter.hide()
			recycler_view_fragment_collection_constructor_list.adapter = adapter

			text_view_fragment_collection_constructor_category_back.hide()
			text_view_fragment_collection_constructor_category_back.isClickable = false

			isItems = false
			isStyle = false

			presenter.getCategory()
		}
	}

	private fun showFilterResults(item: Any?) {
		val map = item as HashMap<String, Any>
		val clothes_type = filterMap["clothes_type"]

		filterMap.clear()
		filterMap["clothes_type"] = clothes_type as String
		filterMap.putAll(map)

		presenter.getShopCategoryTypeDetail(
			token = getTokenFromSharedPref(),
			map = filterMap
		)
	}

	private fun errorLoadData() {
		hideProgress()
		displayMessage(msg = getString(R.string.collection_constructor_error_load_data))
	}

	private fun navigateToCameraFragment(mode: Int) {
		val bundle = Bundle()
		bundle.putInt("mode", mode)

		findNavController().navigate(
			R.id.action_createCollectionFragment_to_cameraFragment,
			bundle
		)
	}

	private fun addCameraCategories(genderCategoryList: ArrayList<GenderCategory>) {
		genderCategoryList.add(
			GenderCategory(
				title = getString(R.string.collection_constructor_add_category),
				isExternal = true,
				externalType = 1,
				isChoosen = false,
				externalImageId = R.drawable.ic_add
			)
		)
		genderCategoryList.add(
			GenderCategory(
				title = getString(R.string.collection_constructor_add_by_barcode),
				isExternal = true,
				externalType = 2,
				isChoosen = false,
				externalImageId = R.drawable.ic_baseline_qr_code_2_24
			)
		)
		genderCategoryList.add(
			GenderCategory(
				title = getString(R.string.collection_constructor_add_by_photo),
				isExternal = true,
				externalType = 3,
				isChoosen = false,
				externalImageId = R.drawable.ic_camera
			)
		)
	}

	private fun processCategory(genderCategoryList: ArrayList<GenderCategory>) {
		if (listOfIdsChosen.isNotEmpty()) {
			listOfIdsChosen.forEach { type ->
				genderCategoryList.find { it.id == type }?.isChoosen = true
			}
		}

		listOfGenderCategory.clear()
		listOfGenderCategory.addAll(genderCategoryList)

		adapter.updateList(listOfGenderCategory)
	}

	private fun processPostImages() {
		if (listOfItems.isNotEmpty() && listOfEntities.isNotEmpty() && currentStyle != null) {
			val constructorImagesContainer = frame_layout_fragment_collection_constructor_images_container
			val clothesList = ArrayList<Int>()
			val clothesCollectionList = getClothesCollectionList(clothesList = clothesList)

			val collectionPostCreateModel = CollectionPostCreateModel(
				style = currentStyle!!.id,
				clothes = clothesList,
				clothes_location = clothesCollectionList,
				author = currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY),
				total_price = listOfItems.sumBy { it.cost ?: 0 }.toFloat()
			)

			constructorImagesContainer.unselectEntity()
			currentCollectionBitmap = createBitmapScreenshot(constructorImagesContainer)

			showCreateCollectionDialog(collectionPostCreateModel)
		} else {
			displayMessage(msg = getString(R.string.collection_constructor_not_all_data_filled))
		}
	}

	private fun successAddedNavigateUp() {
		displayMessage(msg = getString(R.string.collection_constructor_success_added))
		findNavController().navigateUp()
	}

	private fun saveCollection(model: MainResult) {
		model.id?.let {
			presenter.saveCollectionToMe(
				token = getTokenFromSharedPref(),
				id = it
			)
		}
	}

	private fun loadWithGlide(
        photoUrl: String,
        currentSameObject: ImageEntity?,
        clothesMainModel: ClothesMainModel
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
							clothesMainModel = clothesMainModel,
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
				listOfGenderCategory.find { it.id == type }?.isChoosen = true
			}
		}
	}

	private fun getClothesCollectionList(clothesList: ArrayList<Int>): ArrayList<ClothesCollection> {
		val clothesCollectionList = ArrayList<ClothesCollection>()

		listOfEntities.forEachIndexed { index, imageView ->
			val result = RelativeMeasureUtil.measureEntity(
				imageView,
				frame_layout_fragment_collection_constructor_images_container
			)

			clothesCollectionList.add(
				ClothesCollection(
					clothes_id = listOfItems[index].id,
					point_x = result.point_x,
					point_y = result.point_y,
					width = result.width,
					height = result.height,
					degree = result.degree
				)
			)

			clothesList.add(listOfItems[index].id ?: 0)
		}

		return clothesCollectionList
	}

	private fun showCreateCollectionDialog(collectionPostCreateModel: CollectionPostCreateModel) {
		val createCollectionDialog = CreateCollectionAcceptDialog()

		createCollectionDialog.arguments = getCollectionPostBundle(collectionPostCreateModel)
		createCollectionDialog.setChoiceListener(this@CollectionConstructorFragment)
		createCollectionDialog.show(childFragmentManager, "PhotoChossoserTag")
	}

	private fun getCollectionPostBundle(collectionPostCreateModel: CollectionPostCreateModel): Bundle {
		val bundle = Bundle()

		bundle.putParcelable("collectionModel", collectionPostCreateModel)
		bundle.putParcelable("photoBitmap", currentCollectionBitmap)

		return bundle
	}

	private fun runImagesContainer(
        currentSameObject: ImageEntity?,
        clothesMainModel: ClothesMainModel,
        resource: Bitmap
	) {
		currentSameObject?.let {
			frame_layout_fragment_collection_constructor_images_container.deleteEntity(entity = it)
		}

		val measurements = getRelativeImageMeasurements(clothesMainModel)
		val entity = getImageEntity(resource, clothesMainModel)

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

		listOfItems.add(clothesMainModel)
		listOfEntities.add(entity)
		listOfIdsChosen.add(currentId)
	}

	private fun getRelativeImageMeasurements(
		clothesMainModel: ClothesMainModel
	): RelativeImageMeasurements? {
		var measurements: RelativeImageMeasurements? = null

		clothesMainModel.clothe_location?.let {
			measurements = reMeasureEntity(
				RelativeImageMeasurements(
					it.point_x!!.toFloat(),
					it.point_y!!.toFloat(),
					it.width!!.toFloat(),
					it.height!!.toFloat(),
					it.degree!!.toFloat()
				),
				frame_layout_fragment_collection_constructor_images_container
			)
		}

		return measurements
	}

	private fun getImageEntity(
		resource: Bitmap,
		clothesMainModel: ClothesMainModel
	): ImageEntity = ImageEntity(
		layer = Layer(),
		bitmap = resource,
		item = clothesMainModel,
		canvasWidth = frame_layout_fragment_collection_constructor_images_container.width,
		canvasHeight = frame_layout_fragment_collection_constructor_images_container.height
	)

	private fun getTokenFromSharedPref(): String {
		return currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY) ?: EMPTY_STRING
	}
}