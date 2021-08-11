package kz.eztech.stylyts.utils.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import kz.eztech.stylyts.R

fun String.loadImage(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .into(target)
}

fun String.loadImageWithCenterCrop(
    target: ImageView,
    withPlaceHolder: Boolean = false
) {
    Glide.with(target.context)
        .load(this)
        .apply {
            centerCrop()

            if (withPlaceHolder) {
                placeholder(R.color.app_gray)
            }

            into(target)
        }
}

fun Uri.loadImage(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .into(target)
}

fun Uri.loadImageWithCenterCrop(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .centerCrop()
        .into(target)
}

fun Bitmap.loadImage(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .into(target)
}

fun Bitmap.loadImageWithCenterCrop(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .centerCrop()
        .into(target)
}