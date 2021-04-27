package kz.eztech.stylyts.presentation.presenters.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopItemViewModel : ViewModel() {

    private val _isOutfits: MutableLiveData<Boolean> = MutableLiveData()

    val isOutfits: LiveData<Boolean> = _isOutfits

    fun setCollectionMode(isOutfits: Boolean) {
        _isOutfits.value = isOutfits
    }
}