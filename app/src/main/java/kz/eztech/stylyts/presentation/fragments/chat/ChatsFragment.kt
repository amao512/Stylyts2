package kz.eztech.stylyts.presentation.fragments.chat

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_chats.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract
import kz.eztech.stylyts.presentation.utils.extensions.show

class ChatsFragment : BaseFragment<MainActivity>(), EmptyContract.View, View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_chats

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_chats_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.chats_title)
            toolbar_title_text_view.show()

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

    override fun initializeListeners() {
        toolbar_left_corner_action_image_button.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
        }
    }
}