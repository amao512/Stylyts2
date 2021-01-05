package kz.eztech.stylyts.presentation.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.CreateCollectionContract

class CreateCollectionFragment : BaseFragment<MainActivity>(),CreateCollectionContract.View {
    override fun getLayoutId(): Int {
        return R.layout.fragment_create_collection
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {

    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {

    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {

    }

    override fun initializeListeners() {

    }

    override fun processPostInitialization() {

    }

    override fun disposeRequests() {

    }

    override fun displayMessage(msg: String) {

    }

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }
    override fun displayProgress() {

    }

    override fun hideProgress() {

    }
}