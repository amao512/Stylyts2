package kz.eztech.stylyts.presentation.utils.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

fun String.loadImage(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .into(target)
}

fun String.loadImageWithCenterCrop(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .centerCrop()
        .into(target)
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