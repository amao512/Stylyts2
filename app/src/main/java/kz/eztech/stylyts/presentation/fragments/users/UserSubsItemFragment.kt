package kz.eztech.stylyts.presentation.fragments.users

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_user_subs_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.UserSubAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.users.UserSubsContract
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.users.UserSubsPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class UserSubsItemFragment : BaseFragment<MainActivity>(), UserSubsContract.View, UniversalViewClickListener {

    @Inject lateinit var presenter: UserSubsPresenter
    private lateinit var adapter: UserSubAdapter

    private var position: Int = 0
    private var userId: Int = 0

    companion object {
        private const val POSITION_ARGS = "position_args_key"
        private const val USER_ID_ARGS = "user_id_args_key"

        fun getNewInstance(
            position: Int,
            userId: Int
        ): UserSubsItemFragment {
            val fragment = UserSubsItemFragment()
            val bundle = Bundle()

            bundle.putInt(POSITION_ARGS, position)
            bundle.putInt(USER_ID_ARGS, userId)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_user_subs_item

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(POSITION_ARGS)) {
                position = it.getInt(POSITION_ARGS)
            }

            if (it.containsKey(USER_ID_ARGS)) {
                userId = it.getInt(USER_ID_ARGS)
            }
        }
    }

    override fun initializeViewsData() {
        adapter = UserSubAdapter()
        adapter.itemClickListener = this
    }

    override fun initializeViews() {
        recycler_view_fragment_user_subs_item.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_user_subs_item.adapter = adapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        when (position) {
            0 -> presenter.getFollowers(
                token = getTokenFromSharedPref(),
                userId = userId
            )
            1 -> presenter.getFollowings(
                token = getTokenFromSharedPref(),
                userId = userId
            )
        }
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processFollowers(resultsModel: ResultsModel<FollowerModel>) {
        adapter.updateList(list = resultsModel.results)
    }

    override fun processFollowings(resultsModel: ResultsModel<FollowerModel>) {
        adapter.updateList(list = resultsModel.results)
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is FollowerModel -> {
                val bundle = Bundle()
                bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.id)

                findNavController().navigate(R.id.action_userSubsFragment_to_profileFragment, bundle)
            }
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}