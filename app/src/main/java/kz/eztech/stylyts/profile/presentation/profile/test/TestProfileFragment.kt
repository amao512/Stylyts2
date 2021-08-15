package kz.eztech.stylyts.profile.presentation.profile.test

import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.global.presentation.common.contracts.EmptyContract

class TestProfileFragment : BaseFragment<MainActivity>(), EmptyContract.View {

    private val mutableTypeAdapter: MutableTypeAdapter by lazy {
        MutableTypeAdapter().apply {
//            register()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_test_profile

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {}

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {}

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}