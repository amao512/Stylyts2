package kz.eztech.stylyts.global.presentation.userSearch.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_user_search.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.UserSearchAdapter
import kz.eztech.stylyts.global.presentation.base.DialogChooserListener
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.global.presentation.userSearch.contracts.UserSearchContract
import kz.eztech.stylyts.global.presentation.userSearch.presenters.UserSearchPresenter
import kz.eztech.stylyts.utils.Paginator
import kz.eztech.stylyts.utils.extensions.displayToast
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class UserSearchDialog(
    private val chooserListener: DialogChooserListener? = null
) : DialogFragment(), DialogChooserListener, UserSearchContract.View,
    SearchView.OnQueryTextListener, UniversalViewClickListener {

    @Inject lateinit var presenter: UserSearchPresenter
    private lateinit var usersAdapter: UserSearchAdapter
    private lateinit var filterModel: SearchFilterModel

    companion object {
        private const val TOKEN_KEY = "token_key"

        fun getNewInstance(chooserListener: DialogChooserListener? = null): UserSearchDialog {
            return UserSearchDialog(chooserListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_user_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeDependency()
        initializePresenter()
        initializeViewsData()
        initializeViews()
        initializeListeners()
        processPostInitialization()
    }

    override fun onChoice(v: View?, item: Any?) {}

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (activity?.application as StylytsApp).applicationComponent.inject(dialog = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        usersAdapter = UserSearchAdapter()
        usersAdapter.setOnClickListener(this)

        filterModel = SearchFilterModel()
    }

    override fun initializeViews() {
        recycler_view_dialog_user_search_list.adapter = usersAdapter

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        search_view_dialog_user_search.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        search_view_dialog_user_search.setOnQueryTextListener(this)
    }

    override fun initializeListeners() {
        text_view_dialog_user_search_cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (view.id) {
            R.id.linear_layout_item_user_info_container -> {
                chooserListener?.onChoice(view, item)
                dismiss()
            }
        }
    }

    override fun processPostInitialization() {
        presenter.getUsers()
        handleRecyclerView()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        view?.let {
            displayToast(it.context, msg)
        }
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onQueryTextSubmit(query: String): Boolean {
        filterModel.query = query
        usersAdapter.clearList()
        presenter.getUsers()

        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        filterModel.query = newText
        usersAdapter.clearList()
        presenter.getUsers()

        return false
    }

    override fun getSearchFilter(): SearchFilterModel = filterModel

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processUserResults(state.data)
            is Paginator.State.NewPageProgress<*> -> processUserResults(state.data)
            else -> {}
        }

        hideProgress()
    }

    override fun processUserResults(list: List<Any?>) {
        list.map { it!! }.let {
            usersAdapter.updateList(list = it)
        }
    }

    private fun handleRecyclerView() {
        recycler_view_dialog_user_search_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recycler_view_dialog_user_search_list.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMorePage()
                }
            }
        })
    }
}