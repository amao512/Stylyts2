package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_create_collection_accept.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_clean_background.*
import kz.eztech.stylyts.BuildConfig
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.constructor.CleanBackgroundContract
import kz.eztech.stylyts.presentation.utils.removebg.ErrorResponse
import kz.eztech.stylyts.presentation.utils.removebg.RemoveBg
import java.io.File

class CleanBackgroundFragment : BaseFragment<MainActivity>(), CleanBackgroundContract.View,View.OnClickListener {

	private var photoUri: Uri? = null
	private var outputBitmap:Bitmap? = null
	override fun getLayoutId(): Int {
		return R.layout.fragment_clean_background
	}
	
	override fun getContractView(): BaseView {
		return this
	}
	
	
	override fun customizeActionBar() {
		with(include_toolbar_clean_background){
			image_button_left_corner_action.visibility = android.view.View.GONE
			text_view_toolbar_back.visibility = android.view.View.VISIBLE
			text_view_toolbar_title.visibility = android.view.View.VISIBLE
			text_view_toolbar_title.text = "Добавить вещь"
			text_view_toolbar_right_text.visibility = android.view.View.GONE
			elevation = 0f
			customizeActionToolBar(this)
		}
	}
	
	override fun initializeDependency() {
	
	}
	
	override fun initializePresenter() {
	
	}
	
	override fun initializeArguments() {
		arguments?.let {
			if(it.containsKey("uri")){
				photoUri = it.getParcelable("uri")
			}
		}
		photoUri?.let {
			Glide.with(this).load(it).into(this.image_view_fragment_clean_background_image)
		}
	}
	
	override fun initializeViewsData() {
		RemoveBg.init(BuildConfig.REMOVE_BG_API_KEY)
	}
	
	override fun initializeViews() {
	
	}
	
	override fun initializeListeners() {
		button_fragment_clean_background_image_clear_bg.setOnClickListener(this)
		button_fragment_clean_background_image_add_to_wardrobe.setOnClickListener(this)
	}

	override fun onClick(v: View?) {
		when(v?.id){
			R.id.button_fragment_clean_background_image_clear_bg -> {
				photoUri?.let {
					val imageFile = File(it.path)
					displayProgress()
					RemoveBg.from(imageFile, object : RemoveBg.RemoveBgCallback {

						override fun onProcessing() {

						}

						override fun onUploadProgress(progress: Float) {

						}

						override fun onError(errors: List<ErrorResponse.Error>) {
							currentActivity.runOnUiThread {
								hideProgress()
								displayToast("Не удалось убрать фон")
							}

						}

						override fun onSuccess(bitmap: Bitmap) {
							outputBitmap = bitmap
							currentActivity.runOnUiThread {
								image_view_fragment_clean_background_image.setImageBitmap(outputBitmap)
								hideProgress()
							}
						}
					})
				}

			}
			R.id.button_fragment_clean_background_image_add_to_wardrobe -> {

			}
		}
	}

	override fun processPostInitialization() {
	
	}
	
	override fun disposeRequests() {
	
	}
	
	override fun displayMessage(msg: String) {
		displayToast(msg)
	}
	
	override fun isFragmentVisible(): Boolean {
		return isVisible
	}
	
	override fun displayProgress() {
		base_progress_clean_background.visibility = View.VISIBLE
	}
	
	override fun hideProgress() {
		base_progress_clean_background.visibility = View.GONE
	}
}