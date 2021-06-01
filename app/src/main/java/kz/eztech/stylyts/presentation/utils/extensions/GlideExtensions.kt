package kz.eztech.stylyts.presentation.utils.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import kz.eztech.stylyts.R

fun String.loadImage(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .placeholder(R.drawable.rounded_rectangle_very_light_gray_white_corners_half_transparent)
        .into(target)
}

fun String.loadImageWithCenterCrop(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .centerCrop()
        .placeholder(R.drawable.rounded_rectangle_very_light_gray_white_corners_half_transparent)
        .into(target)
}

fun Uri.loadImage(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .placeholder(R.drawable.rounded_rectangle_very_light_gray_white_corners_half_transparent)
        .into(target)
}

fun Uri.loadImageWithCenterCrop(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .centerCrop()
        .placeholder(R.drawable.rounded_rectangle_very_light_gray_white_corners_half_transparent)
        .into(target)
}

fun Bitmap.loadImage(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .placeholder(R.drawable.rounded_rectangle_very_light_gray_white_corners_half_transparent)
        .into(target)
}

fun Bitmap.loadImageWithCenterCrop(target: ImageView) {
    Glide.with(target.context)
        .load(this)
        .centerCrop()
        .placeholder(R.drawable.rounded_rectangle_very_light_gray_white_corners_half_transparent)
        .into(target)
}