package kz.eztech.stylyts.presentation.fragments.users

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_user_subs_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.users.UserSubAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.users.UserSubsContract
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.users.UserSubsPresenter
import kz.eztech.stylyts.presentation.presenters.users.UserSubsViewModel
import kz.eztech.stylyts.utils.EMPTY_STRING
import org.koin.android.ext.android.inject
import javax.inject.Inject

class UserSubsItemFragment : BaseFragment<MainActivity>(), UserSubsContract.View, UniversalViewClickListener {

    private val userSubsViewModel: UserSubsViewModel by inject()

    @Inject lateinit var presenter: UserSubsPresenter
    private lateinit var adapter: UserSubAdapter

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

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        adapter = UserSubAdapter()
        adapter.setOnClickListener(listener = this)
    }

    override fun initializeViews() {
        recycler_view_fragment_user_subs_item.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_user_subs_item.adapter = adapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        getUsers()

        userSubsViewModel.queryLiveData.observe(viewLifecycleOwner, {
            getUsers(username = it)
        })
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

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
        when (view.id) {
            R.id.linear_layout_item_user_info_container -> onNavigateToProfile(item)
            R.id.button_item_user_subs_unfollow -> onUnFollowUser(item)
            R.id.button_item_user_subs_follow -> onFollowUser(item)
        }
    }

    override fun processSuccessFollowing(followSuccessModel: FollowSuccessModel) {
        adapter.setFollowingUser(followerId = followSuccessModel.followee)
    }

    override fun processSuccessUnFollowing(followerId: Int) {
        adapter.setUnFollowingUser(followerId = followerId)
    }

    private fun getUsers(username: String = EMPTY_STRING) {
        when (getPositionFromArgs()) {
            0 -> presenter.getFollowers(
                userId = getUserIdFromArgs(),
                username = username
            )
            1 -> presenter.getFollowings(
                userId = getUserIdFromArgs(),
                username = username
            )
        }
    }

    private fun onNavigateToProfile(item: Any?) {
        item as FollowerModel

        val bundle = Bundle()
        bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, item.id)

        findNavController().navigate(R.id.nav_profile, bundle)
    }

    private fun onFollowUser(item: Any?) {
        item as FollowerModel

        presenter.followUser(followerId = item.id)
    }

    private fun onUnFollowUser(item: Any?) {
        item as FollowerModel

        presenter.unFollowUser(followerId = item.id)
    }

    private fun getPositionFromArgs(): Int = arguments?.getInt(POSITION_ARGS) ?: 0

    private fun getUserIdFromArgs(): Int = arguments?.getInt(USER_ID_ARGS) ?: 0
}