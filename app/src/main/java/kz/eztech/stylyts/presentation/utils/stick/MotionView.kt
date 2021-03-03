package kz.eztech.stylyts.presentation.utils.stick

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import kz.eztech.imageviewdragandscale.sticker.geusters.MoveGestureDetector
import kz.eztech.imageviewdragandscale.sticker.geusters.RotateGestureDetector
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.contracts.main.constructor.MotionViewContract
import kz.eztech.stylyts.presentation.interfaces.MotionViewTapListener
import java.util.*

/**
 * Created by Ruslan Erdenoff on 13.01.2021.
 */
class MotionView : FrameLayout {
	fun attachView(motionViewContract: MotionViewContract){
		this.motionViewContract = motionViewContract
	}

	interface Constants {
		companion object {
			const val SELECTED_LAYER_ALPHA = 0.15f
		}
	}

	interface MotionViewCallback {
		fun onEntitySelected(entity: MotionEntity?)
		fun onEntityDoubleTap(entity: MotionEntity)
	}

	// layers
	private val entities: MutableList<MotionEntity> = ArrayList()
	var selectedEntity: MotionEntity? = null
		private set
	private var selectedLayerPaint: Paint? = null

	// callback
	private var motionViewCallback: MotionViewCallback? = null

	private lateinit var deleteIcon:BitmapStickerIcon
	private var motionViewContract:MotionViewContract? = null

	// gesture detection
	private var scaleGestureDetector: ScaleGestureDetector? = null
	private var rotateGestureDetector: RotateGestureDetector? = null
	private var moveGestureDetector: MoveGestureDetector? = null
	private var gestureDetectorCompat: GestureDetectorCompat? = null
	private var tapListener: MotionViewTapListener? = null
	
	private var isFlexible = true
	private var isDeleted = false

