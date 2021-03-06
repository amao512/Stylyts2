package kz.eztech.stylyts.presentation.fragments.main.collections

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collections.*
import kotlinx.android.synthetic.main.fragment_collections.include_toolbar
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.CollectionFilterModel
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionsFilterAdapter
import kz.eztech.stylyts.presentation.adapters.CollectionsViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.collections.CollectionsContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

class CollectionsFragment : BaseFragment<MainActivity>(), CollectionsContract.View,UniversalViewClickListener {
	private lateinit var filterAdapter:CollectionsFilterAdapter
	private lateinit var filterList:ArrayList<CollectionFilterModel>
	private lateinit var pagerAdapter:CollectionsViewPagerAdapter
	override fun getLayoutId(): Int {
		return R.layout.fragment_collections
	}

	override fun getContractView(): BaseView {
		return this
	}

	override fun customizeActionBar() {
		with(include_toolbar){
			image_button_left_corner_action.visibility = View.VISIBLE
			image_button_left_corner_action.setImageResource(R.drawable.ic_camera)
			text_view_toolbar_back.visibility = View.GONE
			text_view_toolbar_title.visibility = View.GONE
			image_button_right_corner_action.visibility = View.VISIBLE
			image_button_right_corner_action.setImageResource(R.drawable.ic_create_collection)
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
		filterList = ArrayList()
		filterList.add(CollectionFilterModel("????????????????"))
		filterList.add(CollectionFilterModel("??????????????"))
		filterList.add(CollectionFilterModel("Casual"))
		filterList.add(CollectionFilterModel("????????????????????"))
		filterList.add(CollectionFilterModel("????????????????????????"))
		filterList.add(CollectionFilterModel("????????????????"))
		filterList.add(CollectionFilterModel("??????????????"))
		filterList.add(CollectionFilterModel("Casual"))
		filterList.add(CollectionFilterModel("????????????????????"))
		filterList.add(CollectionFilterModel("????????????????????????"))
	}

	override fun initializeViews() {
		pagerAdapter = CollectionsViewPagerAdapter(this,this)
		view_pager_fragment_collections.isSaveEnabled = false
		filterAdapter = CollectionsFilterAdapter()
		recycler_view_fragment_collections_filter_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
		recycler_view_fragment_collections_filter_list.adapter = filterAdapter
		filterAdapter.updateList(filterList)
	}
	
	override fun onResume() {
		super.onResume()
		currentActivity.displayBottomNavigationView()
		view_pager_fragment_collections.adapter = pagerAdapter
		TabLayoutMediator(tab_bar_fragment_collections, view_pager_fragment_collections) { tab, position ->
			when(position){
				0 -> {tab.text = "?????? ??????"}
				1 -> {tab.text = "?????? ????????"}
			}
		}.attach()
	}
	
	override fun onViewClicked(view: View, position: Int, item: Any?) {
		when(item){
			is MainResult -> {
				val bundle = Bundle()
				bundle.putParcelable("model",item)
				findNavController().navigate(R.id.action_collectionsFragment_to_collectionDetailFragment,bundle)
			}
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