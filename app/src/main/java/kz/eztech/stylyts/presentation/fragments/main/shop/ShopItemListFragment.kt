package kz.eztech.stylyts.presentation.fragments.main.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_shop_item_list.*
import kotlinx.android.synthetic.main.fragment_shop_item_list.include_toolbar_profile
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesTypes
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ShopItemListAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemListContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

class ShopItemListFragment : BaseFragment<MainActivity>(), ShopItemListContract.View,UniversalViewClickListener {
    private lateinit var adapter: ShopItemListAdapter

    private var currentGenderCategory: GenderCategory?=null
    private var currentGender: Int?=null
    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_item_list
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
            customizeActionToolBar(this,currentGenderCategory?.title?:"Одежда")
        }
    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey("currentItem")){
                currentGenderCategory = (it.getParcelable("currentItem") ?: null as GenderCategory )
            }
            if(it.containsKey("currentGender")){
                currentGender  = it.getInt("currentGender")
            }
        }
    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        adapter = ShopItemListAdapter()
        recycler_view_fragment_shop_item_list.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_shop_item_list.adapter = adapter
        adapter.itemClickListener = this
    }
    
    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }
    
    override fun initializeListeners() {}

    override fun processPostInitialization() {
        currentGenderCategory?.let {
            it.clothes_types?.let { list ->
                adapter.updateList(list)
            }

        }

    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        with(item as ClothesTypes){
            val bundle = Bundle()
            val gender = when(currentGender){
                0 -> "M"
                1 -> "F"
                else -> "M"
            }
            bundle.putString("gender",gender)
            bundle.putInt("typeId",id?:1)
            bundle.putString("title",title)
            findNavController().navigate(R.id.action_shopItemListFragment_to_categoryTypeDetailFragment,bundle)
        }
    }
}