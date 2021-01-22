package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.loader.app.LoaderManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_constructor.*
import kotlinx.android.synthetic.main.fragment_collection_constructor.include_toolbar_profile
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_collection_constructor_category_item.view.*
import kotlinx.android.synthetic.main.item_constuctor_image_holder.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionConstructorShopCategoryAdapter
import kz.eztech.stylyts.presentation.adapters.CollectionConstructorShopItemAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.constructor.CollectionConstructorContract
import kz.eztech.stylyts.presentation.dialogs.CreateCollectionAcceptDialog
import kz.eztech.stylyts.presentation.dialogs.PhotoChooserDialog
import kz.eztech.stylyts.presentation.fragments.main.constructor.PhotoChooserFragment.Companion.BAR_CODE
import kz.eztech.stylyts.presentation.fragments.main.constructor.PhotoChooserFragment.Companion.PHOTO_LIBRARY
import kz.eztech.stylyts.presentation.fragments.main.constructor.PhotoChooserFragment.Companion.PHOTO_TYPE
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.constructor.CollectionConstructorPresenter
import kz.eztech.stylyts.presentation.utils.FileUtils.createPngFileFromBitmap
import kz.eztech.stylyts.presentation.utils.RelativeMeasureUtil
import kz.eztech.stylyts.presentation.utils.ViewUtils.createBitmapScreenshot
import kz.eztech.stylyts.presentation.utils.stick.ImageEntity
import kz.eztech.stylyts.presentation.utils.stick.Layer
import kz.eztech.stylyts.presentation.utils.stick.MotionEntity
import kz.eztech.stylyts.presentation.utils.stick.MotionView
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.NumberFormat
import javax.inject.Inject


