package kz.eztech.stylyts.domain.models.order

import com.google.android.gms.maps.model.LatLng

data class CustomLatLng(
    val id: Int,
    val address: String,
    val latLng: LatLng
)