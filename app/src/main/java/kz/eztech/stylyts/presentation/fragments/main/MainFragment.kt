package kz.eztech.stylyts.presentation.fragments.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_main_image.view.*
import kotlinx.android.synthetic.main.item_main_image_detail.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.MainImagesAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.MainContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.MainLentaPresenter
import kz.eztech.stylyts.presentation.presenters.main.profile.ProfilePresenter
import javax.inject.Inject

class MainFragment : BaseFragment<MainActivity>(), MainContract.View, View.OnClickListener,UniversalViewClickListener {

    lateinit var dummyList: ArrayList<MainImageModel>
    lateinit var mainAdapter: MainImagesAdapter
    
    @Inject
    lateinit var presenter: MainLentaPresenter
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

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        mainAdapter = MainImagesAdapter()
    }
    

    override fun initializeViews() {
        recycler_view_fragment_main_images_list.layoutManager= LinearLayoutManager(currentActivity)
        recycler_view_fragment_main_images_list.adapter = mainAdapter
        mainAdapter.itemClickListener = this
    }
    
    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }
    
    override fun initializeListeners() {
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when(view?.id){
            R.id.constraint_layout_fragment_item_main_image_profile_container ->{
                val bundle = Bundle()
                bundle.putInt("items",1)
                findNavController().navigate(R.id.action_mainFragment_to_partnerProfileFragment,bundle)
            }
            R.id.button_item_main_image_change_collection -> {
                item as MainResult
                item.clothes?.let {
                    val itemsList = ArrayList<ClothesMainModel>()
                    itemsList.addAll(it)
                    val bundle = Bundle()
                    bundle.putParcelableArrayList("items",itemsList)
                    bundle.putInt("mainId",item.id?:0)
                    findNavController().navigate(R.id.action_mainFragment_to_createCollectionFragment,bundle)
                }?:run{
                    findNavController().navigate(R.id.action_mainFragment_to_createCollectionFragment)
                }
            }
            R.id.frame_layout_item_main_image_holder_container -> {
                item as ClothesMainModel
                val bundle = Bundle()
                bundle.putParcelable("clotheModel",item)
                findNavController().navigate(R.id.action_mainFragment_to_itemDetailFragment,bundle)
            }
            R.id.image_view_item_main_image_imageholder -> {
                item as MainResult
                val bundle = Bundle()
                bundle.putParcelable("model",item)
                findNavController().navigate(R.id.action_mainFragment_to_collectionDetailFragment,bundle)
            }
        }
    }
    
    override fun processCollections(model: MainLentaModel) {
        model.results?.let {
            it.forEach { result ->
                result.clothes_location?.let { locations ->
                        result.clothes?.forEach {  clothesMainModel ->
                            clothesMainModel.clothe_location = locations.find { location ->
                                location.clothes_id == clothesMainModel.id
                            }
                        }
                }
            }
            mainAdapter.updateList(it)
        }
    }
    
    override fun processPostInitialization() {
        presenter.getCollections(currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)?:"")
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
        progress_bar_fragment_main_lenta.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress_bar_fragment_main_lenta.visibility = View.GONE
    }
}