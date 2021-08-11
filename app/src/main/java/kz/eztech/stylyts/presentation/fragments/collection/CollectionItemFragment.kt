package kz.eztech.stylyts.presentation.fragments.collection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import kotlinx.android.synthetic.main.fragment_collection_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.common.GridImageAdapter
import kz.eztech.stylyts.presentation.adapters.helpers.GridSpacesItemDecoration
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection.CollectionItemContract
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionsItemPresenter
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

class CollectionItemFragment(
    private val currentMode: Int
) : BaseFragment<MainActivity>(),
    CollectionItemContract.View,
    UniversalViewClickListener {

    @Inject
    lateinit var presenter: CollectionsItemPresenter
    private lateinit var adapter: GridImageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: TwinklingRefreshLayout

    private var itemClickListener: UniversalViewClickListener? = null

    fun setOnClickListener(itemClickListener: UniversalViewClickListener?) {
        this.itemClickListener = itemClickListener
    }

    override fun getLayoutId(): Int = R.layout.fragment_collection_item

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        adapter = GridImageAdapter()
        adapter.setOnClickListener(this)
    }

    override fun initializeViews() {
        recyclerView = recycler_view_fragment_collection_item
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(GridSpacesItemDecoration(space = 16))

        refreshLayout = fragment_collection_item_swipe_refresh_layout
        refreshLayout.setHeaderView(ProgressLayout(requireContext()))
        refreshLayout.setBottomView(LoadingView(requireContext()))
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        itemClickListener?.onViewClicked(view, position, item)
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        getCollections()
        handleRefreshLayout()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_collection_item_swipe_refresh_layout.startRefresh()
    }

    override fun hideProgress() {
        fragment_collection_item_swipe_refresh_layout.finishRefreshing()
    }

    override fun getTokenId(): String = currentActivity.getTokenFromSharedPref()

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> {
                processPostResults(state.data)
                refreshLayout.finishRefreshing()
            }
            is Paginator.State.NewPageProgress<*> -> {
                processPostResults(state.data)
                refreshLayout.finishLoadmore()
            }
            else -> {
                refreshLayout.finishRefreshing()
                refreshLayout.finishLoadmore()
            }
        }
    }

    override fun processPostResults(list: List<Any?>) {
        val preparedList: List<PostModel> = preparePosts(postList = list)

        adapter.updateList(list = preparedList)
    }

    private fun handleRefreshLayout() {
        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                super.onRefresh(refreshLayout)
                refreshLayout?.startRefresh()
                getCollections()
            }

            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)
                refreshLayout?.startLoadMore()
                presenter.loadMorePost()
            }
        })
    }

    private fun preparePosts(postList: List<Any?>): List<PostModel> {
        val preparedList: MutableList<PostModel> = mutableListOf()

        postList.map {
            it as PostModel

            if (it.clothes.isEmpty()) {
                preparedList.add(it)
            } else {
                it.clothes.map { clothes ->
                    if (clothes.gender == getGender() && !preparedList.contains(it)) {
                        preparedList.add(it)
                    }
                }
            }
        }

        return preparedList
    }

    private fun getCollections() {
        adapter.clearList()
        presenter.getPosts()
    }

    private fun getGender(): String {
        return when (currentMode) {
            0 -> GenderEnum.MALE.gender
            else -> GenderEnum.FEMALE.gender
        }
    }
}