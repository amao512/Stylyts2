package kz.eztech.stylyts.presentation.fragments.main.constructor

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.fragment_collection_constructor.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionConstructorShopCategoryAdapter
import kz.eztech.stylyts.presentation.adapters.CollectionConstructorShopItemAdapter
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.constructor.CollectionConstructorContract
import kz.eztech.stylyts.presentation.dialogs.ConstructorFilterDialog
import kz.eztech.stylyts.presentation.dialogs.CreateCollectionAcceptDialog
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewDoubleClickListener
import kz.eztech.stylyts.presentation.presenters.main.constructor.CollectionConstructorPresenter
import kz.eztech.stylyts.presentation.utils.FileUtils.createPngFileFromBitmap
import kz.eztech.stylyts.presentation.utils.RelativeImageMeasurements
import kz.eztech.stylyts.presentation.utils.RelativeMeasureUtil
import kz.eztech.stylyts.presentation.utils.RelativeMeasureUtil.reMeasureEntity
import kz.eztech.stylyts.presentation.utils.ViewUtils.createBitmapScreenshot
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

	private lateinit var adapter: CollectionConstructorShopCategoryAdapter
	private lateinit var itemAdapter: CollectionConstructorShopItemAdapter
	private var isItems = false
	private var isStyle = false
	private var currentType = 0
	private var currentSaveMode = 1

	private val listOfItems = ArrayList<ClothesMainModel>()
	private val listOfEntities = ArrayList<ImageEntity>()
	private val listOfIdsChosen = ArrayList<Int>()
	private val listOfGenderCategory = ArrayList<GenderCategory>()
	private var currentId : Int = -1
	private var currentStyle:Style? = null
	private var currentCollectionBitmap:Bitmap? = null
	private var currentMainId: Int = -1
	private val filterMap = HashMap<String, Any>()

	private lateinit var filterDialog: ConstructorFilterDialog
	@Inject
	lateinit var presenter:CollectionConstructorPresenter
	override fun getLayoutId(): Int {
		return R.layout.fragment_collection_constructor
	}
	
	override fun getContractView(): BaseView {
		return this
	}
	
	override fun customizeActionBar() {

	}
	
	override fun initializeDependency() {
		(currentActivity.application as StylytsApp).applicationComponent.inject(this)
	}
	
	override fun initializePresenter() {
		presenter.attach(this)
	}
	
	override fun initializeArguments() {
		arguments?.let {
			if(it.containsKey("items")){
				val handler = Handler(Looper.getMainLooper())
				handler.postDelayed(Runnable {
					it.getParcelableArrayList<ClothesMainModel>("items")?.let { it1 ->
						it1.forEach {
							processInputImageToPlace(it)
						}
					}
				}, 500)
				
			}
			
			if(it.containsKey("mainId")){
				currentMainId = it.getInt("mainId")
			}

			if(it.containsKey("currentType")){
				currentType = it.getInt("currentType")
			}
		}
	}
	
	override fun initializeViewsData() {
		linearLayout2.layoutTransition.setAnimateParentHierarchy(false)
	}
	
	override fun initializeViews() {
		filterDialog = ConstructorFilterDialog()
		checkWritePermission()
		adapter = CollectionConstructorShopCategoryAdapter()
		itemAdapter = CollectionConstructorShopItemAdapter()
		recycler_view_fragment_collection_constructor_list.layoutManager = LinearLayoutManager(
				context,
				LinearLayoutManager.HORIZONTAL,
				false
		)
		recycler_view_fragment_collection_constructor_list.adapter = adapter
		adapter.itemClickListener = this
		itemAdapter.itemClickListener = this
		itemAdapter.itemDoubleClickListener = this
		frame_layout_fragment_collection_constructor_images_container.attachView(this)
		processDraggedItems()
	}
	
	override fun initializeListeners() {
		text_view_fragment_collection_constructor_total_price.setOnClickListener(this)
		text_view_fragment_collection_constructor_category_back.setOnClickListener(this)
		text_view_fragment_collection_constructor_category_next.setOnClickListener(this)
		text_view_fragment_collection_constructor_category_filter.setOnClickListener(this)
		filterDialog.setChoiceListener(this)
	}
	

	override fun processPostInitialization() {
		presenter.getCategory()
	}
	
	override fun disposeRequests() {
		presenter.disposeRequests()
	}
	
	override fun displayMessage(msg: String) {
		displayToast(msg)
	}
	
	override fun isFragmentVisible(): Boolean {
		return isVisible
	}
	
	override fun displayProgress() {
		progress_bar_fragment_collection_constructor.visibility = View.VISIBLE
	}
	
	override fun hideProgress() {
		progress_bar_fragment_collection_constructor.visibility = View.GONE
	}

	override fun onClick(v: View?) {
		when(v?.id){
			R.id.text_view_fragment_collection_constructor_category_back -> {
				if (isStyle) {
					recycler_view_fragment_collection_constructor_list.adapter = adapter
					recycler_view_fragment_collection_constructor_list.visibility = View.VISIBLE
					list_view_fragment_collection_constructor_list_style.visibility = View.GONE
					text_view_fragment_collection_constructor_category_back.visibility = View.INVISIBLE
					text_view_fragment_collection_constructor_category_back.isClickable  = false
					isStyle = false
					isItems = false
					checkIsListEmpty()
					presenter.getCategory()
				} else if (isItems) {
					checkIsListEmpty()
					text_view_fragment_collection_constructor_category_filter.visibility = View.GONE
					recycler_view_fragment_collection_constructor_list.adapter = adapter
					text_view_fragment_collection_constructor_category_back.visibility = View.INVISIBLE
					text_view_fragment_collection_constructor_category_back.isClickable  = false
					isItems = false
					isStyle = false
					presenter.getCategory()
				} else {
					presenter.getCategory()
				}
			}
			R.id.text_view_fragment_collection_constructor_category_next -> {
				if (isStyle) {
					processPostImages()
				} else if (listOfItems.isNotEmpty()) {
					text_view_fragment_collection_constructor_category_back.visibility = View.VISIBLE
					text_view_fragment_collection_constructor_category_back.isClickable  = true
					isStyle = true
					isItems = false
					recycler_view_fragment_collection_constructor_list.visibility = View.GONE
					presenter.getStyles(
							currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)
									?: ""
					)
				}
			}
			R.id.text_view_fragment_collection_constructor_category_filter -> {
				filterDialog.show(childFragmentManager, "FilterDialog")
			}
		}
	}
	private fun checkIsListEmpty(){
		if(listOfItems.isNotEmpty()){
			text_view_fragment_collection_constructor_category_next.visibility = View.VISIBLE
		}
	}
	override fun onViewClicked(view: View, position: Int, item: Any?) {
		when(view?.id){
			R.id.image_view_item_collection_constructor_category_item_image_holder -> {
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
								when(currentType){
									0 -> {
										filterMap["gender"] = "M"
									}
									1 -> {
										filterMap["gender"] = "F"
									}
								}

								presenter.getShopCategoryTypeDetail(
										currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)
												?: "",
									filterMap)
							}
						}
					}
				}
			}
		}
	}
	
	override fun onViewDoubleClicked(view: View, position: Int, item: Any?, isDouble: Boolean?) {
		when(view?.id){
			R.id.image_view_item_collection_constructor_category_item_image_holder -> {
				if (isItems) {
					checkIsListEmpty()
					text_view_fragment_collection_constructor_category_filter.visibility = View.GONE
					recycler_view_fragment_collection_constructor_list.adapter = adapter
					text_view_fragment_collection_constructor_category_back.visibility = View.GONE
					text_view_fragment_collection_constructor_category_back.isClickable = false
					isItems = false
					isStyle = false
					presenter.getCategory()
				}
			}
		}
	}
	
	override fun processFilteredItems(model: FilteredItemsModel) {
		text_view_fragment_collection_constructor_category_next.visibility = View.GONE
		text_view_fragment_collection_constructor_category_filter.visibility = View.VISIBLE
		text_view_fragment_collection_constructor_category_back.visibility = View.VISIBLE
		text_view_fragment_collection_constructor_category_back.isClickable = true
		model.results?.let {
			recycler_view_fragment_collection_constructor_list.adapter = itemAdapter
			itemAdapter.updateList(it)
			isItems = true
		}
	}
	
	
	private fun processInputImageToPlace(item: Any?) {
		item as ClothesMainModel
		var photoUrl = ""
		item.cover_photo?.let {
			photoUrl = if(it.contains("http", true)) it else "http://178.170.221.31:8000${it}"
		}
		var currentSameObject:ImageEntity? = null
		if(listOfItems.isNotEmpty() && listOfEntities.isNotEmpty() ){
			listOfEntities.forEach {
				if(item.clothes_type?.body_part == it.item.clothes_type?.body_part){
					currentSameObject = it
				}
			}
		}
		
		Glide.with(currentActivity.applicationContext)
			.asFile()
			.load(photoUrl)
			.into(object : CustomTarget<File>() {
				override fun onResourceReady(file: File, transition: Transition<in File>?) {
					frame_layout_fragment_collection_constructor_images_container_placeholder.visibility = View.GONE
					val options = BitmapFactory.Options()
					options.inMutable = true
					var resource = BitmapFactory.decodeFile(file.path, options)
					frame_layout_fragment_collection_constructor_images_container.post(Runnable {
						currentSameObject?.let {
							frame_layout_fragment_collection_constructor_images_container.deleteEntity(it)
						}
						val layer = Layer()
						var measurements: RelativeImageMeasurements? = null
						item.clothe_location?.let {
							measurements = reMeasureEntity(RelativeImageMeasurements(it.point_x!!.toFloat(), it.point_y!!.toFloat(), it.width!!.toFloat(), it.height!!.toFloat(), it.degree!!.toFloat()), frame_layout_fragment_collection_constructor_images_container)
						}
						
						val entity = ImageEntity(layer, resource, item, frame_layout_fragment_collection_constructor_images_container.getWidth(), frame_layout_fragment_collection_constructor_images_container.getHeight())
						frame_layout_fragment_collection_constructor_images_container.addEntityAndPosition(entity)
						currentSameObject?.let {
							entity.layer.scale = it.layer.scale
							entity.layer.rotationInDegrees = it.layer.rotationInDegrees
							entity.moveToPoint(PointF(it.absoluteCenterX(),it.absoluteCenterY()))
						}
						
						measurements?.let {
							entity.moveToPoint(PointF(it.point_x, it.point_y))
							entity.layer.postScaleRaw(it.width/resource.width)
							entity.layer.postRotate(it.degree)
						}
						listOfItems.add(item)
						listOfEntities.add(entity)
						listOfIdsChosen.add(currentId)
						processDraggedItems()
					})
				}
				
				override fun onLoadCleared(placeholder: Drawable?) {
				}
			})
	}
	
	private fun checkWritePermission() {
		if (ContextCompat.checkSelfPermission(
						currentActivity,
						Manifest.permission.WRITE_EXTERNAL_STORAGE
				)
				!= PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(
							currentActivity,
							Manifest.permission.WRITE_EXTERNAL_STORAGE
					)) {
				
			} else {
				ActivityCompat.requestPermissions(
						currentActivity,
						arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
						2
				);
			}
		}
	}
	
	override fun onChoice(v: View?, item: Any?) {
		if(v?.id == R.id.button_dialog_filter_constructor_submit){
			val map = item as HashMap<String, Any>
			val clothes_type = filterMap["clothes_type"]
			filterMap.clear()
			filterMap["clothes_type"] = clothes_type as String
			filterMap.putAll(map)
			
			
			presenter.getShopCategoryTypeDetail(
					currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY) ?: "",
					filterMap)
		}else{
			when(item){
				is Map<*, *> -> {
					val currentModel = item["model"] as CollectionPostCreateModel
					currentSaveMode = item["mode"] as Int
					currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)?.let {
						try {
							currentCollectionBitmap?.let { bitmap ->
								val file = createPngFileFromBitmap(requireContext(), bitmap)
								file?.let { currentFile ->
									if(currentMainId != -1){
										presenter.updateCollection(
												it, currentMainId,currentModel, file
										)
									}else{
										presenter.saveCollection(
												it, currentModel, file
										)
									}
									
								} ?: run {
									displayMessage("Не удалось загрузить данные")
									hideProgress()
								}
								
							} ?: run {
								displayMessage("Не удалось загрузить данные")
								hideProgress()
							}
						} catch (e: Exception) {
							hideProgress()
							displayMessage("Не удалось загрузить данные")
						}
						
					}
					
				}
				is Int -> {
					when(item){
						1 -> {
							val bundle = Bundle()
							bundle.putInt("mode",1)
							findNavController().navigate(R.id.action_createCollectionFragment_to_cameraFragment,bundle)
						}
						2 -> {
							val bundle = Bundle()
							bundle.putInt("mode",2)
							findNavController().navigate(R.id.action_createCollectionFragment_to_cameraFragment,bundle)
						}
					}
				}
			}
		}
		
	}
	
	override fun processShopCategories(shopCategoryModel: ShopCategoryModel) {
		when(currentType){
			0 -> {
				shopCategoryModel.menCategory?.let {

					it.add(GenderCategory(
						title = "Добавить по штрихкоду",
						isExternal = true,
						externalType = 1,
						isChoosen = false,
						externalImageId = R.drawable.ic_baseline_qr_code_2_24
					))
					it.add(GenderCategory(
						title = "Добавить по фото",
						isExternal = true,
						externalType = 2,
						isChoosen = false,
						externalImageId = R.drawable.ic_camera
					))
					if(listOfIdsChosen.isNotEmpty()){
						listOfIdsChosen.forEach { type ->
							it.find { it.id == type }?.isChoosen = true
						}
					}
					listOfGenderCategory.clear()
					listOfGenderCategory.addAll(it)
					adapter.updateList(listOfGenderCategory)
				}
			}
			1 -> {
				shopCategoryModel.femaleCategory?.let {
					it.add(GenderCategory(
						title = "Добавить по штрихкоду",
						isExternal = true,
						externalType = 1,
						isChoosen = false,
						externalImageId = R.drawable.ic_baseline_qr_code_2_24
					))
					it.add(GenderCategory(
						title = "Добавить по фото",
						isExternal = true,
						externalType = 2,
						isChoosen = false,
						externalImageId = R.drawable.ic_camera
					))
					if(listOfIdsChosen.isNotEmpty()){
						listOfIdsChosen.forEach { type ->
							it.find { it.id == type }?.isChoosen = true
						}
					}
					listOfGenderCategory.clear()
					listOfGenderCategory.addAll(it)
					adapter.updateList(listOfGenderCategory)
				}
			}
		}
	}
	

	private fun processDraggedItems(){
		val price = NumberFormat.getInstance().format(listOfItems.sumBy { it.cost ?: 0 })
		text_view_fragment_collection_constructor_total_price.text = "$price тг."
		if(!isItems){
			checkIsListEmpty()
			checkIsChosen()
		}
	}

	private fun checkIsChosen(){
		if(listOfIdsChosen.isNotEmpty()){
			listOfIdsChosen.forEach { type ->
				listOfGenderCategory.find { it.id == type }?.isChoosen = true
			}
		}
	}

	private fun processPostImages(){
		if(listOfItems.isNotEmpty() && listOfEntities.isNotEmpty() && currentStyle!=null){
			val collectionPostCreateModel = CollectionPostCreateModel()

			collectionPostCreateModel.style = currentStyle!!.id
			val clth = ArrayList<Int>()



			val loccloth = ArrayList<ClothesCollection>()

			listOfEntities.forEachIndexed { index, imageView ->
				val result = RelativeMeasureUtil.measureEntity(
						imageView,
						frame_layout_fragment_collection_constructor_images_container
				)
				loccloth.add(
						ClothesCollection(
								clothes_id = listOfItems[index].id,
								point_x = result.point_x,
								point_y = result.point_y,
								width = result.width,
								height = result.height,
								degree = result.degree
						)
				)
				clth.add(listOfItems[index].id ?: 0)
			}
			collectionPostCreateModel.clothes = clth

			collectionPostCreateModel.clothes_location = loccloth
			collectionPostCreateModel.author = currentActivity.getSharedPrefByKey<Int>(
					SharedConstants.USER_ID_KEY
			)
			collectionPostCreateModel.total_price = listOfItems.sumBy { it.cost?:0 }.toFloat()
			frame_layout_fragment_collection_constructor_images_container.unselectEntity()
			currentCollectionBitmap = createBitmapScreenshot(frame_layout_fragment_collection_constructor_images_container)
			val createCollecationDialog = CreateCollectionAcceptDialog()
			val bundle = Bundle()
			bundle.putParcelable("collectionModel", collectionPostCreateModel)
			bundle.putParcelable("photoBitmap", currentCollectionBitmap)
			createCollecationDialog.arguments = bundle
			createCollecationDialog.setChoiceListener(this@CollectionConstructorFragment)
			createCollecationDialog.show(childFragmentManager, "PhotoChossoserTag")
			
		}else{
			displayMessage("Не все данные заполнены")
		}


	}

	override fun processStyles(list: List<Style>) {
		val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
				currentActivity, R.layout.item_style, list.map { it.title }
		)
		recycler_view_fragment_collection_constructor_list.visibility = View.GONE
		list_view_fragment_collection_constructor_list_style.adapter = adapter
		list_view_fragment_collection_constructor_list_style.visibility = View.VISIBLE
		list_view_fragment_collection_constructor_list_style.setOnItemClickListener { parent, view, position, id ->
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
		result?.let{ model ->
			when(currentSaveMode){
				1 -> {
					displayMessage("Успешно добавлено")
					findNavController().navigateUp()
				}
				2 -> {
					model.id?.let{
						presenter.saveCollectionToMe(currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)
								?: "",
								it)
					}
					
				}
				else -> {
					displayMessage("Успешно добавлено")
					findNavController().navigateUp()
				}
			}
		} ?: run {
			displayMessage("Успешно добавлено")
			findNavController().navigateUp()
		}
		
	}
}