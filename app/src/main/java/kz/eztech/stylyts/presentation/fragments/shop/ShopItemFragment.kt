package kz.eztech.stylyts.presentation.fragments.shop

import android.net.Uri
import android.view.View
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import kotlinx.android.synthetic.main.fragment_shop_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.shop.ShopCategoryAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.shop.ShopItemContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.shop.ShopCategoryPresenter
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
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

    override fun getToken(): String = currentActivity.getTokenFromSharedPref()

    override fun processClothesTypes(resultsModel: ResultsModel<ClothesTypeModel>) {
        val preparedTypes: MutableList<ClothesTypeModel> = mutableListOf()
        val shopsIcon = Uri.parse("android.resource://${R::class.java.`package`.name}/${R.drawable.ic_shops}")

        preparedTypes.addAll(resultsModel.results)
        preparedTypes.add(
            ClothesTypeModel(
                id = 0,
                title = getString(R.string.search_item_shops),
                menCoverPhoto = shopsIcon.toString(),
                womenCoverPhoto = shopsIcon.toString(),
            )
        )

        adapter.updateList(list = preparedTypes)
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