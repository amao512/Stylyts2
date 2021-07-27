package kz.eztech.stylyts.presentation.utils.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.utils.DateFormatterHelper
import org.threeten.bp.ZonedDateTime
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

fun displayToast(
    context: Context?,
    msg: String
) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun getFormattedDate(date: String): String {
    return DateFormatterHelper.formatISO_8601(
        date,
        DateFormatterHelper.FORMAT_DATE_DD_MMMM
    )
}

fun String.getTimeByFormat(): ZonedDateTime = try {
    ZonedDateTime.parse(this) ?: ZonedDateTime.now()
} catch (e: Exception) {
    ZonedDateTime.now()
}

fun String.capitalizeWord(): String {
    return first().toUpperCase() + substring(1).toLowerCase(Locale.getDefault())
}