package kz.eztech.stylyts.presentation.base

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
interface BasePresenter<in T> {
    fun disposeRequests()

    fun attach(view: T)
}