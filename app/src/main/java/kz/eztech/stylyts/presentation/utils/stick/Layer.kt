package kz.eztech.stylyts.presentation.utils.stick

import androidx.annotation.FloatRange




/**
 * Created by Ruslan Erdenoff on 13.01.2021.
 */
class Layer {
    @FloatRange(from = 0.0, to = 360.0)
     var rotationInDegrees = 0f
     var scale = 0f
     var x = 0f
     var y = 0f
     var isFlipped = false
    
    protected fun reset() {
        rotationInDegrees = 0.0f
        scale = 1.0f
        isFlipped = false
        x = 0.0f
        y = 0.0f
    }

    fun postScale(scaleDiff: Float) {
        val newVal = scale + scaleDiff
        if (newVal >= getMinScale() && newVal <= getMaxScale()) {
            scale = newVal
        }
    }

    protected fun getMaxScale(): Float {
        return Limits.MAX_SCALE
    }

    protected fun getMinScale(): Float {
        return Limits.MIN_SCALE
    }

    fun postRotate(rotationInDegreesDiff: Float) {
        rotationInDegrees += rotationInDegreesDiff
        rotationInDegrees %= 360.0f
    }

    fun postTranslate(dx: Float, dy: Float) {
        x += dx
        y += dy
    }

    fun flip() {
        isFlipped = !isFlipped
    }

    fun initialScale(): Float {
        return Limits.INITIAL_ENTITY_SCALE
    }
    
    internal interface Limits {
        companion object {
            const val MIN_SCALE = 0.3f
            const val MAX_SCALE = 0.7f
            const val INITIAL_ENTITY_SCALE = 0.5f
        }
    }

    init {
        reset()
    }
}