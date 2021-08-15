package kz.eztech.stylyts.utils.stick

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import androidx.annotation.IntRange
import kz.eztech.stylyts.global.domain.models.motion.MotionItemModel
import kz.eztech.stylyts.utils.stick.MathUtils.pointInTriangle

/**
 * Created by Ruslan Erdenoff on 13.01.2021.
 */
abstract class MotionEntity(
    val layer: Layer,
    val item: MotionItemModel,
    @IntRange(from = 1) var canvasWidth: Int,
    @IntRange(from = 1) var canvasHeight: Int
) {
    protected val matrix = Matrix()
    var isSelected = false
    protected var holyScale = 0f
    private val destPoints = FloatArray(10) // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0
    protected val srcPoints = FloatArray(10) // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0
    protected val customPoints = FloatArray(2) // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0
    private var borderPaint = Paint()
    private var deleteIcon: BitmapStickerIcon? = null

    //private val icons = ArrayList<BitmapStickerIcon>()
    protected fun updateMatrix() {
        // init matrix to E - identity matrix
        matrix.reset()
        val topLeftX = layer.x * canvasWidth
        val topLeftY = layer.y * canvasHeight
        val centerX = topLeftX + width * holyScale * 0.5f
        val centerY = topLeftY + height * holyScale * 0.5f

        // calculate params
        var rotationInDegree = layer.rotationInDegrees
        var scaleX = layer.scale
        val scaleY = layer.scale
        if (layer.isFlipped) {
            // flip (by X-coordinate) if needed
            rotationInDegree *= -1.0f
            scaleX *= -1.0f
        }

        // applying transformations : L = S * R * T

        // scale
        matrix.preScale(scaleX, scaleY, centerX, centerY)

        // rotate
        matrix.preRotate(rotationInDegree, centerX, centerY)

        // translate
        matrix.preTranslate(topLeftX, topLeftY)

        // applying holy scale - S`, the result will be : L = S * R * T * S`
        matrix.preScale(holyScale, holyScale)
    }

    fun absoluteCenterX(): Float {
        val topLeftX = layer.x * canvasWidth
        return topLeftX + width * holyScale * 0.5f
    }

    fun absoluteCenterY(): Float {
        val topLeftY = layer.y * canvasHeight
        return topLeftY + height * holyScale * 0.5f
    }

    fun absoluteCenter(): PointF {
        val topLeftX = layer.x * canvasWidth
        val topLeftY = layer.y * canvasHeight
        val centerX = topLeftX + width * holyScale * 0.5f
        val centerY = topLeftY + height * holyScale * 0.5f
        return PointF(centerX, centerY)
    }

    fun moveToCanvasCenter() {
        moveCenterTo(PointF(canvasWidth * 0.5f, canvasHeight * 0.5f))
    }

    fun moveToPoint(point: PointF) {
        moveCenterTo(point)
    }

    fun moveCenterTo(moveToCenter: PointF) {
        val currentCenter = absoluteCenter()
        layer.postTranslate(
            dx = 1.0f * (moveToCenter.x - currentCenter.x) / canvasWidth,
            dy = 1.0f * (moveToCenter.y - currentCenter.y) / canvasHeight
        )
    }

    fun setPoint(point: PointF) {
        val currentCenter = absoluteCenter()
        layer.postTranslate(
            dx = ((point.x * 10.0f) - currentCenter.x) / canvasWidth,
            dy = ((point.y * 10.0f) - currentCenter.y) / canvasHeight
        )
    }

    private val pA = PointF()
    private val pB = PointF()
    private val pC = PointF()
    private val pD = PointF()
    fun pointInLayerRect(point: PointF?): Boolean {
        updateMatrix()
        // map rect vertices
        matrix.mapPoints(destPoints, srcPoints)
        pA.x = destPoints[0]
        pA.y = destPoints[1]
        pB.x = destPoints[2]
        pB.y = destPoints[3]
        pC.x = destPoints[4]
        pC.y = destPoints[5]
        pD.x = destPoints[6]
        pD.y = destPoints[7]
        return pointInTriangle(point!!, pA, pB, pC) || pointInTriangle(
            point, pA, pD, pC
        )
    }

    fun draw(canvas: Canvas, drawingPaint: Paint?) {
        updateMatrix()
        canvas.save()
        drawContent(canvas, drawingPaint)
        if (isSelected) {
            // get alpha from drawingPaint
            val storedAlpha = borderPaint.alpha
            if (drawingPaint != null) {
                borderPaint.alpha = drawingPaint.alpha
            }
            drawSelectedBg(canvas)
            // restore border alpha
            borderPaint.alpha = storedAlpha
        }
        canvas.restore()
    }

    private fun drawSelectedBg(canvas: Canvas) {
        matrix.mapPoints(destPoints, srcPoints)
        matrix.mapPoints(customPoints)
        //canvas.drawLines(destPoints, 0, 8, borderPaint)
        //canvas.drawLines(destPoints, 2, 8, borderPaint)

        val x1: Float = destPoints.get(0)
        val y1: Float = destPoints.get(1)
        val x2: Float = destPoints.get(2)
        val y2: Float = destPoints.get(3)
        val x3: Float = destPoints.get(4)
        val y3: Float = destPoints.get(5)
        val x4: Float = destPoints.get(6)
        val y4: Float = destPoints.get(7)

        val xC: Float = customPoints.get(0)
        val yC: Float = customPoints.get(1)

        val rotation: Float = calculateRotation(x3, y3, x4, y4)
        /*icons.forEach {

        }*/

        deleteIcon?.let {
            try {
                when (it.position) {
                    BitmapStickerIcon.LEFT_TOP -> configIconMatrix(it, x1, y1, rotation)
                    BitmapStickerIcon.RIGHT_TOP -> configIconMatrix(it, x2, y2, rotation)
                    BitmapStickerIcon.LEFT_BOTTOM -> configIconMatrix(it, x3, y3, rotation)
                    BitmapStickerIcon.RIGHT_BOTOM -> configIconMatrix(it, x4, y4, rotation)
                    BitmapStickerIcon.CUSTOM -> configCustomIconMatrix(
                        icon = it,
                        x = (x3 - width / 7),
                        y = (y3 - height / 2.5).toFloat(),
                        padding = 0f
                    )
                }

                when (it.position) {
                    BitmapStickerIcon.CUSTOM -> it.drawSmallRadius(canvas, borderPaint)
                    else -> it.draw(canvas, borderPaint)
                }
            } catch (e: Exception) {}
        }
    }

    fun configIconMatrix(
        icon: BitmapStickerIcon, x: Float, y: Float,
        rotation: Float
    ) {
        icon.x = x
        icon.y = y
        icon.matrix.reset()
        //icon.matrix.postRotate(rotation, (icon.width / 2).toFloat(), (icon.height / 2).toFloat())
        icon.matrix.postTranslate(x - icon.width / 2, y - icon.height / 2)
    }

    fun configCustomIconMatrix(
        icon: BitmapStickerIcon, x: Float, y: Float,
        padding: Float
    ) {
        icon.x = x
        icon.y = y
        icon.matrix.reset()
        //icon.matrix.postRotate(rotation, (icon.width / 2).toFloat(), (icon.height / 2).toFloat())
        icon.matrix.postTranslate(x - icon.width / 2, y - icon.height / 2)
    }

    fun setBorderPaint(borderPaint: Paint) {
        this.borderPaint = borderPaint
    }

    fun findCurrentIconTouched(downX: Float, downY: Float): BitmapStickerIcon? {
        /*
        for (icon in icons) {
            val x: Float = icon.getX() - downX
            val y: Float = icon.getY() - downY
            val distance_pow_2 = x * x + y * y
            if (distance_pow_2 <= Math.pow((icon.getIconRadius() + icon.getIconRadius()).toDouble(), 2.0)) {
                return icon
            }
        }*/
        deleteIcon?.let {
            val x: Float = it.getX() - downX
            val y: Float = it.getY() - downY
            val distance_pow_2 = x * x + y * y
            if (distance_pow_2 <= Math.pow(
                    (it.getIconRadius() + it.getIconRadius()).toDouble(),
                    2.0
                )
            ) {
                return it
            }
        }
        return null
    }

    fun setIcon(icon: BitmapStickerIcon) {
        //icons.clear()
        //icons.add(icon)
        deleteIcon = icon
    }

    protected abstract fun drawContent(canvas: Canvas, drawingPaint: Paint?)
    abstract val width: Int
    abstract val height: Int
    open fun release() {
        // free resources here
        //icons.clear()=
        deleteIcon?.release()
    }

    fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = (x1 - x2).toDouble()
        val y = (y1 - y2).toDouble()
        val radians = Math.atan2(y, x)
        return Math.toDegrees(radians).toFloat()
    }

    @Throws(Throwable::class)
    protected open fun finalize() {
        try {
            release()

        } finally {
            //super.finalize()
        }
    }
}