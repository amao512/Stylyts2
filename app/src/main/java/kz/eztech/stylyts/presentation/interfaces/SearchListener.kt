package kz.eztech.stylyts.presentation.interfaces

interface SearchListener {

    fun onQuery(query: (String) -> Unit)
}