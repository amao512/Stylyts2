package kz.eztech.stylyts.presentation.fragments.users

import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_user_subs.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.UserSubViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.users.UserSubsContract
import kz.eztech.stylyts.presentation.presenters.users.UserSubsPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class UserSubsFragment : BaseFragment<MainActivity>(), UserSubsContract.View, View.OnClickListener {

    @Inject lateinit var presenter: UserSubsPresenter
    private lateinit var adapter: UserSubViewPagerAdapter

    private var userId: Int = 0
    private var username: String = EMPTY_STRING
    private var followersCount: Int = 0
    private var followingsCount: Int = 0
    private var position = FOLLOWERS_POSITION

    companion object {
        const val USER_ID_ARGS = "user_id_args_key"
        const val USERNAME_ARGS = "username_args_key"
        const val POSITION_ARGS = "position_args_key"
        const val FOLLOWERS_POSITION = 0
        const val FOLLOWINGS_POSITION = 1
    }

    override fun onResume() {
        super.onResume()

        currentActivity.displayBottomNavigationView()

        initializeTabLayout()
    }

    override fun getLayoutId(): Int = R.layout.fragment_user_subs

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_user_subs) {
            toolbar_right_corner_action_image_button.hide()
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener(this@UserSubsFragment)

            toolbar_title_text_view.text = username
            toolbar_title_text_view.show()
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(USER_ID_ARGS)) {
                userId = it.getInt(USER_ID_ARGS)
            }

            if (it.containsKey(USERNAME_ARGS)) {
                username = it.getString(USERNAME_ARGS) ?: EMPTY_STRING
            }

            if (it.containsKey(POSITION_ARGS)) {
                position = it.getInt(POSITION_ARGS)
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        adapter = UserSubViewPagerAdapter(fa = this, userId = userId)
        view_pager_fragment_user_subs.isSaveEnabled = false
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {
        presenter.getFollowers(
            token = getTokenFromSharedPref(),
            userId = userId
        )
        presenter.getFollowings(
            token = getTokenFromSharedPref(),
            userId = userId
        )
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processFollowers(resultsModel: ResultsModel<FollowerModel>) {
        followersCount = resultsModel.totalCount
        initializeTabLayout()
    }

    override fun processFollowings(resultsModel: ResultsModel<FollowerModel>) {
        followingsCount = resultsModel.totalCount
        initializeTabLayout()
    }

    override fun processSuccessFollowing(followSuccessModel: FollowSuccessModel) {}

    override fun processSuccessUnFollowing() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
        }
    }

    private fun initializeTabLayout() {
        view_pager_fragment_user_subs.adapter = adapter
        view_pager_fragment_user_subs.currentItem = position

        TabLayoutMediator(
            tab_bar_fragment_user_subs,
            view_pager_fragment_user_subs
        ) { tab, position ->
            tab.text = when (position) {
                FOLLOWERS_POSITION -> getString(R.string.followers_text_format, followersCount.toString())
                FOLLOWINGS_POSITION -> getString(R.string.followings_text_format, followingsCount.toString())
                else -> EMPTY_STRING
            }
        }.attach()
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}