package kz.eztech.stylyts.presentation.utils.extensions

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kz.eztech.stylyts.R
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

fun displaySnackBar(
    context: Context,
    view: View,
    msg: String
) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(ContextCompat.getColor(context, R.color.app_dark_blue_gray))
        view.setPadding(0, 0, 0, 0)
    }.show()
}