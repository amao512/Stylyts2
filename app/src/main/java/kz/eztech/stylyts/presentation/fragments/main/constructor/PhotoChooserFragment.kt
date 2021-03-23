package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.*
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_clothes_grid.view.*
import kotlinx.android.synthetic.main.fragment_photo_chooser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageItemFilteredAdapter
import kz.eztech.stylyts.presentation.adapters.MainImagesAdditionalAdapter
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.collections.PhotoChooserContract
import kz.eztech.stylyts.presentation.dialogs.ConstructorFilterDialog
import kz.eztech.stylyts.presentation.dialogs.CreateCollectionAcceptDialog
import kz.eztech.stylyts.presentation.interfaces.MotionViewTapListener
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.constructor.PhotoChooserPresenter
import kz.eztech.stylyts.presentation.utils.ViewUtils.createBitmapScreenshot
import kz.eztech.stylyts.presentation.utils.stick.ImageEntity
import kz.eztech.stylyts.presentation.utils.stick.Layer
import kz.eztech.stylyts.presentation.utils.stick.MotionEntity
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PhotoChooserFragment : BaseFragment<MainActivity>(),PhotoChooserContract.View,
    UniversalViewClickListener,
    DialogChooserListener,MotionViewTapListener {
    private var photoUri:Uri? = null
    
    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var filterList:ArrayList<CollectionFilterModel>
    private lateinit var genderCategoryList: List<GenderCategory>
    
    private lateinit var filteredAdapter:GridImageItemFilteredAdapter
    private lateinit var selectedAdapter: MainImagesAdditionalAdapter
    private lateinit var createCollecationDialog:CreateCollectionAcceptDialog
    private val selectedList = ArrayList<ClothesMainModel>()
    private lateinit var filterDialog:ConstructorFilterDialog
    private val filterMap = HashMap<String, Any>()
    @Inject
    lateinit var presenter:PhotoChooserPresenter
    
    override fun getLayoutId(): Int {
        return R.layout.fragment_photo_chooser
    }

    override fun getContractView(): BaseView {
       return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_photo_chooser){
            toolbar_left_corner_action_image_button.visibility = android.view.View.GONE
            toolbar_back_text_view.visibility = android.view.View.GONE
            toolbar_title_text_view.visibility = android.view.View.VISIBLE
            toolbar_left_corner_action_image_button.visibility = android.view.View.GONE
            toolbar_right_text_text_view.visibility = android.view.View.VISIBLE
            toolbar_right_text_text_view.text = "Готово"
            toolbar_right_text_text_view.setOnClickListener {
                showCompleteDialog()
            }
            elevation = 0f
            customizeActionToolBar(this, "Создать Публикацию")
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if(it.containsKey("uri")){
                photoUri = it.getParcelable("uri")
            }
        }
    }

    override fun initializeViewsData() {
    
    }

    override fun initializeViews() {
        // Request camera permissions
        filterDialog = ConstructorFilterDialog()
        filterDialog.setChoiceListener(this)
        updatePhoto(photoUri)
        filterList = ArrayList()
        filteredAdapter = GridImageItemFilteredAdapter()
        filterAdapter = CollectionsFilterAdapter()
        selectedAdapter = MainImagesAdditionalAdapter()
    
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser_filter_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser_filter_list.adapter = filterAdapter
    
    
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser.layoutManager = GridLayoutManager(context, 2)
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser.adapter = filteredAdapter
    
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser_filter_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser_filter_list.adapter = filterAdapter
    
        recycler_view_fragment_photo_chooser_selected_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_fragment_photo_chooser_selected_list.adapter = selectedAdapter
        
        
        motion_view_fragment_photo_chooser_tags_container.setCustomDeleteIcon(R.drawable.ic_baseline_close_20)
        motion_view_fragment_photo_chooser_tags_container.setFlexible(false)
        motion_view_fragment_photo_chooser_tags_container.attachView(this)
        motion_view_fragment_photo_chooser_tags_container.setTapListener(this)
        createCollecationDialog = CreateCollectionAcceptDialog()
        createCollecationDialog.setChoiceListener(this)
        val bundle = Bundle()
        bundle.putParcelable("photoUri", photoUri)
        bundle.putBoolean("isChooser", true)
        createCollecationDialog.arguments = bundle
        createCollecationDialog.show(childFragmentManager, "PhotoChossoserTag")
        hideBottomSheet()
    }
    
    override fun deleteSelectedView(motionEntity: MotionEntity) {
        selectedList.remove(motionEntity.item)
        selectedAdapter.updateList(selectedList)
        checkEmptyList()
    }
    
    
    private fun addClotheTag(clothe: ClothesMainModel){
        val layer = Layer()
        val textView = layoutInflater.inflate(R.layout.text_view_tag_element, motion_view_fragment_photo_chooser_tags_container, false) as TextView
        textView.text = clothe.title
        motion_view_fragment_photo_chooser_tags_container.addView(textView)
        val observer = textView.viewTreeObserver
        if(observer.isAlive){
            observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val resource = createBitmapScreenshot(textView)
                    val entity = ImageEntity(layer, resource, clothe, motion_view_fragment_photo_chooser_tags_container.getWidth(), motion_view_fragment_photo_chooser_tags_container.getHeight())
                    motion_view_fragment_photo_chooser_tags_container.addEntityAndPosition(entity, true)
                    textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    motion_view_fragment_photo_chooser_tags_container.removeView(textView)
                    selectedList.add(clothe)
                    selectedAdapter.updateList(selectedList)
                    hideBottomSheet()
                }
            })
        }else{
            Log.wtf("observer", "not alive")
        }
    }
    
    override fun updatePhoto(path: Uri?) {
        path?.let {
            Glide.with(this).load(path).into(this.image_view_fragment_photo_chooser)
        }?:run{
            displayMessage("Упс, что то пошло не так :(")
        }
    }

    override fun initializeListeners() {
        filterAdapter.setOnClickListener(this)
        filteredAdapter.setOnClickListener(this)
        selectedAdapter.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        presenter.getCategory(currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)
                ?: "")
    }
    
    override fun processFilteredItems(model: FilteredItemsModel) {
        model.results?.let {
            filteredAdapter.updateList(it)
        }
    }
    
    override fun processShopCategories(shopCategoryModel: ShopCategoryModel) {
        shopCategoryModel.menCategory?.let{
            genderCategoryList = it
            filterList.clear()
            filterList.add(CollectionFilterModel("Фильтр", 0, "M", 1))
            it.forEach { category ->
                filterList.add(CollectionFilterModel(category.title, category.id, "M", 0))
                filterAdapter.updateList(filterList)
            }
        }
    }
    
    override fun getFilterMap(): HashMap<String, Any> {
        return filterMap
    }

    override fun onSingleTapUp() {
        showBottomSheet()
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when(item){
            is CollectionFilterModel -> {
                when (item.mode) {
                    0 -> {
                        val model = genderCategoryList.find { it.id == item.id }
                        model?.let {
                            it.clothes_types?.let { clothes ->
                                filterMap["clothes_type"] = clothes.map { it.id }.joinToString()
                                presenter.getShopCategoryTypeDetail(
                                        currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)
                                                ?: "",
                                        filterMap)
                            }
                        }
                        filterList.forEach {
                            it.isChosen = false
                        }
                        filterList[filterList.indexOf(item)].isChosen = true
                        filterAdapter.updateList(filterList)
                    }
                    1 -> {
                        filterDialog.show(childFragmentManager, "FilterDialog")
                    }
                }
            }
            is ClothesMainModel -> {
                when (view.id) {
                    R.id.shapeable_image_view_item_collection_image -> {
                        addClotheTag(item)
                    }
                    R.id.frame_layout_item_main_image_holder_container -> {
                
                    }
                }
        
            }
        }
    }
    
    override fun onChoice(v: View?, item: Any?) {
        when(v?.id){
            R.id.frame_layout_dialog_create_collection_accept_choose_clothes -> {
                linear_layout_fragment_photo_chooser_container.visibility = View.VISIBLE
                bottom_sheet_clothes.visibility = View.VISIBLE
            }
            R.id.toolbar_back_text_view -> {
                findNavController().navigateUp()
            }
            R.id.toolbar_right_text_text_view -> {
                displayMessage("Успешно добавлено")
                val intent = Intent(currentActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                currentActivity.startActivity(intent)
                (currentActivity.finish())
        
            }
            R.id.button_dialog_filter_constructor_submit -> {
                val map = item as HashMap<String, Any>
                val clothes_type = filterMap["clothes_type"]
                filterMap.clear()
                filterMap["clothes_type"] = clothes_type as String
                filterMap.putAll(map)
                
                
                presenter.getShopCategoryTypeDetail(
                        currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY) ?: "",
                        filterMap)
            }
        }
    }
    
    private fun showBottomSheet(){
        BottomSheetBehavior.from(bottom_sheet_clothes).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            isHideable = false
        }
        recycler_view_fragment_photo_chooser_selected_list.visibility = View.GONE
        linear_layout_fragment_photo_chooser_desc_container.visibility = View.GONE
        text_view_fragment_photo_chooser_empty_text.visibility = View.GONE
    }
    
    private fun hideBottomSheet(){
        BottomSheetBehavior.from(bottom_sheet_clothes).apply {
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    
        checkEmptyList()
    }
    private fun checkEmptyList(){
        if(selectedList.isEmpty()){
            recycler_view_fragment_photo_chooser_selected_list.visibility = View.GONE
            text_view_fragment_photo_chooser_empty_text.visibility = View.VISIBLE
            linear_layout_fragment_photo_chooser_desc_container.visibility = View.GONE
        }else{
            linear_layout_fragment_photo_chooser_desc_container.visibility = View.VISIBLE
            recycler_view_fragment_photo_chooser_selected_list.visibility = View.VISIBLE
            text_view_fragment_photo_chooser_empty_text.visibility = View.GONE
        }
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

    }

    override fun hideProgress() {

    }
    
    private fun showCompleteDialog(){
        val bundle = Bundle()
        bundle.putParcelable("photoUri", photoUri)
        bundle.putBoolean("isChooser", true)
        if(selectedList.isNotEmpty()){
            bundle.putParcelableArrayList("clothes", selectedList)
        }
        createCollecationDialog.arguments = bundle
        createCollecationDialog.show(childFragmentManager, "PhotoChossoserTag")
    }

}