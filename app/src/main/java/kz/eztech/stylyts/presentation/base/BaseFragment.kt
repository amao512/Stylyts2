package kz.eztech.stylyts.presentation.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.base_toolbar.*
import kz.eztech.stylyts.R


/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
abstract class BaseFragment<T : BaseActivity>: Fragment(){
    val currentActivity: T
        get() = activity as T

    val currentView: BaseView
        get() = getContractView()

    var hasInitializedRootView = false

    private var rootView: View? = null

    fun displayToast(msg: String){
        rootView?.let {
            Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).apply {
                setBackgroundTint(getColor(context, R.color.app_dark_blue_gray))
                view.setPadding(0, 0, 0, 0)
            }.show()
        }

    }

    fun getPersistentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false)
        } else {
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentView.initializeDependency()
        currentView.initializePresenter()
        return getPersistentView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            currentView.initializeViewsData()
            currentView.initializeArguments()
            currentView.customizeActionBar()
            currentView.initializeViews()
            currentView.initializeListeners()
            currentView.processPostInitialization()
        }
    }

    override fun onStop() {
        super.onStop()
        currentView.disposeRequests()
    }

    fun hideSoftWareKeyboard() {
        val imm =
            currentActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentActivity.currentFocus
        if (view == null) {
            view = View(currentActivity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    abstract fun getLayoutId():Int

    abstract fun getContractView():BaseView

    fun customizeActionToolBar(toolbar: View? = null, title: String? = null){
        toolbar?.let {
            it.apply {
                title?.let { text ->
                    toolbar_title_text_view.text = text
                }
                toolbar_back_text_view.setOnClickListener{
                    findNavController().navigateUp()
                }
            }
        }
    }

}