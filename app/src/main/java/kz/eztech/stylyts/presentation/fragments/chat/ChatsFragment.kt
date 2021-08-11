package kz.eztech.stylyts.presentation.fragments.chat

import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_chats.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.utils.extensions.show

class ChatsFragment : BaseFragment<MainActivity>(), EmptyContract.View {

    override fun getLayoutId(): Int = R.layout.fragment_chats

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_chats_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.chats_title))

            background = ContextCompat.getDrawable(requireContext(), R.color.toolbar_bg_gray)
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        currentActivity.hideBottomNavigationView()
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}