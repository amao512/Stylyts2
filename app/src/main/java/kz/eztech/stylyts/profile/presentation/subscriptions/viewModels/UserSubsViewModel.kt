package kz.eztech.stylyts.profile.presentation.subscriptions.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserSubsViewModel : ViewModel() {

    private val _queryLiveData: MutableLiveData<String> = MutableLiveData()

    val queryLiveData: LiveData<String> = _queryLiveData

    fun onSearch(query: String) {
        _queryLiveData.value = query
    }
}