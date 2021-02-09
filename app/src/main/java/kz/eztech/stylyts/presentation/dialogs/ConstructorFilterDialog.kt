package kz.eztech.stylyts.presentation.dialogs

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_bottom_item_detail_chooser.*
import kotlinx.android.synthetic.main.dialog_filter_constructor.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.BrandsModel
import kz.eztech.stylyts.domain.models.ClothesColor
import kz.eztech.stylyts.domain.models.ClothesSize
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.constructor.CollectionConstructorFilterContract

/**
 * Created by Ruslan Erdenoff on 04.02.2021.
 */
class ConstructorFilterDialog: DialogFragment(), View.OnClickListener,
	CollectionConstructorFilterContract.View {

	private enum class ConstructorFilterMode(val title:String){
		MAIN("Фильтр"),
		WARDROBE("Мой гардероб"),
		CATEGORIES("Категории"),
		BRANDS("Бренды"),
		COLORS("Цвета"),
		PRICE_RANGE("Диапазон цен"),
		DISCOUNT("Скидка");
	}
	private var currentMode = ConstructorFilterMode.MAIN
	internal var listener: DialogChooserListener? = null
	
	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.dialog_filter_constructor, container, false)
	}
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeArguments()
		initializeViewsData()
		initializeViews()
		initializeListeners()
		customizeActionBar()
		initializePresenter()

		processPostInitialization()
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
			text_view_toolbar_right_text.visibility = View.GONE
			elevation = 0f
		}
	}

	override fun initializeDependency() {

	}

	override fun initializePresenter() {

	}

	override fun initializeArguments() {

	}

	override fun initializeViewsData() {

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
	}

	override fun onClick(v: View?) {
		when(v?.id){
			R.id.text_view_dialog_filter_constructor_my_wardrobe -> {
				processCurrentMode(ConstructorFilterMode.WARDROBE)
			}
			R.id.text_view_dialog_filter_constructor_categories -> {
				processCurrentMode(ConstructorFilterMode.CATEGORIES)
			}
			R.id.text_view_dialog_filter_constructor_brands -> {
				processCurrentMode(ConstructorFilterMode.BRANDS)
			}
			R.id.text_view_dialog_filter_constructor_colors -> {
				processCurrentMode(ConstructorFilterMode.COLORS)
			}
			R.id.text_view_dialog_filter_constructor_price_range -> {
				processCurrentMode(ConstructorFilterMode.PRICE_RANGE)
			}
			R.id.text_view_dialog_filter_constructor_discount -> {
				processCurrentMode(ConstructorFilterMode.DISCOUNT)
			}
		}
	}


	override fun processPostInitialization() {

	}

	override fun processBrands(models: BrandsModel) {

	}

	private fun processCurrentMode(mode:ConstructorFilterMode){
		currentMode = mode
		frame_layout_dialog_filter_constructor_main.visibility = View.GONE
		recycler_view_dialog_filter_constructor_list.visibility = View.VISIBLE

		when(currentMode){
			ConstructorFilterMode.MAIN -> {
				customizeActionBar()
				initializeViews()
			}
			ConstructorFilterMode.WARDROBE -> {

			}
			ConstructorFilterMode.CATEGORIES -> {

			}
			ConstructorFilterMode.BRANDS -> {

			}
			ConstructorFilterMode.COLORS -> {

			}
			ConstructorFilterMode.PRICE_RANGE -> {

			}
			ConstructorFilterMode.DISCOUNT -> {

			}
		}
		customizeFilterToolBar()
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
				processResetFilter()
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
		dismiss()
	}
	override fun disposeRequests() {

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
}