package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.getColor
import kotlinx.android.synthetic.main.fragment_collections.*
import kotlinx.android.synthetic.main.fragment_collections.view_pager_fragment_collections
import kotlinx.android.synthetic.main.fragment_constructor_holder.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.CollectionsViewPagerAdapter
import kz.eztech.stylyts.presentation.adapters.ConstructorPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.constructor.ConstructorHolderContract

class ConstructorHolderFragment : BaseFragment<MainActivity>(), ConstructorHolderContract.View , MotionLayout.TransitionListener{

    private var isCompleted = false
    private var startId = 0
    private var endId = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_constructor_holder
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {}

    override fun initializeDependency() {

    }

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {
        currentActivity.hideBottomNavigationView()
        val pagerAdapter = ConstructorPagerAdapter(this)
        view_pager_fragment_constructor_holder.adapter = pagerAdapter
        view_pager_fragment_constructor_holder.isUserInputEnabled = false
    }

    override fun initializeListeners() {
        motion_layout_fragment_constructor_holder_chooser.setTransitionListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        startId = p1
        endId = p2
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        if(p1 == endId)isCompleted = true else false
        if(isCompleted){
            when(view_pager_fragment_constructor_holder.currentItem){
                0 -> {
                    view_pager_fragment_constructor_holder.setCurrentItem(1,true)
                    text_view_fragment_constructor_holder_create_collection.setTextColor(getColor(currentActivity,R.color.app_gray))
                    text_view_fragment_constructor_holder_create_post.setTextColor(getColor(currentActivity,R.color.white))
                }
                1 -> {
                    view_pager_fragment_constructor_holder.setCurrentItem(0,true)
                    text_view_fragment_constructor_holder_create_collection.setTextColor(getColor(currentActivity,R.color.white))
                    text_view_fragment_constructor_holder_create_post.setTextColor(getColor(currentActivity,R.color.app_gray))
                }
            }
        }

    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
}