package kz.eztech.stylyts.presentation.fragments.main.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_item_detail.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ItemDetailContract

class ItemDetailFragment : BaseFragment<MainActivity>(),ItemDetailContract.View,View.OnClickListener {
	override fun getLayoutId(): Int {
		return R.layout.fragment_item_detail
	}
	
	override fun getContractView(): BaseView {
		return this
	}
	
	override fun customizeActionBar() {
		with(include_toolbar_item_detail){
			image_button_left_corner_action.visibility = android.view.View.GONE
			text_view_toolbar_back.visibility = android.view.View.VISIBLE
			text_view_toolbar_title.visibility = android.view.View.VISIBLE
			image_button_right_corner_action.visibility = android.view.View.VISIBLE
			image_button_right_corner_action.setImageResource(kz.eztech.stylyts.R.drawable.ic_shop)
			elevation = 0f
			customizeActionToolBar(this,"zara")
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
		currentActivity.displayBottomNavigationView()
		val imageArray = ArrayList<String>()
		
		imageArray.add("image")
		imageArray.add("image")
		imageArray.add("image")
		imageArray.add("image")
		imageArray.add("image")
		
		val imageAdapter = ImagesViewPagerAdapter(imageArray)
		view_pager_fragment_item_detail_photos_holder.adapter = imageAdapter
		page_indicator_fragment_item_detail_pager_selector.visibility = View.VISIBLE
		page_indicator_fragment_item_detail_pager_selector.attachToPager(view_pager_fragment_item_detail_photos_holder)
	}
	
	override fun initializeListeners() {
		button_fragment_item_detail_create_collection.setOnClickListener(this)
	}

	override fun onClick(v: View?) {
		when(v?.id){
			R.id.button_fragment_item_detail_create_collection -> {
				findNavController().navigate(R.id.action_itemDetailFragment_to_createCollectionFragment)
			}
		}
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