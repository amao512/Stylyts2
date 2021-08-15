package kz.eztech.stylyts.global.presentation.common.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PagerViewModel : ViewModel() {

    private val _positionLiveData: MutableLiveData<Int> = MutableLiveData()

    val positionLiveData: LiveData<Int> = _positionLiveData

    fun setPosition(position: Int) {
        _positionLiveData.value = position
    }
}