package kz.eztech.stylyts.presentation.base

import android.util.Log

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
public interface BaseView{
    fun customizeActionBar()
    fun initializeDependency()
    fun initializePresenter()
    fun initializeArguments()
    fun initializeViewsData()
    fun initializeViews()
    fun initializeListeners()
    fun processPostInitialization()
    fun disposeRequests()
    fun displayMessage(msg: String)
    fun isFragmentVisible():Boolean
    fun displayProgress()
    fun hideProgress()
}

inline fun <V:BaseView> V.processViewAction(action: V.() -> Unit) {
    if(this.isFragmentVisible()){
        action()
    }else{
        Log.wtf("FragmentError","No such fragment")
    }
}