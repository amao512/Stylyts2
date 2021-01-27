package kz.eztech.stylyts.presentation.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.base_toolbar.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
abstract class BaseFragment <T:BaseActivity>: Fragment(){
    val currentActivity: T
        get() = activity as T

    val currentView: BaseView
        get() = getContractView()

    lateinit var rootView: View

    fun displayToast(msg:String){
        Snackbar.make(rootView,msg,Snackbar.LENGTH_SHORT).apply {
            setTextColor(getColor(context, R.color.white))
            setBackgroundTint(getColor(context,R.color.app_dark_blue_gray))
        }
        //currentActivity.displayToast(msg)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentView.initializeDependency()
        currentView.initializePresenter()
        rootView =  inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentView.initializeViewsData()
        currentView.initializeArguments()
        currentView.customizeActionBar()
        currentView.initializeViews()
        currentView.initializeListeners()
        currentView.processPostInitialization()
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

    fun customizeActionToolBar(toolbar:View? = null,title:String? = null){
        toolbar?.let {
            it.apply {
                title?.let {text ->
                    text_view_toolbar_title.text = text
                }
                text_view_toolbar_back.setOnClickListener{
                    currentActivity.onBackPressed()
                }
            }
        }
    }

}