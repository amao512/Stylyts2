package kz.eztech.stylyts.presentation.fragments.main.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kotlinx.android.synthetic.main.fragment_collection_item.recycler_view_fragment_collection_item
import kotlinx.android.synthetic.main.fragment_shop_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.domain.models.MainImageAdditionalModel
import kz.eztech.stylyts.domain.models.MainImageModel
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.ShopCategoryAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.shop.ShopCategoryPresenter
import javax.inject.Inject

class ShopItemFragment(private var currentType:Int) : BaseFragment<MainActivity>(), ShopItemContract.View,
	SwipeRefreshLayout.OnRefreshListener, UniversalViewClickListener {

	private lateinit var adapter:ShopCategoryAdapter
	private var itemClickListener:UniversalViewClickListener? = null
	@Inject
	lateinit var presenter:ShopCategoryPresenter
	override fun getLayoutId(): Int {
		return R.layout.fragment_shop_item
	}
	
	override fun getContractView(): BaseView {
		return this
	}
	
	override fun customizeActionBar() {
	
	}
	fun setOnClickListener(itemClickListener:UniversalViewClickListener?){
		this.itemClickListener = itemClickListener
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
		adapter = ShopCategoryAdapter()
		recycler_view_fragment_shop_item.layoutManager = LinearLayoutManager(context)
		recycler_view_fragment_shop_item.adapter = adapter
		adapter.itemClickListener = this
	}
	
	
	
	override fun initializeListeners() {
		swipe_refresh_fragment_shop_item.setOnRefreshListener(this)
	}
	
	override fun processPostInitialization() {}

	override fun onRefresh() {
		presenter.getCategory()
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
		recycler_view_fragment_shop_item.visibility = View.GONE
		swipe_refresh_fragment_shop_item.isRefreshing = true
	}

	override fun hideProgress() {
		recycler_view_fragment_shop_item.visibility = View.VISIBLE
		swipe_refresh_fragment_shop_item.isRefreshing = false
	}

	override fun processShopCategories(shopCategoryModel: ShopCategoryModel) {
		when(currentType){
			0 -> {
				shopCategoryModel.menCategory?.let {
					adapter.updateList(it)
				}
			}
			1 -> {
				shopCategoryModel.femaleCategory?.let {
					adapter.updateList(it)
				}
			}
		}
	}

	override fun onViewClicked(view: View, position: Int, item: Any?) {
		val data = HashMap<String,Any>()
		 data["currentGender"] = currentType
		data["currentItem"] = item as GenderCategory
		itemClickListener?.let {
			it.onViewClicked(view,position,data)
		}
	}

	override fun onResume() {
		super.onResume()
		presenter.getCategory()
		currentActivity.displayBottomNavigationView()
	}
}