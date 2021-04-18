package kz.eztech.stylyts.presentation.dialogs.collection_constructor

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_user_search.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.adapters.collection_constructor.UserSearchAdapter
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.profile.UserSearchContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.search.UserSearchPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.displayToast
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

    private var currentQuery: String = EMPTY_STRING

    companion object {
        private const val TOKEN_KEY = "token_key"

        fun getNewInstance(
            token: String,
            chooserListener: DialogChooserListener? = null
        ): UserSearchDialog {
            val dialog = UserSearchDialog(chooserListener)
            val bundle = Bundle()

            bundle.putString(TOKEN_KEY, token)
            dialog.arguments = bundle

            return dialog
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
        presenter.getUserByUsername(
            token = getTokenFromArgs(),
            username = currentQuery
        )
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
        currentQuery = query
        presenter.getUserByUsername(
            token = getTokenFromArgs(),
            username = currentQuery
        )

        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        currentQuery = newText

        if (newText.length % 2 == 0 && newText.isNotEmpty()) {
            presenter.getUserByUsername(
                token = getTokenFromArgs(),
                username = currentQuery
            )
        } else if (newText.isEmpty()) {
            presenter.getUserByUsername(
                token = getTokenFromArgs(),
                username = currentQuery
            )
        }

        presenter.getUserByUsername(
            token = getTokenFromArgs(),
            username = currentQuery
        )

        return false
    }

    override fun processUserResults(resultsModel: ResultsModel<UserModel>) {
        usersAdapter.updateList(list = resultsModel.results)
    }

    private fun getTokenFromArgs(): String = arguments?.getString(TOKEN_KEY) ?: EMPTY_STRING
}