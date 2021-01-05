package kz.eztech.stylyts.presentation.fragments.main

import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.MainImageAdditionalModel
import kz.eztech.stylyts.domain.models.MainImageModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.MainImagesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

class MainFragment : BaseFragment<MainActivity>(), MainContract.View, View.OnClickListener,UniversalViewClickListener {

    lateinit var dummyList: ArrayList<MainImageModel>
    lateinit var mainAdapter: MainImagesAdapter
    override fun customizeActionBar() {
        with(include_toolbar){
            image_button_left_corner_action.visibility = View.GONE
            text_view_toolbar_back.visibility = View.GONE
            text_view_toolbar_title.visibility = View.GONE
            image_button_right_corner_action.visibility = View.VISIBLE
            image_button_right_corner_action.setImageResource(R.drawable.ic_send_message)
            elevation = 0f
            customizeActionToolBar(this)
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        dummyList = ArrayList()
        addItems()
        mainAdapter = MainImagesAdapter()
    }

    private fun addItems(){
        for(i in 0..5){
            val listAdditional = ArrayList<MainImageAdditionalModel>()
            for (i in 0..5){
                listAdditional.add(MainImageAdditionalModel(name = "Hello"))
            }
            dummyList.add(MainImageModel(name = "zara ${dummyList.count()}",additionals = listAdditional))
        }
    }

    override fun initializeViews() {
        currentActivity.displayBottomNavigationView()
        recycler_view_fragment_main_images_list.layoutManager= LinearLayoutManager(currentActivity)
        recycler_view_fragment_main_images_list.adapter = mainAdapter
        mainAdapter.itemClickListener = this
    }

    override fun initializeListeners() {
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        Log.wtf("onViewClicked","Clicked here")
        when(view?.id){
            R.id.constraint_layout_fragment_item_main_image_profile_container ->{
                findNavController().navigate(R.id.action_mainFragment_to_partnerProfileFragment)
            }
            R.id.button_item_main_image_change_collection -> {
                findNavController().navigate(R.id.action_mainFragment_to_createCollectionFragment)
            }
        }
    }

    override fun processPostInitialization() {
        mainAdapter.updateList(dummyList)
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun onClick(v: View?) {
    
    }
    override fun displayProgress() {

    }

    override fun hideProgress() {

    }
}