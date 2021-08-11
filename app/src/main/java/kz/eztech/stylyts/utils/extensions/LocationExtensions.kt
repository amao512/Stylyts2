package kz.eztech.stylyts.utils.extensions

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng

fun getLocationFromAddress(
    context: Context,
    address: String
): LatLng? {
    val coder = Geocoder(context)

    try {
        val addresses = coder.getFromLocationName(address, 5) ?: return null
        val location = addresses.first()

        return LatLng(location.latitude, location.longitude)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}