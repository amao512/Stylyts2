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
import kz.eztech.stylyts.domain.models.MainImageAdditionalModel
import kz.eztech.stylyts.domain.models.MainImageModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.collections.CollectionItemContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

class CollectionItemFragment : BaseFragment<MainActivity>(), CollectionItemContract.View,UniversalViewClickListener {

    private lateinit var adapter:GridImageAdapter
    private var itemClickListener:UniversalViewClickListener? = null
    
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

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {

    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        currentActivity.displayBottomNavigationView()
        val dummyList = ArrayList<MainImageModel>()
        for(i in 0..5){
            val listAdditional = ArrayList<MainImageAdditionalModel>()
            for (i in 0..5){
                listAdditional.add(MainImageAdditionalModel(name = "Hello"))
            }
            dummyList.add(MainImageModel(name = "zara ${dummyList.count()}",additionals = listAdditional))
        }
        adapter = GridImageAdapter()
        recycler_view_fragment_collection_item.layoutManager = GridLayoutManager(context,2)
        recycler_view_fragment_collection_item.adapter = adapter
        adapter.updateList(dummyList)
        adapter.setOnClickListener(this)
    }
    
    override fun onViewClicked(view: View, position: Int, item: Any?) {
        Log.wtf("GridImageViewHolder","onViewClicked")
        itemClickListener?.let {
            it.onViewClicked(view,position,item)
        }
    }
    
    override fun initializeListeners() {

    }

    override fun processPostInitialization() {

    }

    override fun disposeRequests() {

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