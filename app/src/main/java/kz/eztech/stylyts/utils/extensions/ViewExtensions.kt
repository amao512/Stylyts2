package kz.eztech.stylyts.utils.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kz.eztech.stylyts.R
import java.util.*

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

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

fun displayToast(
    context: Context?,
    msg: String
) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun String.capitalizeWord(): String {
    return first().toUpperCase() + substring(1).toLowerCase(Locale.getDefault())
}