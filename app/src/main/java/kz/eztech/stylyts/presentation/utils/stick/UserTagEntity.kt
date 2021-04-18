package kz.eztech.stylyts.presentation.utils.stick

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.Nullable
import kz.eztech.stylyts.domain.models.user.UserModel


/**
 * Created by Ruslan Erdenoff on 13.01.2021.
 */
class UserTagEntity(
    layer: Layer,
    bitmap: Bitmap,
    item: UserModel,
    @androidx.annotation.IntRange(from = 1) canvasWidth: Int,
    @androidx.annotation.IntRange(from = 1) canvasHeight: Int
) : UserMotionEntity(layer, item, canvasWidth, canvasHeight) {

    private val bitmap: Bitmap

    override fun drawContent(
        canvas: Canvas,
        @Nullable drawingPaint: Paint?
    ) {
        canvas.drawBitmap(bitmap, matrix, drawingPaint)
    }

    override val width: Int
        get() = bitmap.width
    override val height: Int
        get() = bitmap.height

    override fun release() {
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
    }

    fun getScaledWidth(): Float {
        return bitmap.width * layer.scale
    }

    fun getScaledHeight(): Float {
        return bitmap.height * layer.scale
    }

    init {
        this.bitmap = bitmap
        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()
        val widthAspect = 1.0f * canvasWidth / width
        val heightAspect = 1.0f * canvasHeight / height
        // fit the smallest size
        holyScale = Math.min(widthAspect, heightAspect)

        // initial position of the entity
        srcPoints[0] = 0f
        srcPoints[1] = 0f
        srcPoints[2] = width
        srcPoints[3] = 0f
        srcPoints[4] = width
        srcPoints[5] = height
        srcPoints[6] = 0f
        srcPoints[7] = height
        srcPoints[8] = 0f
        srcPoints[8] = 0f

        customPoints[0] = width
        customPoints[1] = (height / 2.0).toFloat()
    }
}