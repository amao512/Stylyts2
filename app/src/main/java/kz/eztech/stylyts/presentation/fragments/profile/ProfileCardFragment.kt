package kz.eztech.stylyts.presentation.fragments.profile

import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_profile_card.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.profile.ProfileCardContract
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class ProfileCardFragment : BaseFragment<MainActivity>(), ProfileCardContract.View {

    override fun getLayoutId(): Int = R.layout.fragment_profile_card

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_profile_card) {
            toolbar_back_text_view.show()
            toolbar_title_text_view.show()
            toolbar_right_corner_action_image_button.hide()

            customizeActionToolBar(
                toolbar = this,
                title = context.getString(R.string.card_fragment_add_way_payment)
            )
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {}

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}