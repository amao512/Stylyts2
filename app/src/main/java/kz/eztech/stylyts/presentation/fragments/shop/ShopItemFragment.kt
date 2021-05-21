package kz.eztech.stylyts.presentation.fragments.shop

import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_shop_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
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
) : BaseFragment<MainActivity>(), ShopItemContract.View, SwipeRefreshLayout.OnRefreshListener,
    UniversalViewClickListener {

    @Inject lateinit var presenter: ShopCategoryPresenter

    private lateinit var adapter: ShopCategoryAdapter
    private var itemClickListener: UniversalViewClickListener? = null

    override fun onResume() {
        super.onResume()

        presenter.getClothesTypes(token = currentActivity.getTokenFromSharedPref())
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
    }

    override fun initializeListeners() {
        swipe_refresh_fragment_shop_item.setOnRefreshListener(this)
    }

    override fun processPostInitialization() {}

    override fun onRefresh() {
        presenter.getClothesTypes(token = currentActivity.getTokenFromSharedPref())
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
        data[ShopItemListFragment.CLOTHES_TYPE_GENDER] = this.position
        data[ShopItemListFragment.CLOTHES_TYPE] = item

        itemClickListener?.onViewClicked(view, position, data)
    }
}