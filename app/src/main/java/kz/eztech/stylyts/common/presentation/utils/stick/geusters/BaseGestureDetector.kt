package kz.eztech.imageviewdragandscale.sticker.geusters

import android.content.Context
import android.view.MotionEvent

/**
 * Created by Ruslan Erdenoff on 13.01.2021.
 */
abstract class BaseGestureDetector(protected val mContext: Context) {
    var isInProgress = false
    protected var mPrevEvent: MotionEvent? = null
    protected var mCurrEvent: MotionEvent? = null
    protected var mCurrPressure = 0f
    protected var mPrevPressure = 0f
    var timeDelta: Long = 0

    fun onTouchEvent(event: MotionEvent): Boolean {
        val actionCode = event.action and MotionEvent.ACTION_MASK
        if (!isInProgress) {
            handleStartProgressEvent(actionCode, event)
        } else {
            handleInProgressEvent(actionCode, event)
        }
        return true
    }

    protected abstract fun handleStartProgressEvent(actionCode: Int, event: MotionEvent)
    protected abstract fun handleInProgressEvent(actionCode: Int, event: MotionEvent)
    protected open fun updateStateByEvent(curr: MotionEvent) {
        val prev = mPrevEvent
        if (mCurrEvent != null) {
            mCurrEvent!!.recycle()
            mCurrEvent = null
        }
        mCurrEvent = MotionEvent.obtain(curr)
        timeDelta = curr.eventTime - prev!!.eventTime
        mCurrPressure = curr.getPressure(curr.actionIndex)
        mPrevPressure = prev.getPressure(prev.actionIndex)
    }

    protected open fun resetState() {
        if (mPrevEvent != null) {
            mPrevEvent!!.recycle()
            mPrevEvent = null
        }
        if (mCurrEvent != null) {
            mCurrEvent!!.recycle()
            mCurrEvent = null
        }
        isInProgress = false
    }

    val eventTime: Long
        get() = mCurrEvent!!.eventTime

    companion object {
        const val PRESSURE_THRESHOLD = 0.67f
    }
}