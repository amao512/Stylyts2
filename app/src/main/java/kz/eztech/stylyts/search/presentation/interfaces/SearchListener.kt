package kz.eztech.stylyts.search.presentation.interfaces

interface SearchListener {

    fun onQuery(query: (String) -> Unit)
}