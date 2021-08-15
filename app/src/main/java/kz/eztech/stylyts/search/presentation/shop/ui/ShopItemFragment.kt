package kz.eztech.stylyts.search.presentation.shop.ui

import android.view.View
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import kotlinx.android.synthetic.main.fragment_shop_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.search.presentation.shop.ui.adapters.ShopCategoryAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.search.presentation.shop.contracts.ShopItemContract
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.search.presentation.shop.presenters.ShopCategoryPresenter
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class ShopItemFragment(
    private var position: Int
) : BaseFragment<MainActivity>(), ShopItemContract.View,
    UniversalViewClickListener {

    @Inject lateinit var presenter: ShopCategoryPresenter

    private lateinit var adapter: ShopCategoryAdapter
    private var itemClickListener: UniversalViewClickListener? = null

    private lateinit var refreshLayout: TwinklingRefreshLayout

    override fun onResume() {
        super.onResume()

        presenter.getClothesTypes()
        currentActivity.displayBottomNavigationView()
    }

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

    override fun initializeViewsData() {
        adapter = ShopCategoryAdapter(gender = position)
        adapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recycler_view_fragment_shop_item.adapter = adapter

        refreshLayout = swipe_refresh_fragment_shop_item
        refreshLayout.setHeaderView(ProgressLayout(requireContext()))
        refreshLayout.setEnableLoadmore(false)
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)
                refreshLayout?.startRefresh()
                presenter.getClothesTypes()
            }
        })
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
        swipe_refresh_fragment_shop_item.startRefresh()
    }

    override fun hideProgress() {
        recycler_view_fragment_shop_item.show()
        swipe_refresh_fragment_shop_item.finishRefreshing()
    }

    override fun processClothesTypes(clothesTypes: List<ClothesTypeModel>) {
        adapter.updateList(list = clothesTypes)
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        item as ClothesTypeModel

        val data = HashMap<String, Any>()
        data[ShopCategoryListFragment.CLOTHES_TYPE_GENDER] = this.position
        data[ShopCategoryListFragment.CLOTHES_TYPE] = item

        itemClickListener?.onViewClicked(view, position, data)
    }
}