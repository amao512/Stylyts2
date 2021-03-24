package kz.eztech.imageviewdragandscale.sticker.geusters

import android.content.Context
import android.graphics.PointF
import android.view.MotionEvent

/**
 * Created by Ruslan Erdenoff on 13.01.2021.
 */
class MoveGestureDetector:BaseGestureDetector {
	private val FOCUS_DELTA_ZERO = PointF()
	private var mListener: OnMoveGestureListener
	private var mCurrFocusInternal: PointF? = null
	private var mPrevFocusInternal: PointF? = null
	private val mFocusExternal = PointF()
	private var mFocusDeltaExternal = PointF()
	
	constructor(context: Context, listener: OnMoveGestureListener):super(context) {
		mListener = listener
	}
	
	override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent) {
		when (actionCode) {
			MotionEvent.ACTION_DOWN -> {
				resetState()
				mPrevEvent = MotionEvent.obtain(event)
				timeDelta = 0
				updateStateByEvent(event)
			}
			MotionEvent.ACTION_MOVE -> isInProgress = mListener.onMoveBegin(this)
		}
	}
	
	override fun handleInProgressEvent(actionCode: Int, event: MotionEvent) {
		when (actionCode) {
			MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
				mListener.onMoveEnd(this)
				resetState()
			}
			MotionEvent.ACTION_MOVE -> {
				updateStateByEvent(event)
				if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD) {
					val updatePrevious = mListener.onMove(this)
					if (updatePrevious) {
						mPrevEvent?.recycle()
						mPrevEvent = MotionEvent.obtain(event)
					}
				}
			}
		}
	}
	
	override fun updateStateByEvent(curr: MotionEvent) {
		super.updateStateByEvent(curr)
		val prev = mPrevEvent
		mCurrFocusInternal = determineFocalPoint(curr)
		mPrevFocusInternal = determineFocalPoint(prev!!)
		val mSkipNextMoveEvent = prev.pointerCount != curr.pointerCount
		mFocusDeltaExternal = if (mSkipNextMoveEvent) FOCUS_DELTA_ZERO else PointF(mCurrFocusInternal!!.x - mPrevFocusInternal!!.x, mCurrFocusInternal!!.y - mPrevFocusInternal!!.y)
		mFocusExternal.x += mFocusDeltaExternal.x
		mFocusExternal.y += mFocusDeltaExternal.y
	}
	
	private fun determineFocalPoint(e: MotionEvent): PointF? {
		val pCount = e.pointerCount
		var x = 0f
		var y = 0f
		for (i in 0 until pCount) {
			x += e.getX(i)
			y += e.getY(i)
		}
		return PointF(x / pCount, y / pCount)
	}
	
	interface OnMoveGestureListener {
		fun onMove(detector: MoveGestureDetector?): Boolean
		fun onMoveBegin(detector: MoveGestureDetector?): Boolean
		fun onMoveEnd(detector: MoveGestureDetector?)
	}
	
	open class SimpleOnMoveGestureListener : OnMoveGestureListener {
		override fun onMove(detector: MoveGestureDetector?): Boolean {
			return false
		}
		
		override fun onMoveBegin(detector: MoveGestureDetector?): Boolean {
			return true
		}
		
		override fun onMoveEnd(detector: MoveGestureDetector?) {
			// Do nothing, overridden implementation may be used
		}
	}
	
	fun getFocusX(): Float {
		return mFocusExternal.x
	}
	
	fun getFocusY(): Float {
		return mFocusExternal.y
	}
	
	fun getFocusDelta(): PointF? {
		return mFocusDeltaExternal
	}
}