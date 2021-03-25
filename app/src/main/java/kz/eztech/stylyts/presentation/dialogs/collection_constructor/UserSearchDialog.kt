package kz.eztech.stylyts.presentation.dialogs.collection_constructor

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_user_search.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.adapters.collection_constructor.UserSearchAdapter
import kz.eztech.stylyts.presentation.contracts.profile.UserSearchContract

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class UserSearchDialog : DialogFragment(), DialogChooserListener, UserSearchContract.View,
    SearchView.OnQueryTextListener, UniversalViewClickListener {

//    @Inject
//    lateinit var itemPresenter: SearchItemPresenter

    private lateinit var adapter: UserSearchAdapter

    private var currentQuery: String = ""

    internal var listener: DialogChooserListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initializeDependency()
        initializePresenter()
        return inflater.inflate(R.layout.dialog_user_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViewsData()
        initializeArguments()
        customizeActionBar()
        initializeViews()
        initializeListeners()
        processPostInitialization()
    }

    override fun onChoice(v: View?, item: Any?) {}

    override fun processUser(list: List<ProfileModel>) {
        adapter.updateList(list)
    }

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (requireContext().applicationContext as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun onQueryTextSubmit(query: String): Boolean {
        currentQuery = query
//        itemPresenter.getUserByUsername(
//			(activity as MainActivity).getSharedPrefByKey<String>(
//				SharedConstants.TOKEN_KEY
//			) ?: "", currentQuery
//		)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        currentQuery = newText
        /*if(newText.length % 2 == 0 && newText.isNotEmpty()){
            presenter.getUserByUsername((activity as MainActivity).getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)?:"",currentQuery)
        }else if(newText.isEmpty()){
            presenter.getUserByUsername((activity as MainActivity).getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)?:"",currentQuery)
        }*/
//        itemPresenter.getUserByUsername(
//			(activity as MainActivity).getSharedPrefByKey<String>(
//				SharedConstants.TOKEN_KEY
//			) ?: "", currentQuery
//		)
        return false
    }

    override fun initializeViewsData() {
        adapter = UserSearchAdapter()
    }

    override fun initializeViews() {
        recycler_view_dialog_user_search_list.layoutManager = LinearLayoutManager(requireContext())
        recycler_view_dialog_user_search_list.adapter = adapter

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        search_view_dialog_user_search.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        search_view_dialog_user_search.setOnQueryTextListener(this)
        adapter.setOnClickListener(this)
    }

    override fun initializeListeners() {
        text_view_dialog_user_search_cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.linear_layout_item_user_info_container -> {
                listener?.onChoice(view, item)
            }
        }
    }

    override fun processPostInitialization() {
//        itemPresenter.getUserByUsername(
//			(activity as MainActivity).getSharedPrefByKey<String>(
//				SharedConstants.TOKEN_KEY
//			) ?: "", currentQuery
//		)
    }

    override fun disposeRequests() {
//        itemPresenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun getTheme(): Int = R.style.FullScreenDialog

    fun setChoiceListener(listener: DialogChooserListener) {
        this.listener = listener
    }
}