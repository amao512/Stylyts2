package kz.eztech.stylyts.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_bottom_item_detail_chooser.*
import kotlinx.android.synthetic.main.dialog_filter_constructor.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.BrandModel
import kz.eztech.stylyts.domain.models.BrandsModel
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionConstructorFilterAdapter
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.constructor.CollectionConstructorFilterContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.constructor.CollectionConstructorFilterPresenter
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 04.02.2021.
 */
class ConstructorFilterDialog: DialogFragment(), View.OnClickListener,
	CollectionConstructorFilterContract.View,UniversalViewClickListener {

	private enum class ConstructorFilterMode(val title:String){
		MAIN("Фильтр"),
		WARDROBE("Мой гардероб"),
		CATEGORIES("Категории"),
		BRANDS("Бренды"),
		COLORS("Цвета"),
		PRICE_RANGE("Диапазон цен"),
		DISCOUNT("Скидка");
	}
	
	@Inject
	lateinit var presenter: CollectionConstructorFilterPresenter
	
	private var currentMode = ConstructorFilterMode.MAIN
	internal var listener: DialogChooserListener? = null
	private lateinit var adapter: CollectionConstructorFilterAdapter
	private var currentList = ArrayList<Any>()
	private var filterMap = HashMap<String,Any>()
	private var chosenGenderCategoryIds = ArrayList<Int>()
	private var currentInt = -1
	
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.dialog_filter_constructor, container, false)
	}
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeDependency()
		initializeArguments()
		initializeViewsData()
		initializeViews()
		initializeListeners()
		customizeActionBar()
		initializePresenter()

		processPostInitialization()
		presenter.attach(this)
	}
	fun setChoiceListener(listener: DialogChooserListener){
		this.listener = listener
	}

	override fun onStop() {
		super.onStop()
		disposeRequests()
	}

	override fun customizeActionBar() {
		with(include_dialog_filter_toolbar){
			image_button_left_corner_action.visibility = View.VISIBLE
			image_button_left_corner_action.setImageResource(R.drawable.ic_baseline_close_24)
			image_button_left_corner_action.setOnClickListener {
				processDismiss()
			}
			text_view_toolbar_back.visibility = View.GONE
			text_view_toolbar_title.visibility = View.GONE
			image_button_right_corner_action.visibility = View.GONE
			text_view_toolbar_right_text.text = "Очистить"
			text_view_toolbar_right_text.setOnClickListener {
				clearFilter()
			}
			if(filterMap.isNotEmpty()){
				text_view_toolbar_right_text.visibility = View.VISIBLE
				button_dialog_filter_constructor_submit.visibility = View.VISIBLE
			}else{
				text_view_toolbar_right_text.visibility = View.GONE
				button_dialog_filter_constructor_submit.visibility = View.GONE
			}
			
			elevation = 0f
		}
	}

	override fun initializeDependency() {
		(activity?.application as StylytsApp).applicationComponent.inject(this)
	}

	override fun initializePresenter() {

	}

	override fun initializeArguments() {
		arguments?.let {
			if(it.containsKey("filterMap")){
				filterMap.putAll(it.getSerializable("filterMap") as Map<out String, Any>)
			}
			if(it.containsKey("currentType")){
				currentInt = it.getInt("currentType")
			}
		}
	}

	override fun initializeViewsData() {
		adapter = CollectionConstructorFilterAdapter()
		adapter.setOnClickListener(this)
		activity?.let {
			presenter.initToken((it as MainActivity).getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY))
		}
		recycler_view_dialog_filter_constructor_list.layoutManager = LinearLayoutManager(activity)
		recycler_view_dialog_filter_constructor_list.adapter = adapter
	}
	
	override fun onViewClicked(view: View, position: Int, item: Any?) {
		when(item){
			is BrandModel -> {
				val model = currentList[currentList.indexOf(item)] as BrandModel
				model.isChosen = true
				model.id?.let {
					filterMap["brand"] = it
				}
				
				updateAdapter()
			}
			is GenderCategory -> {
				if(chosenGenderCategoryIds.contains(item.chosenClothesTypes)){
					chosenGenderCategoryIds.remove(item.chosenClothesTypes)
				}else{
					chosenGenderCategoryIds.add(item.chosenClothesTypes as Int)
				}
				filterMap["clothes_type"] = chosenGenderCategoryIds.joinToString()
			}
		}
	}
	
	override fun initializeViews() {
		frame_layout_dialog_filter_constructor_main.visibility = View.VISIBLE
		recycler_view_dialog_filter_constructor_list.visibility = View.GONE
	}

	override fun initializeListeners() {
		text_view_dialog_filter_constructor_my_wardrobe.setOnClickListener(this)
		text_view_dialog_filter_constructor_categories.setOnClickListener(this)
		text_view_dialog_filter_constructor_brands.setOnClickListener(this)
		text_view_dialog_filter_constructor_colors.setOnClickListener(this)
		text_view_dialog_filter_constructor_price_range.setOnClickListener(this)
		text_view_dialog_filter_constructor_discount.setOnClickListener(this)
		button_dialog_filter_constructor_submit.setOnClickListener(this)
	}

	override fun onClick(v: View?) {
		when(v?.id){
			R.id.text_view_dialog_filter_constructor_my_wardrobe -> {
				currentMode = ConstructorFilterMode.WARDROBE
				processCurrentMode(currentMode)
			}
			R.id.text_view_dialog_filter_constructor_categories -> {
				currentMode = ConstructorFilterMode.CATEGORIES
				processCurrentMode(ConstructorFilterMode.CATEGORIES)
			}
			R.id.text_view_dialog_filter_constructor_brands -> {
				currentMode = ConstructorFilterMode.BRANDS
				processCurrentMode(ConstructorFilterMode.BRANDS)
			}
			R.id.text_view_dialog_filter_constructor_colors -> {
				currentMode = ConstructorFilterMode.COLORS
				processCurrentMode(ConstructorFilterMode.COLORS)
			}
			R.id.text_view_dialog_filter_constructor_price_range -> {
				currentMode = ConstructorFilterMode.PRICE_RANGE
				processCurrentMode(ConstructorFilterMode.PRICE_RANGE)
			}
			R.id.text_view_dialog_filter_constructor_discount -> {
				currentMode = ConstructorFilterMode.DISCOUNT
				processCurrentMode(ConstructorFilterMode.DISCOUNT)
			}
			R.id.button_dialog_filter_constructor_submit -> {
				processDismiss()
			}
		}
	}


	override fun processPostInitialization() {
	
	}

	override fun processBrands(models: BrandsModel) {
		models.results?.let {
			if(filterMap.containsKey("brand")){
				val id = filterMap["brand"]
				it.forEach {  model ->
					if(model.id == id){
						model.isChosen = true
					}
				}
			}
			currentList.clear()
			currentList.addAll(it)
			updateAdapter()
			
		}
	}
	
	override fun processShopCategories(shopCategoryModel: ShopCategoryModel) {
		shopCategoryModel.menCategory?.let {
			val list = it.map { it.id == currentInt }
			currentList.clear()
			currentList.addAll(list)
			updateAdapter()
		}
	}
	
	private fun updateAdapter(){
		adapter.updateList(currentList)
		adapter.notifyDataSetChanged()
	}
	
	private fun processCurrentInitialCurrentMode(){
		processCurrentMode(currentMode)
	}

	private fun processCurrentMode(mode:ConstructorFilterMode){
		currentMode = mode
		frame_layout_dialog_filter_constructor_main.visibility = View.GONE
		recycler_view_dialog_filter_constructor_list.visibility = View.VISIBLE
		customizeFilterToolBar()
		when(currentMode){
			ConstructorFilterMode.MAIN -> {
				customizeActionBar()
				initializeViews()
			}
			ConstructorFilterMode.WARDROBE -> {

			}
			ConstructorFilterMode.CATEGORIES -> {
				presenter.getCategories()
			}
			ConstructorFilterMode.BRANDS -> {
				presenter.getBrands()
			}
			ConstructorFilterMode.COLORS -> {

			}
			ConstructorFilterMode.PRICE_RANGE -> {

			}
			ConstructorFilterMode.DISCOUNT -> {

			}
		}
		
	}

	private fun customizeFilterToolBar(){
		with(include_dialog_filter_toolbar){
			image_button_left_corner_action.visibility = View.VISIBLE
			image_button_left_corner_action.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
			image_button_left_corner_action.setOnClickListener {
				processBack()
			}
			text_view_toolbar_back.visibility = View.GONE
			text_view_toolbar_title.visibility = View.VISIBLE
			text_view_toolbar_title.text = currentMode.title
			image_button_right_corner_action.visibility = View.GONE
			text_view_toolbar_right_text.visibility = View.VISIBLE
			text_view_toolbar_right_text.text = "Сбросить"
			text_view_toolbar_right_text.setOnClickListener {
				clearFilter()
				processCurrentInitialCurrentMode()
			}

			elevation = 0f
		}
	}

	private fun processBack(){
		processCurrentMode(ConstructorFilterMode.MAIN)
	}
	private fun processResetFilter(){

	}
	private fun processDismiss(){
		listener?.onChoice(button_dialog_filter_constructor_submit,filterMap)
		dismiss()
	}
	override fun disposeRequests() {
		presenter.disposeRequests()
	}

	override fun displayMessage(msg: String) {
		(activity as MainActivity).displayToast(msg)
	}

	override fun isFragmentVisible(): Boolean {
		return isVisible
	}

	override fun displayProgress() {

	}

	override fun hideProgress() {

	}
	override fun getTheme(): Int {
		return R.style.FullScreenDialog
	}
	
	override fun clearFilter() {
		filterMap.clear()
		customizeActionBar()
	}
}