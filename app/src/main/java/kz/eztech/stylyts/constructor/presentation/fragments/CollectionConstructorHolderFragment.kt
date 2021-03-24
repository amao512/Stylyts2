package kz.eztech.stylyts.constructor.presentation.fragments

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_constructor_holder.*
import kotlinx.android.synthetic.main.fragment_profile.include_toolbar_profile
import kz.eztech.stylyts.R
import kz.eztech.stylyts.constructor.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.constructor.presentation.adapters.CollectionConstructorPagerAdapter
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.constructor.presentation.contracts.ConstructorHolderContract

class CollectionConstructorHolderFragment : BaseFragment<MainActivity>(), ConstructorHolderContract.View {
    private val inputClotheList = ArrayList<ClothesTypeDataModel>()
    private var currentMainId = -1
    private lateinit var pagerAdapter: CollectionConstructorPagerAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_collection_constructor_holder
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_profile){
            toolbar_left_corner_action_image_button.visibility = android.view.View.GONE
            toolbar_back_text_view.visibility = android.view.View.VISIBLE
            toolbar_title_text_view.visibility = android.view.View.VISIBLE
            toolbar_right_corner_action_image_button.visibility = android.view.View.GONE
            elevation = 0f
            customizeActionToolBar(this, "Создать образ")
        }
    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {
        arguments?.let {
            if(it.containsKey("items")){
                it.getParcelableArrayList<ClothesTypeDataModel>("items")?.let { it1 ->
                    inputClotheList.addAll(it1)
                }
            }

            if(it.containsKey("mainId")){
                currentMainId = it.getInt("mainId")
            }
        }
    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        val bundle = Bundle()
        bundle.putParcelableArrayList("items",inputClotheList)
        bundle.putInt("mainId",currentMainId)
        pagerAdapter = CollectionConstructorPagerAdapter(this,bundle)


        view_pager_fragment_collection_constructor_holder.isUserInputEnabled = false
        view_pager_fragment_collection_constructor_holder.isSaveEnabled = false
    }

    override fun initializeListeners() {

    }

    override fun onResume() {
        super.onResume()
        view_pager_fragment_collection_constructor_holder.adapter = pagerAdapter
        TabLayoutMediator(tab_bar_fragment_collection_constructor_holder, view_pager_fragment_collection_constructor_holder) { tab, position ->
            when(position){
                0 -> {tab.text = "Для него"}
                1 -> {tab.text = "Для нее"}
            }
        }.attach()
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