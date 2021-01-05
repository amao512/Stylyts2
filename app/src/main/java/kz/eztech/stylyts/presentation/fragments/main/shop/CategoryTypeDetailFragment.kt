package kz.eztech.stylyts.presentation.fragments.main.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.*
import kotlinx.android.synthetic.main.fragment_category_type_detail.include_toolbar_profile
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kotlinx.android.synthetic.main.fragment_collection_item.recycler_view_fragment_collection_item
import kotlinx.android.synthetic.main.fragment_shop_item_list.*
import kotlinx.android.synthetic.main.item_category_type_detail.view.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CategoryTypeDetailAdapter
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.CategoryTypeDetailContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.shop.CategoryTypeDetailFragmentPresenter
import javax.inject.Inject


class CategoryTypeDetailFragment : BaseFragment<MainActivity>(), CategoryTypeDetailContract.View,UniversalViewClickListener{

    private var gender:String = "M"
    private var typeId:Int? = null
    private var title:String? = null
    @Inject
    lateinit var presenter: CategoryTypeDetailFragmentPresenter
    private lateinit var adapter:CategoryTypeDetailAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_category_type_detail
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_profile){
            image_button_left_corner_action.visibility = View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            image_button_right_corner_action.visibility = android.view.View.VISIBLE
            image_button_right_corner_action.setImageResource(kz.eztech.stylyts.R.drawable.ic_shop)
            elevation = 0f
            customizeActionToolBar(this,title?:"Одежда")
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
            if(it.containsKey("gender")){
                gender = it.getString("gender") ?: "M"
            }
            if(it.containsKey("typeId")){
                typeId = it.getInt("typeId")
            }
            if(it.containsKey("title")){
                title = it.getString("title")
            }
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when(view?.id){
            R.id.linear_layout_item_category_type_detail->{
                findNavController().navigate(R.id.action_categoryTypeDetailFragment_to_itemDetailFragment)
            }
        }
    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        currentActivity.displayBottomNavigationView()
        adapter = CategoryTypeDetailAdapter()
        recycler_view_fragment_category_type_detail.layoutManager = GridLayoutManager(context,2)
        recycler_view_fragment_category_type_detail.adapter = adapter
        adapter.itemClickListener = this
    }

    override fun initializeListeners() {

    }

    override fun processPostInitialization() {
        presenter.getShopCategoryTypeDetail(typeId ?: 1,gender)
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
    override fun processTypeDetail(model: CategoryTypeDetailModel) {
        model.clothes?.data?.let {
            adapter.updateList(it)
        }

    }
}