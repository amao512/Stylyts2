package kz.eztech.stylyts.presentation.fragments.collection_constructor

import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat.getColor
import kotlinx.android.synthetic.main.fragment_constructor_holder.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection_constructor.ConstructorPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.collection_constructor.ConstructorHolderContract

class ConstructorHolderFragment : BaseFragment<MainActivity>(), ConstructorHolderContract.View,
    MotionLayout.TransitionListener {

    private lateinit var pagerAdapter: ConstructorPagerAdapter

    private val inputClotheList = ArrayList<ClothesTypeDataModel>()
    private var currentMainId = -1

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

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey("items")) {
                it.getParcelableArrayList<ClothesTypeDataModel>("items")?.let { it1 ->
                    inputClotheList.addAll(it1)
                }
            }

            if (it.containsKey("mainId")) {
                currentMainId = it.getInt("mainId")
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        val bundle = Bundle()
        bundle.putParcelableArrayList("items", inputClotheList)
        bundle.putInt("mainId", currentMainId)
        pagerAdapter = ConstructorPagerAdapter(this, bundle)
        view_pager_fragment_constructor_holder.isUserInputEnabled = false
        view_pager_fragment_constructor_holder.isSaveEnabled = false
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
        view_pager_fragment_constructor_holder.adapter = pagerAdapter
    }

    override fun initializeListeners() {
        motion_layout_fragment_constructor_holder_chooser.setTransitionListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        when (motion_layout_fragment_constructor_holder_chooser.currentState) {
            motion_layout_fragment_constructor_holder_chooser.startState -> {
                view_pager_fragment_constructor_holder.setCurrentItem(0, true)
                text_view_fragment_constructor_holder_create_collection.setTextColor(
                    getColor(
                        currentActivity,
                        R.color.white
                    )
                )
                text_view_fragment_constructor_holder_create_post.setTextColor(
                    getColor(
                        currentActivity,
                        R.color.app_gray
                    )
                )
            }
            motion_layout_fragment_constructor_holder_chooser.endState -> {
                view_pager_fragment_constructor_holder.setCurrentItem(1, true)
                text_view_fragment_constructor_holder_create_collection.setTextColor(
                    getColor(
                        currentActivity,
                        R.color.app_gray
                    )
                )
                text_view_fragment_constructor_holder_create_post.setTextColor(
                    getColor(
                        currentActivity,
                        R.color.white
                    )
                )
            }
        }
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
}