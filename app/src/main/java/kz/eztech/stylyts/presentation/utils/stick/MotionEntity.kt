package kz.eztech.stylyts.presentation.utils.stick

import android.graphics.*
import android.util.Log
import androidx.annotation.IntRange
import kz.eztech.stylyts.presentation.utils.stick.MathUtils.pointInTriangle

/**
 * Created by Ruslan Erdenoff on 13.01.2021.
 */
abstract class MotionEntity(
    val layer: Layer,
    @IntRange(from = 1) var canvasWidth: Int,
    @IntRange(from = 1) var canvasHeight: Int
) {
    protected val matrix = Matrix()
    var isSelected = false
    protected var holyScale = 0f
    private val destPoints = FloatArray(10) // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0
    protected val srcPoints = FloatArray(10) // x0, y0, x1, y1, x2, y2, x3, y3, x0, y0
    private var borderPaint = Paint()
    private var deleteBitmap: Bitmap? = null
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
    
    fun absoluteWidth():Float{
        return canvasWidth.toFloat()
    }
    
    fun absoluteHeight():Float{
        return canvasHeight.toFloat()
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
    fun moveCenterTo(moveToCenter: PointF) {
        val currentCenter = absoluteCenter()
        layer.postTranslate(
            1.0f * (moveToCenter.x - currentCenter.x) / canvasWidth,
            1.0f * (moveToCenter.y - currentCenter.y) / canvasHeight
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
        canvas.drawLines(destPoints, 0, 8, borderPaint)
        canvas.drawLines(destPoints, 2, 8, borderPaint)
        deleteBitmap?.let {
            canvas.drawBitmap(it,canvasHeight.toFloat(),canvasHeight.toFloat(),borderPaint)
        }
       
    }

    fun setBorderPaint(borderPaint: Paint) {
        this.borderPaint = borderPaint
    }
    
    fun setDeleteBitmap(bitmap: Bitmap){
        this.deleteBitmap = bitmap
    }

    protected abstract fun drawContent(canvas: Canvas, drawingPaint: Paint?)
    abstract val width: Int
    abstract val height: Int
    open fun release() {
        // free resources here
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