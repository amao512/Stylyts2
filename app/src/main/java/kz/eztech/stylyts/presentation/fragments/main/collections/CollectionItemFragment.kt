package kz.eztech.stylyts.presentation.fragments.main.collections

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.MainImageAdditionalModel
import kz.eztech.stylyts.domain.models.MainImageModel
import kz.eztech.stylyts.domain.models.MainLentaModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageCollectionItemAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.collections.CollectionItemContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.CollectionsItemPresenter
import kz.eztech.stylyts.presentation.presenters.main.MainLentaPresenter
import javax.inject.Inject

class CollectionItemFragment(var currentMode:Int) : BaseFragment<MainActivity>(), CollectionItemContract.View,UniversalViewClickListener {

    private lateinit var adapter:GridImageCollectionItemAdapter
    private var itemClickListener:UniversalViewClickListener? = null
    
    @Inject
    lateinit var presenter: CollectionsItemPresenter
    
    fun setOnClickListener(itemClickListener:UniversalViewClickListener?){
        this.itemClickListener = itemClickListener
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_collection_item
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

    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        adapter = GridImageCollectionItemAdapter()
        recycler_view_fragment_collection_item.layoutManager = GridLayoutManager(context,2)
        recycler_view_fragment_collection_item.adapter = adapter
        
        adapter.setOnClickListener(this)
        val map = HashMap<String,Any>()
        when(currentMode){
            0 -> {
                map["gender"] = "M"
               
            }
            1 -> {
                map["gender"] = "F"
            }
        }
        presenter.getCollections(currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY) ?: "",
                map)
    }
    
    override fun onViewClicked(view: View, position: Int, item: Any?) {
        itemClickListener?.let {
            it.onViewClicked(view,position,item)
        }
    }
    
    override fun initializeListeners() {

    }

    override fun processPostInitialization() {

    }
    
    override fun processCollections(model: MainLentaModel) {
        model.results?.let {
            adapter.updateList(it)
        }
    }
    
    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {

    }

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }
    override fun displayProgress() {

    }

    override fun hideProgress() {

    }
}