package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_HTML
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_constructor.*
import kotlinx.android.synthetic.main.fragment_collection_constructor.include_toolbar_profile
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_collection_constructor_category_item.view.*
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
import kz.eztech.stylyts.presentation.utils.RelativeMeasureUtil
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
	private val listOfViews = ArrayList<ImageView>()
	private var currentStyle:Style? = null
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
			image_button_right_corner_action.visibility = android.view.View.VISIBLE
			image_button_right_corner_action.setOnClickListener{
				Log.wtf("ImagePhoto", "Here")
				val chooserDialog = PhotoChooserDialog()
				chooserDialog.setChoiceListener(this@CollectionConstructorFragment)
				chooserDialog.show(childFragmentManager, "PhotoChooserTag")
			}
			image_button_right_corner_action.setImageResource(kz.eztech.stylyts.R.drawable.ic_camera)
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
		currentActivity.hideBottomNavigationView()
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
		processDraggedItems()
	}
	
	override fun initializeListeners() {
		frame_layout_fragment_collection_constructor_images_container.setOnDragListener(dragListen)
		recycler_view_fragment_collection_constructor_list.setOnDragListener(dragRecyclerListener)
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
				if(isStyle){
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
				if(isStyle){
					processPostImages()
				} else if(isItems){
					isStyle = true
					isItems = false
					recycler_view_fragment_collection_constructor_list.visibility= View.GONE
					presenter.getStyles(currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)?:"")
				}
			}
		}
	}
	
	override fun onViewClicked(view: View, position: Int, item: Any?) {
		when(view?.id){
			R.id.image_view_item_collection_constructor_category_item_image_holder -> {
				if (isItems) {
					//findNavController().navigate(R.id.action_createCollectionFragment_to_itemDetailFragment)
				} else {
					if (!adapter.isSubCategory) {
						adapter.isSubCategory = true
						(item as GenderCategory).run {
							clothes_types?.let { list ->
								recycler_view_fragment_collection_constructor_list.adapter = adapter
								adapter.updateList(list)
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
	
	override fun onChoice(v: View?, item: Any?) {
		when(item){
			is Int -> {
				when (item){
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
			is CollectionPostCreateModel ->{
				currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)?.let {
					presenter.saveCollection(
						it,item)
				}
			}
		}
	}
	
	override fun processShopCategories(shopCategoryModel: ShopCategoryModel) {
		shopCategoryModel.menCategory?.let {
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
	
	override fun copyImageView(
		view: ImageView,
		positionX: Float,
		positionY: Float,
		item: ClothesTypeDataModel?
	): ImageView {
		var childView = ImageView(currentActivity)
		childView.layoutParams =  ViewGroup.LayoutParams(
			view.layoutParams.width,
			view.layoutParams.height
		)
		childView.layoutParams.width = childView.layoutParams.width*2
		childView.layoutParams.height = childView.layoutParams.height*2
		childView.x = positionX - childView.layoutParams.width/2
		childView.y = positionY - childView.layoutParams.height/2
		childView.scaleX = 1f
		childView.scaleY = 1f
		childView.setImageDrawable(view.drawable)
		childView.setOnLongClickListener {
			initializeChildDrag(it, item)
			true
		}
		item?.let {
			listOfItems.add(it)
			listOfViews.add(childView)
		}
		return childView
	}

	private fun createImage(positionX: Float, positionY: Float, width: Float, height: Float):ImageView{
		var childView = ImageView(currentActivity)
		childView.x = positionX
		childView.y = positionY
		childView.layoutParams = ViewGroup.LayoutParams(width.toInt(), height.toInt())
		childView.layoutParams.height = height.toInt()
		childView.setImageResource(R.drawable.jacket)
		return childView
	}
	
	private fun initializeChildDrag(v: View, item: ClothesTypeDataModel?){
		val intent = Intent()
		intent.putExtra("clothesmodel", item)
		val item = ClipData.Item(v.tag as? CharSequence, intent, null)
		val dragData = ClipData(
			v.tag as? CharSequence,
			arrayOf(MIMETYPE_TEXT_HTML),
			item
		)
		val myShadow = MyDragShadowBuilder(v)
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
			v.startDragAndDrop(
				dragData,
				myShadow,
				v,
				0
			)
		} else {
			v.startDrag(
				dragData,
				myShadow,
				v,
				0
			)
		}
	}
	
	private class MyDragShadowBuilder(v: View) : View.DragShadowBuilder(v) {
		private val currentView = v
		private var mScaleFactor: Point? = null
		// Defines a callback that sends the drag shadow dimensions and touch point back to the
		// system.
		override fun onProvideShadowMetrics(size: Point, touch: Point) {
			// Sets the width of the shadow to half the width of the original View
			val width: Int = view.width
			
			// Sets the height of the shadow to half the height of the original View
			val height: Int = view.height
			
			
			
			// Sets the size parameter's width and height values. These get back to the system
			// through the size parameter.
			size.set(width, height)
			mScaleFactor = size;
			currentView.requestLayout()
			
			// Sets the touch point's position to be in the middle of the drag shadow
			touch.set(width / 2, height / 2)
		}
		
		// Defines a callback that draws the drag shadow in a Canvas that the system constructs
		// from the dimensions passed in onProvideShadowMetrics().
		override fun onDrawShadow(canvas: Canvas) {
			// Draws the ColorDrawable in the Canvas passed in from the system.
			mScaleFactor?.let {
				canvas.scale(
					1f, 1f
				)
			}
			
			currentView.draw(canvas)
		}
	}
	
	
	private val dragListen = View.OnDragListener { v, event ->
		
		// Handles each of the expected events
		when (event.action) {
			DragEvent.ACTION_DRAG_STARTED -> {
				event.getClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN) ||
						event.getClipDescription().hasMimeType(MIMETYPE_TEXT_HTML)
			}
			DragEvent.ACTION_DRAG_ENTERED -> {
				true;
			}

			DragEvent.ACTION_DRAG_LOCATION ->
				true
			DragEvent.ACTION_DRAG_EXITED -> {
				true
			}
			DragEvent.ACTION_DROP -> {
				try {
					val view = event.localState as ImageView
					if (view != null) {
						when (event.clipDescription.getMimeType(0)) {
							MIMETYPE_TEXT_HTML -> {
								val owner = view.parent as ViewGroup
								val container = v as FrameLayout
								container.removeView(view)
								view.x = event.x - view.layoutParams.width / 2
								view.y = event.y - view.layoutParams.height / 2
								owner.addView(view)
								owner.requestLayout()
								view.visibility = View.VISIBLE

								true
							}
							MIMETYPE_TEXT_PLAIN -> {
								val owner = view.parent as ViewGroup
								val container = v as FrameLayout
								val intent = event.clipData.getItemAt(0).intent
								intent.setExtrasClassLoader(ClothesTypeDataModel::class.java.classLoader)
								val currentItem =
									intent.getParcelableExtra<ClothesTypeDataModel>("clothesmodel")
								container.addView(
									copyImageView(
										view,
										event.x,
										event.y,
										currentItem
									)
								)
								owner.requestLayout()
								view.visibility = View.VISIBLE


								true
							}
							else -> {
								false
							}
						}

					} else false

				} catch (e: Exception) {
					false
				}

			}

			DragEvent.ACTION_DRAG_ENDED -> {

				// Invalidates the view to force a redraw
				v.invalidate()

				// Does a getResult(), and displays what happened.
				when (event.result) {
					true -> {
						processDraggedItems()
					}

					else -> {
					}
				}

				// returns true; the value is ignored.
				true
			}
			else -> {
				// An unknown action type was received.
				Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
				false
			}
		}
	}
	
	private val dragRecyclerListener = View.OnDragListener { v, event ->
		
		// Handles each of the expected events
		when (event.action) {
			DragEvent.ACTION_DRAG_STARTED -> {
				event.getClipDescription().hasMimeType(MIMETYPE_TEXT_HTML)
			}
			DragEvent.ACTION_DRAG_ENTERED -> {
				true;
			}

			DragEvent.ACTION_DRAG_LOCATION ->
				true
			DragEvent.ACTION_DRAG_EXITED -> {
				true
			}
			DragEvent.ACTION_DROP -> {
				try {
					val view = event.localState as ImageView
					if (view != null) {
						val owner = view.parent as ViewGroup
						val container = v as RecyclerView
						val intent = event.clipData.getItemAt(0).intent
						intent.setExtrasClassLoader(ClothesTypeDataModel::class.java.classLoader)
						val currentItem =
							intent.getParcelableExtra<ClothesTypeDataModel>("clothesmodel")
						if (listOfItems.contains(currentItem)) {
							listOfItems.remove(currentItem)
							listOfViews.remove(view)
						}
						owner.removeView(view)
						owner.requestLayout()
						true
					} else false

				} catch (e: Exception) {
					false
				}

			}

			DragEvent.ACTION_DRAG_ENDED -> {

				// Invalidates the view to force a redraw
				v.invalidate()

				// Does a getResult(), and displays what happened.
				when (event.result) {
					true -> {
						processDraggedItems()
					}
					else -> {

					}
				}

				// returns true; the value is ignored.
				true
			}
			else -> {
				// An unknown action type was received.
				Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
				false
			}
		}
	}

	private fun processDraggedItems(){
		val price = NumberFormat.getInstance().format(listOfItems.sumBy { it.cost ?: 0 })
		text_view_fragment_collection_constructor_total_price.text = "$price тг."
	}

	private fun processPostImages(){
		if(listOfItems.isNotEmpty() && listOfViews.isNotEmpty() && currentStyle!=null){
			val collectionPostCreateModel = CollectionPostCreateModel()

			collectionPostCreateModel.style = currentStyle!!.id
			val clth = ArrayList<Int>()



			val loccloth = ArrayList<ClothesCollection>()

			listOfViews.forEachIndexed { index, imageView ->
				val result = RelativeMeasureUtil.measureView(
					imageView,
					frame_layout_fragment_collection_constructor_images_container
				)
				loccloth.add(ClothesCollection(
					clothes_id = listOfItems[index].id,
					point_x = result.point_x,
					point_y = result.point_y,
					width = result.width,
					height = result.height,
					degree = 0f
				))
				clth.add(listOfItems[index].id?:0)
			}
			collectionPostCreateModel.clothes = clth

			collectionPostCreateModel.clothes_location = loccloth
			collectionPostCreateModel.author = currentActivity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY)
			collectionPostCreateModel.total_price = listOfItems.sumBy { it.cost?:0 }.toFloat()

			val createCollecationDialog = CreateCollectionAcceptDialog()
			val bundle = Bundle()
			bundle.putParcelable("collectionModel",collectionPostCreateModel)
			createCollecationDialog.arguments = bundle
			createCollecationDialog.setChoiceListener(this@CollectionConstructorFragment)
			createCollecationDialog.show(childFragmentManager, "PhotoChossoserTag")

		}else{
			displayMessage("Не все данные заполнены")
		}


	}

	override fun processStyles(list: List<Style>) {
		val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
			currentActivity,R.layout.item_style, list.map { it.title }
		)
		recycler_view_fragment_collection_constructor_list.visibility = View.GONE
		list_view_fragment_collection_constructor_list_style.adapter = adapter
		list_view_fragment_collection_constructor_list_style.visibility = View.VISIBLE
		list_view_fragment_collection_constructor_list_style.setOnItemClickListener { parent, view, position, id ->
			currentStyle = list[position]
			processPostImages()
		}

	}

	override fun processSuccess() {
		displayMessage("Успешно добавлено")
		findNavController().navigateUp()
	}
}