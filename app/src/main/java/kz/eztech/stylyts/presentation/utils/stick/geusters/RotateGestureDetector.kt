package kz.eztech.imageviewdragandscale.sticker.geusters

import android.content.Context
import android.view.MotionEvent

/**
 * Created by Ruslan Erdenoff on 13.01.2021.
 */
class RotateGestureDetector(context: Context, private val mListener: OnRotateGestureListener) : TwoFingerGestureDetector(context) {
	private var mSloppyGesture = false
	override fun handleStartProgressEvent(actionCode: Int, event: MotionEvent) {
		when (actionCode) {
			MotionEvent.ACTION_POINTER_DOWN -> {
				// At least the second finger is on screen now
				resetState() // In case we missed an UP/CANCEL event
				mPrevEvent = MotionEvent.obtain(event)
				timeDelta = 0
				updateStateByEvent(event)
				
				// See if we have a sloppy gesture
				mSloppyGesture = isSloppyGesture(event)
				if (!mSloppyGesture) {
					// No, start gesture now
					isInProgress = mListener.onRotateBegin(this)
				}
			}
			MotionEvent.ACTION_MOVE -> {
				if (!mSloppyGesture) {
					return
				}
				
				// See if we still have a sloppy gesture
				mSloppyGesture = isSloppyGesture(event)
				if (!mSloppyGesture) {
					// No, start normal gesture now
					isInProgress = mListener.onRotateBegin(this)
				}
			}
			MotionEvent.ACTION_POINTER_UP -> if (!mSloppyGesture) {
				return
			}
		}
	}
	
	override fun handleInProgressEvent(actionCode: Int, event: MotionEvent) {
		when (actionCode) {
			MotionEvent.ACTION_POINTER_UP -> {
				// Gesture ended but
				updateStateByEvent(event)
				if (!mSloppyGesture) {
					mListener.onRotateEnd(this)
				}
				resetState()
			}
			MotionEvent.ACTION_CANCEL -> {
				if (!mSloppyGesture) {
					mListener.onRotateEnd(this)
				}
				resetState()
			}
			MotionEvent.ACTION_MOVE -> {
				updateStateByEvent(event)
				
				// Only accept the event if our relative pressure is within
				// a certain limit. This can help filter shaky data as a
				// finger is lifted.
				if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD) {
					val updatePrevious = mListener.onRotate(this)
					if (updatePrevious) {
						mPrevEvent?.recycle()
						mPrevEvent = MotionEvent.obtain(event)
					}
				}
			}
		}
	}
	
	override fun resetState() {
		super.resetState()
		mSloppyGesture = false
	}
	
	val rotationDegreesDelta: Float
		get() {
			val diffRadians = Math.atan2(mPrevFingerDiffY.toDouble(), mPrevFingerDiffX.toDouble()) - Math.atan2(mCurrFingerDiffY.toDouble(), mCurrFingerDiffX.toDouble())
			return (diffRadians * 180 / Math.PI).toFloat()
		}
	
	interface OnRotateGestureListener {
		fun onRotate(detector: RotateGestureDetector?): Boolean
		fun onRotateBegin(detector: RotateGestureDetector?): Boolean
		fun onRotateEnd(detector: RotateGestureDetector?)
	}
	
	open class SimpleOnRotateGestureListener : OnRotateGestureListener {
		override fun onRotate(detector: RotateGestureDetector?): Boolean {
			return false
		}
		
		override fun onRotateBegin(detector: RotateGestureDetector?): Boolean {
			return true
		}
		
		override fun onRotateEnd(detector: RotateGestureDetector?) {
			// Do nothing, overridden implementation may be used
		}
	}
}