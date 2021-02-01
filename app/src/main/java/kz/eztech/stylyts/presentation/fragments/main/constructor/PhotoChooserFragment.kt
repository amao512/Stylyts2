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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kotlinx.android.synthetic.main.fragment_photo_chooser.*
import kotlinx.android.synthetic.main.fragment_profile_income_detail.*
import kotlinx.android.synthetic.main.fragment_profile_income_detail.include_toolbar_income_detail
import kotlinx.android.synthetic.main.item_collection_image.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageItemFilteredAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.collections.PhotoChooserContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.constructor.PhotoChooserPresenter
import kz.eztech.stylyts.presentation.utils.FileUtils
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class PhotoChooserFragment : BaseFragment<MainActivity>(),PhotoChooserContract.View,UniversalViewClickListener {
    private var photoUri:Uri? = null
    
    private lateinit var filterAdapter: CollectionsFilterAdapter
    private lateinit var filterList:ArrayList<CollectionFilterModel>
    private lateinit var genderCategoryList: List<GenderCategory>
    
    private lateinit var filteredAdapter:GridImageItemFilteredAdapter
    
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
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            image_button_right_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_right_text.visibility = android.view.View.VISIBLE
            text_view_toolbar_right_text.text = "Далее"
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
        
        recycler_view_fragment_photo_chooser_filter_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_fragment_photo_chooser_filter_list.adapter = filterAdapter
       
        
        recycler_view_fragment_photo_chooser.layoutManager = GridLayoutManager(context,2)
        recycler_view_fragment_photo_chooser.adapter = filteredAdapter
        
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
        when(view.id){
            R.id.frame_layout_item_collection_filter -> {
                item as CollectionFilterModel
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

}