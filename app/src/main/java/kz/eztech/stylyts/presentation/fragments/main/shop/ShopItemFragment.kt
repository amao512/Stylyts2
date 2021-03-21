package kz.eztech.stylyts.presentation.fragments.main.shop

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_shop_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ShopCategoryAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.main.shop.ShopCategoryPresenter
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ShopItemFragment(
    private var currentType: Int
) : BaseFragment<MainActivity>(), ShopItemContract.View, SwipeRefreshLayout.OnRefreshListener,
    UniversalViewClickListener {

    @Inject
    lateinit var presenter: ShopCategoryPresenter

    private lateinit var adapter: ShopCategoryAdapter
    private var itemClickListener: UniversalViewClickListener? = null

    override fun getLayoutId(): Int = R.layout.fragment_shop_item

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    fun setOnClickListener(itemClickListener: UniversalViewClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

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

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        recycler_view_fragment_shop_item.hide()
        swipe_refresh_fragment_shop_item.isRefreshing = true
    }

    override fun hideProgress() {
        recycler_view_fragment_shop_item.show()
        swipe_refresh_fragment_shop_item.isRefreshing = false
    }

    override fun processShopCategories(shopCategoryModel: ShopCategoryModel) {
        when (currentType) {
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
        val data = HashMap<String, Any>()
        data["currentGender"] = currentType
        data["currentItem"] = item as GenderCategory
        itemClickListener?.let {
            it.onViewClicked(view, position, data)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.getCategory()
        currentActivity.displayBottomNavigationView()
    }
}