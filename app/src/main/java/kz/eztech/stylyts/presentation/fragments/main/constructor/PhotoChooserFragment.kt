package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_clothes_grid.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_clothes_grid.view.*
import kotlinx.android.synthetic.main.dialog_create_collection_accept.*
import kotlinx.android.synthetic.main.fragment_collection_constructor.*
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kotlinx.android.synthetic.main.fragment_photo_chooser.*
import kotlinx.android.synthetic.main.fragment_profile_income_detail.*
import kotlinx.android.synthetic.main.fragment_profile_income_detail.include_toolbar_income_detail
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kotlinx.android.synthetic.main.item_main_image_detail.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageItemFilteredAdapter
import kz.eztech.stylyts.presentation.adapters.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.collections.PhotoChooserContract
import kz.eztech.stylyts.presentation.dialogs.CreateCollectionAcceptDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.constructor.PhotoChooserPresenter
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.RelativeImageMeasurements
import kz.eztech.stylyts.presentation.utils.ViewUtils.createBitmapScreenshot
import kz.eztech.stylyts.presentation.utils.stick.ImageEntity
import kz.eztech.stylyts.presentation.utils.stick.Layer
import kz.eztech.stylyts.presentation.utils.stick.MotionEntity
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class PhotoChooserFragment : BaseFragment<MainActivity>(),PhotoChooserContract.View,UniversalViewClickListener, DialogChooserListener {
    private var photoUri:Uri? = null
    
    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var filterList:ArrayList<CollectionFilterModel>
    private lateinit var genderCategoryList: List<GenderCategory>
    
    private lateinit var filteredAdapter:GridImageItemFilteredAdapter
    private lateinit var selectedAdapter: MainImagesAdditionalAdapter
    private lateinit var createCollecationDialog:CreateCollectionAcceptDialog
    private val selectedList = ArrayList<ClothesMainModel>()
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
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.GONE
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            image_button_left_corner_action.visibility = android.view.View.VISIBLE
            image_button_left_corner_action.setImageResource(kz.eztech.stylyts.R.drawable.ic_drawer)
            image_button_left_corner_action.setOnClickListener {
                showBottomSheet()
            }
            text_view_toolbar_right_text.visibility = android.view.View.VISIBLE
            text_view_toolbar_right_text.text = "Готово"
            text_view_toolbar_right_text.setOnClickListener {
                showCompleteDialog()
            }
            elevation = 0f
            customizeActionToolBar(this,"Создать Публикацию")
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
        updatePhoto(photoUri)
        filterList = ArrayList()
        filteredAdapter = GridImageItemFilteredAdapter()
        filterAdapter = CollectionsFilterAdapter()
        selectedAdapter = MainImagesAdditionalAdapter()
    
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser_filter_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser_filter_list.adapter = filterAdapter
    
    
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser.layoutManager = GridLayoutManager(context,2)
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser.adapter = filteredAdapter
    
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser_filter_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottom_sheet_clothes.recycler_view_fragment_photo_chooser_filter_list.adapter = filterAdapter
    
        recycler_view_fragment_photo_chooser_selected_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_fragment_photo_chooser_selected_list.adapter = selectedAdapter
        
        
        motion_view_fragment_photo_chooser_tags_container.setCustomDeleteIcon(R.drawable.ic_baseline_close_20)
        motion_view_fragment_photo_chooser_tags_container.setFlexible(false)
        motion_view_fragment_photo_chooser_tags_container.attachView(this)
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
    
    
    private fun addClotheTag(clothe:ClothesMainModel){
        val layer = Layer()
        val textView = layoutInflater.inflate(R.layout.text_view_tag_element,motion_view_fragment_photo_chooser_tags_container,false) as TextView
        textView.text = clothe.title
        motion_view_fragment_photo_chooser_tags_container.addView(textView)
        val observer = textView.viewTreeObserver
        if(observer.isAlive){
            observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    val resource = createBitmapScreenshot(textView)
                    val entity = ImageEntity(layer, resource, clothe, motion_view_fragment_photo_chooser_tags_container.getWidth(), motion_view_fragment_photo_chooser_tags_container.getHeight())
                    motion_view_fragment_photo_chooser_tags_container.addEntityAndPosition(entity,true)
                    textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    motion_view_fragment_photo_chooser_tags_container.removeView(textView)
                    selectedList.add(clothe)
                    selectedAdapter.updateList(selectedList)
                    hideBottomSheet()
                }
            })
        }else{
            Log.wtf("observer","not alive")
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
            filterList.add(CollectionFilterModel("Фильтр",0,"M",1))
            it.forEach { category ->
                filterList.add(CollectionFilterModel(category.title,category.id,"M",0))
                filterAdapter.updateList(filterList)
            }
        }
    }
    
    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when(item){
            is CollectionFilterModel -> {
                when(item.mode){
                    0 -> {
                        val model = genderCategoryList.find { it.id == item.id }
                        model?.let {
                            it.clothes_types?.let { clothes ->
                                val map = HashMap<String,Any>()
                                map["clothes_type"] = clothes.map { it.id }.joinToString()
                                presenter.getShopCategoryTypeDetail(
                                        currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY) ?: "",
                                        map)
                            }
                        }
                        filterList.forEach {
                            it.isChosen = false
                        }
                        filterList[filterList.indexOf(item)].isChosen = true
                        filterAdapter.updateList(filterList)
                    }
                    1 -> {
                        displayMessage("Фильтры")
                    }
                }
            }
            is ClothesMainModel -> {
                when(view.id){
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
            R.id.text_view_toolbar_back -> {
                findNavController().navigateUp()
            }
            R.id.text_view_toolbar_right_text -> {
                displayMessage("Успешно добавлено")
                val intent = Intent(currentActivity,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                currentActivity.startActivity(intent)
                (currentActivity.finish())
                
            }
        }
    }
    
    private fun showBottomSheet(){
        BottomSheetBehavior.from(bottom_sheet_clothes).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
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
            bundle.putParcelableArrayList("clothes",selectedList)
        }
        createCollecationDialog.arguments = bundle
        createCollecationDialog.show(childFragmentManager, "PhotoChossoserTag")
    }

}