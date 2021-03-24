package kz.eztech.stylyts.common.presentation.utils.extensions

import android.view.View
import java.util.Locale

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun getShortName(
    firstName: String?,
    lastName: String?
): String = "${firstName?.toUpperCase(Locale.getDefault())?.get(0)}${lastName?.toUpperCase(Locale.getDefault())?.get(0)}"