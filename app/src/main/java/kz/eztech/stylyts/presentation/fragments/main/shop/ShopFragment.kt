package kz.eztech.stylyts.presentation.fragments.main.shop

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kotlinx.android.synthetic.main.fragment_collections.include_toolbar
import kotlinx.android.synthetic.main.fragment_collections.view_pager_fragment_collections
import kotlinx.android.synthetic.main.fragment_shop.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionsViewPagerAdapter
import kz.eztech.stylyts.presentation.adapters.ShopViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

class ShopFragment : BaseFragment<MainActivity>(), ShopContract.View,UniversalViewClickListener {

    private lateinit var pagerAdapter:ShopViewPagerAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_shop
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {
        with(include_toolbar){
            image_button_left_corner_action.visibility = View.GONE
            text_view_toolbar_back.visibility = View.GONE
            text_view_toolbar_title.visibility = View.GONE
            image_button_right_corner_action.visibility = View.VISIBLE
            image_button_right_corner_action.setImageResource(R.drawable.ic_shop)
            image_button_right_corner_action.setOnClickListener {
                val cartDialog = CartDialog()
                cartDialog.show(childFragmentManager,"Cart")
            }
            elevation = 0f
            customizeActionToolBar(this,getString(R.string.fragment_registration_appbar_title))
        }
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
        pagerAdapter = ShopViewPagerAdapter(this,this)
        view_pager_fragment_shop.isSaveEnabled = false
    }
    
    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
        view_pager_fragment_shop.adapter = pagerAdapter
        TabLayoutMediator(tab_bar_fragment_shop, view_pager_fragment_shop) { tab, position ->
            when(position){
                0 -> {tab.text = "?????? ????????"}
                1 -> {tab.text = "?????? ??????"}
            }
        }.attach()
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

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        with(item as HashMap<String,Any>){
            val currentItem = this["currentItem"] as GenderCategory
            val currentGender = this["currentGender"] as Int

            val bundle = Bundle()
            bundle.putInt("currentGender",currentGender)
            bundle.putParcelable("currentItem",currentItem)
            findNavController().navigate(R.id.action_shopFragment_to_shopItemListFragment,bundle)
        }

    }
}