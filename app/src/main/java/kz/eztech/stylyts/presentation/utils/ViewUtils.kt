package kz.eztech.stylyts.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View

/**
 * Created by Ruslan Erdenoff on 21.01.2021.
 */
object ViewUtils {

    fun createBitmapScreenshot(v: View): Bitmap {
        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.draw(c)

        return b
    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density

        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}