	// constructors
	constructor(context: Context) : super(context) {
		init(context)
	}

	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
		init(context)
	}

	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init(context)
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
		init(context)
	}

	private fun init(context: Context) {
		setWillNotDraw(false)
		selectedLayerPaint = Paint()
		selectedLayerPaint!!.alpha = (255 * Constants.SELECTED_LAYER_ALPHA).toInt()
		selectedLayerPaint!!.isAntiAlias = true
		deleteIcon = BitmapStickerIcon(
				ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_delete_24),
				BitmapStickerIcon.RIGHT_TOP)
		deleteIcon.iconEvent = DeleteIconEvent()
		
		scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
		rotateGestureDetector = RotateGestureDetector(context, RotateListener())
		moveGestureDetector = MoveGestureDetector(context, MoveListener())
		gestureDetectorCompat = GestureDetectorCompat(context, TapsListener())
		setOnTouchListener(onTouchListener)
		updateUI()
	}
	
	fun setFlexible(isFlexible: Boolean){
		this.isFlexible = isFlexible
		if(!isFlexible){
			scaleGestureDetector = null
			rotateGestureDetector = null
		}
	}

	fun setTapListener(listener:MotionViewTapListener){
		this.tapListener = listener
	}
	
	fun setCustomDeleteIcon(deleteIconId: Int){
		deleteIcon = BitmapStickerIcon(
				ContextCompat.getDrawable(getContext(), deleteIconId),
				BitmapStickerIcon.CUSTOM)
		deleteIcon.iconEvent = DeleteIconEvent()
		selectedEntity?.let {
			it.setIcon(deleteIcon)
		}
	}

	fun getEntities(): List<MotionEntity> {
		return entities
	}

	fun setMotionViewCallback(callback: MotionViewCallback?) {
		motionViewCallback = callback
	}

	fun addEntity(entity: MotionEntity?) {
		if (entity != null) {
			entities.add(entity)
			selectEntity(entity, false)
		}
	}

	fun addEntityAndPosition(entity: MotionEntity?,isCustom:Boolean?=false) {
		if (entity != null) {
			initEntityBorder(entity)
			isCustom?.let {
				if(isCustom){
					initialTranslateAndScaleCustom(entity)
				}else{
					initialTranslateAndScale(entity)
				}
			} ?: run {
				initialTranslateAndScale(entity)
			}
			
			
			entities.add(entity)
			selectEntity(entity, true)
		}
	}

	private fun initEntityBorder(entity: MotionEntity) {
		// init stroke
		val strokeSize = resources.getDimensionPixelSize(R.dimen.stroke_size)
		val borderPaint = Paint()
		borderPaint.strokeWidth = strokeSize.toFloat()
		borderPaint.isAntiAlias = true
		borderPaint.color = ContextCompat.getColor(context, R.color.white)
		entity.setBorderPaint(borderPaint)
		entity.setIcon(deleteIcon)
	}

	override fun dispatchDraw(canvas: Canvas) {
		super.dispatchDraw(canvas)
		if (selectedEntity != null) {
			selectedEntity!!.draw(canvas, selectedLayerPaint)
		}
	}

	override fun onDraw(canvas: Canvas) {
		drawAllEntities(canvas)
		super.onDraw(canvas)
	}

	private fun drawAllEntities(canvas: Canvas) {
		for (i in entities.indices) {
			entities[i].draw(canvas, null)
		}
	}

	val thumbnailImage: Bitmap
		get() {
			selectEntity(null, false)
			val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
			// IMPORTANT: always create white background, cos if the image is saved in JPEG format,
			// which doesn't have transparent pixels, the background will be black
			bmp.eraseColor(Color.WHITE)
			val canvas = Canvas(bmp)
			drawAllEntities(canvas)
			return bmp
		}

	private fun updateUI() {
		invalidate()
	}

	fun handleTranslate(delta: PointF?) {
		if (selectedEntity != null) {
			val newCenterX = selectedEntity!!.absoluteCenterX() + delta!!.x
			val newCenterY = selectedEntity!!.absoluteCenterY() + delta.y

			var needUpdateUI = false
			if (newCenterX >= 0 && newCenterX <= width) {
				selectedEntity!!.layer.postTranslate(delta.x / width, 0.0f)
				needUpdateUI = true
			}
			if (newCenterY >= 0 && newCenterY <= height) {
				selectedEntity!!.layer.postTranslate(0.0f, delta.y / height)
				needUpdateUI = true
			}
			if (needUpdateUI) {
				updateUI()
			}
		}
	}

	private fun initialTranslateAndScale(entity: MotionEntity) {
		entity.moveToCanvasCenter()
		entity.layer.scale = entity.layer.initialScale()
	}
	
	private fun initialTranslateAndScaleCustom(entity: MotionEntity){
		entity.moveToCanvasCenter()
		entity.layer.scale = entity.layer.getMinMediumScale()
	}

	private fun selectEntity(entity: MotionEntity?, updateCallback: Boolean) {
		if (selectedEntity != null) {
			selectedEntity!!.isSelected = (false)
		}
		entity?.isSelected = (true)
		selectedEntity = entity
		invalidate()
		if (updateCallback && motionViewCallback != null) {
			motionViewCallback!!.onEntitySelected(entity)
		}
	}

	fun unselectEntity() {
		if (selectedEntity != null) {
			selectEntity(null, true)
		}
	}

	private fun findEntityAtPoint(x: Float, y: Float): MotionEntity? {
		var selected: MotionEntity? = null
		val p = PointF(x, y)
		for (i in entities.indices.reversed()) {
			if (entities[i].pointInLayerRect(p)) {
				selected = entities[i]
				break
			}
		}
		return selected
	}

	private fun updateSelectionOnTap(e: MotionEvent) {
		val entity = findEntityAtPoint(e.x, e.y)
		selectEntity(entity, true)
	}

	private fun updateOnLongPress(e: MotionEvent) {
		// if layer is currently selected and point inside layer - move it to front
		if (selectedEntity != null) {
			val p = PointF(e.x, e.y)
			if (selectedEntity!!.pointInLayerRect(p)) {
				bringLayerToFront(selectedEntity!!)
			}
		}
	}

	private fun bringLayerToFront(entity: MotionEntity) {
		// removing and adding brings layer to front
		if (entities.remove(entity)) {
			entities.add(entity)
			invalidate()
		}
	}

	private fun moveEntityToBack(entity: MotionEntity?) {
		if (entity == null) {
			return
		}
		if (entities.remove(entity)) {
			entities.add(0, entity)
			invalidate()
		}
	}

	fun flipSelectedEntity() {
		if (selectedEntity == null) {
			return
		}
		selectedEntity!!.layer.flip()
		invalidate()
	}

	fun moveSelectedBack() {
		moveEntityToBack(selectedEntity)
	}

	fun deletedSelectedEntity() {
		isDeleted = true
		if (selectedEntity == null) {
			return
		}
		if (entities.remove(selectedEntity)) {
			motionViewContract?.deleteSelectedView(selectedEntity!!)
			selectedEntity!!.release()
			selectedEntity = null
			invalidate()
		}
	}
	
	fun deleteEntity(entity: MotionEntity){
		if (entities.remove(entity)) {
			motionViewContract?.deleteSelectedView(entity)
			entity.release()
			invalidate()
		}
	}
	

	// memory
	fun release() {
		for (entity in entities) {
			entity.release()
		}
	}



	// gesture detectors
	private val onTouchListener = OnTouchListener { v, event ->
		when (event.getAction()) {
			MotionEvent.ACTION_DOWN -> {
				selectedEntity?.let {
					val currentIcon = it.findCurrentIconTouched(event.getX(), event.getY())
					if (currentIcon != null) {
						currentIcon.onActionUp(this, event)
					}
				}
			}
		}
		
		scaleGestureDetector?.let {
			it.onTouchEvent(event)
		}
		
		rotateGestureDetector?.let {
			it.onTouchEvent(event)
		}
		moveGestureDetector?.let {
			it.onTouchEvent(event)
		}
		gestureDetectorCompat?.let {
			it.onTouchEvent(event)
		}
		true
	}

	private inner class TapsListener : SimpleOnGestureListener() {
		override fun onDoubleTap(e: MotionEvent): Boolean {
			if (motionViewCallback != null && selectedEntity != null) {
				motionViewCallback!!.onEntityDoubleTap(selectedEntity!!)
			}
			return true
		}

		override fun onLongPress(e: MotionEvent) {
			updateOnLongPress(e)
		}
		
		override fun onDown(e: MotionEvent): Boolean {
			updateSelectionOnTap(e)
			return true
		}

		override fun onSingleTapUp(e: MotionEvent): Boolean {
			if(isDeleted){
				isDeleted = false
			}else{
				if( findEntityAtPoint(e.x, e.y) == null){
					tapListener?.onSingleTapUp()
				}
			}

			return true
		}
	}

	private inner class ScaleListener : SimpleOnScaleGestureListener() {
		override fun onScale(detector: ScaleGestureDetector): Boolean {
			if (selectedEntity != null) {
				val scaleFactorDiff = detector.scaleFactor
				selectedEntity!!.layer.postScale(scaleFactorDiff - 1.0f)
				updateUI()
			}
			return true
		}
	}

	private inner class RotateListener : RotateGestureDetector.SimpleOnRotateGestureListener() {
		override fun onRotate(detector: RotateGestureDetector?): Boolean {
			if (selectedEntity != null) {
				selectedEntity!!.layer.postRotate(-detector!!.rotationDegreesDelta)
				updateUI()
			}
			return true
		}
	}

	private inner class MoveListener : MoveGestureDetector.SimpleOnMoveGestureListener() {
		override fun onMove(detector: MoveGestureDetector?): Boolean {
			handleTranslate(detector!!.getFocusDelta())
			return true
		}
	}

	companion object {
		private val TAG = MotionView::class.java.simpleName
	}
}