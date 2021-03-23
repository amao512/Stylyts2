package kz.eztech.stylyts.search.presentation.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SearchViewModel @Inject constructor() : ViewModel() {

    val queryLiveData: MutableLiveData<String> = MutableLiveData()

    fun onSearch(query: String) {
        queryLiveData.postValue(query)

        Log.d("TAG", "viewModel = $query")
    }
}