class CollectionConstructorFragment : BaseFragment<MainActivity>(),
	CollectionConstructorContract.View,
	View.OnClickListener,
	DialogChooserListener,
	UniversalViewClickListener{

	private lateinit var adapter: CollectionConstructorShopCategoryAdapter
	private lateinit var itemAdapter: CollectionConstructorShopItemAdapter
	private var isItems = false
	private var isStyle = false

	private val listOfItems = ArrayList<ClothesTypeDataModel>()
	private val listOfEntities = ArrayList<ImageEntity>()
	private val listOfIdsChosen = ArrayList<Int>()
	private var currentId : Int = -1
	private var currentStyle:Style? = null
	private var currentCollectionBitmap:Bitmap? = null
	@Inject
	lateinit var presenter:CollectionConstructorPresenter
	override fun getLayoutId(): Int {
		return R.layout.fragment_collection_constructor
	}
	
	override fun getContractView(): BaseView {
		return this
	}
	
	override fun customizeActionBar() {
		with(include_toolbar_profile){
			image_button_left_corner_action.visibility = android.view.View.GONE
			text_view_toolbar_back.visibility = android.view.View.VISIBLE
			text_view_toolbar_title.visibility = android.view.View.VISIBLE
			image_button_right_corner_action.visibility = android.view.View.GONE
			elevation = 0f
			customizeActionToolBar(this, "Создать образ")
		}
	}
	
	override fun initializeDependency() {
		(currentActivity.application as StylytsApp).applicationComponent.inject(this)
	}
	
	override fun initializePresenter() {
		presenter.attach(this)
	}
	
	override fun initializeArguments() {
	
	}
	
	override fun initializeViewsData() {
	
	}
	
	override fun initializeViews() {
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
		frame_layout_fragment_collection_constructor_images_container.attachView(this)
		processDraggedItems()
	}
	
	override fun initializeListeners() {
		text_view_fragment_collection_constructor_total_price.setOnClickListener(this)
		text_view_fragment_collection_constructor_category_back.setOnClickListener(this)
		text_view_fragment_collection_constructor_category_next.setOnClickListener(this)
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
					recycler_view_fragment_collection_constructor_list.adapter = itemAdapter
					recycler_view_fragment_collection_constructor_list.visibility = View.VISIBLE
					list_view_fragment_collection_constructor_list_style.visibility = View.GONE
					isStyle = false
					isItems = true
				} else if (isItems) {
					recycler_view_fragment_collection_constructor_list.adapter = adapter
					isItems = false
					isStyle = false
				} else {
					if (adapter.isSubCategory) {
						adapter.isSubCategory = false
						presenter.getCategory()
					}
				}
			}
			R.id.text_view_fragment_collection_constructor_category_next -> {
				if (isStyle) {
					processPostImages()
				} else if (isItems) {
					isStyle = true
					isItems = false
					recycler_view_fragment_collection_constructor_list.visibility = View.GONE
					presenter.getStyles(
							currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)
									?: ""
					)
				}
			}
		}
	}
	
	override fun onViewClicked(view: View, position: Int, item: Any?) {
		when(view?.id){
			R.id.image_view_item_collection_constructor_category_item_image_holder -> {
				if (isItems) {
					processInputImageToPlace(view, item)
				} else {
					if (!adapter.isSubCategory) {
						(item as GenderCategory).run {
							if(isExternal){
								onChoice(view,externalType)
							}else{
								adapter.isSubCategory = true
								currentId = item.id?:0
								clothes_types?.let { list ->
									list.forEach {
										it.constructor_icon = this.constructor_icon
									}
									recycler_view_fragment_collection_constructor_list.adapter = adapter
									adapter.updateList(list)
								}
							}
						}
					} else {
						(item as ClothesTypes).run {
							presenter.getShopCategoryTypeDetail(item.id ?: 1, "M")
						}
					}
				}
				
			}
		}
	}

	private fun processInputImageToPlace(view: View, item: Any?) {
		item as ClothesTypeDataModel
		Glide.with(currentActivity.applicationContext)
			.asFile()
			.load("http://178.170.221.31:8000${item.cover_photo}")
			.into(object : CustomTarget<File>(){
				override fun onResourceReady(file: File, transition: Transition<in File>?) {
					frame_layout_fragment_collection_constructor_images_container_placeholder.visibility = View.GONE
					val resource = BitmapFactory.decodeFile(file.path)
					frame_layout_fragment_collection_constructor_images_container.post(Runnable {
						val layer = Layer()
						val entity = ImageEntity(layer, resource,item,frame_layout_fragment_collection_constructor_images_container.getWidth(), frame_layout_fragment_collection_constructor_images_container.getHeight())
						frame_layout_fragment_collection_constructor_images_container.addEntityAndPosition(entity)
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
		when(item){
			is Int -> {
				when (item) {
					1 -> {
						val bundle = Bundle()
						bundle.putString(PHOTO_TYPE, BAR_CODE)
						findNavController().navigate(
								R.id.action_createCollectionFragment_to_cameraFragment,
								bundle
						)
					}
					2 -> {
						val bundle = Bundle()
						bundle.putString(PHOTO_TYPE, PHOTO_LIBRARY)
						findNavController().navigate(
								R.id.action_createCollectionFragment_to_photoChooserFragment,
								bundle
						)
					}
					3 -> {
						findNavController().navigate(R.id.action_createCollectionFragment_to_photoChooserFragment)
					}
				}
			}
			is CollectionPostCreateModel -> {
				currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)?.let {
					try {
						currentCollectionBitmap?.let { bitmap ->
							val file =  createPngFileFromBitmap(requireContext(),bitmap)
							file?.let { currentFile ->
								presenter.saveCollection(
										it, item,file
								)
							}?:run {
								displayMessage("Не удалось загрузить данные")
								hideProgress()
							}
							
						}?:run{
							displayMessage("Не удалось загрузить данные")
							hideProgress()
						}
					}catch (e:Exception){
						hideProgress()
						displayMessage("Не удалось загрузить данные")
					}
					
				}
			}
		}
	}
	
	override fun processShopCategories(shopCategoryModel: ShopCategoryModel) {
		shopCategoryModel.menCategory?.let {
			it.add(GenderCategory(
					title = "Добавить категорию",
					isExternal = true,
					externalType = 0,
					externalImageId = R.drawable.ic_baseline_add_circle_outline_24
			))
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
			adapter.isSubCategory = false
			adapter.updateList(it)
		}
	}
	
	override fun processTypeDetail(model: CategoryTypeDetailModel) {
		model.clothes?.data?.let {
			recycler_view_fragment_collection_constructor_list.adapter = itemAdapter
			itemAdapter.updateList(it)
			isItems = true
		}
	}
	

	private fun processDraggedItems(){
		val price = NumberFormat.getInstance().format(listOfItems.sumBy { it.cost ?: 0 })
		text_view_fragment_collection_constructor_total_price.text = "$price тг."
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
								degree = 0f
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
			bundle.putParcelable("photoBitmap",currentCollectionBitmap)
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

		Log.wtf("deletedSelectedEntity","res1:$res res2:$res2 res3:$res3")
		processDraggedItems()
	}

	override fun processSuccess() {
		displayMessage("Успешно добавлено")
		findNavController().navigateUp()
	}
}