package kz.eztech.stylyts.presentation.fragments.main.constructor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_clean_background.*
import kz.eztech.stylyts.BuildConfig
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.main.constructor.CleanBackgroundContract
import kz.eztech.stylyts.presentation.dialogs.SaveItemAcceptDialog
import kz.eztech.stylyts.presentation.presenters.main.CleanBackgroundPresenter
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.removebg.ErrorResponse
import kz.eztech.stylyts.presentation.utils.removebg.RemoveBg
import java.io.File
import javax.inject.Inject

class CleanBackgroundFragment : BaseFragment<MainActivity>(), CleanBackgroundContract.View,View.OnClickListener,
	DialogChooserListener {

	private var photoUri: Uri? = null
	private var outputBitmap:Bitmap? = null
	private lateinit var saveItemAcceptDialog: SaveItemAcceptDialog
	private val hashMap = HashMap<String,String>()
	@Inject
	lateinit var presenter:CleanBackgroundPresenter
	override fun getLayoutId(): Int {
		return R.layout.fragment_clean_background
	}
	
	override fun getContractView(): BaseView {
		return this
	}
	
	
	override fun customizeActionBar() {
		with(include_toolbar_clean_background){
			toolbar_left_corner_action_image_button.visibility = android.view.View.GONE
			toolbar_back_text_view.visibility = android.view.View.VISIBLE
			toolbar_title_text_view.visibility = android.view.View.VISIBLE
			toolbar_title_text_view.text = "Добавить вещь"
			toolbar_right_text_text_view.visibility = android.view.View.GONE
			elevation = 0f
			customizeActionToolBar(this)
		}
	}
	
	override fun initializeDependency() {
		(currentActivity.application as StylytsApp).applicationComponent.inject(this)
	}
	
	override fun initializePresenter() {
		presenter.attach(this)
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
		saveItemAcceptDialog = SaveItemAcceptDialog()
		saveItemAcceptDialog.setChoiceListener(this)
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
				val bundle = Bundle()
				outputBitmap?.let {
					bundle.putParcelable("photoBitmap", it)
				}?: run{
					photoUri?.let {
						bundle.putParcelable("photoUri", it)
					}
				}
				saveItemAcceptDialog.arguments = bundle
				saveItemAcceptDialog.show(childFragmentManager, "SaveItemDialog")
			}
		}
	}

	override fun onChoice(v: View?, item: Any?) {
		when(v?.id){
			R.id.toolbar_right_text_text_view -> {
				item?.let { description ->
					description as String
					hashMap["description"] = description
					outputBitmap?.let {
						val file = FileUtils.createPngFileFromBitmap(requireContext(), it)
						file?.let { inpFile ->
							processImage(inpFile)
						}?: run {
							displayMessage("Ошибка при конвертировании фотографии")
						}
					}?:run{
						photoUri?.let {
							val imageFile = File(it.path)
							processImage(imageFile)
						}
					}
				}
			}
		}
	}

	private fun processImage(file:File){
		presenter.saveItem(
			currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)?:"",
			hashMap,
			file
		)
	}

	override fun processPostInitialization() {
	
	}

	override fun processItemDetail(model: ClothesMainModel) {
		val itemsList = ArrayList<ClothesMainModel>()
		val bundle = Bundle()
		itemsList.add(model)
		bundle.putParcelableArrayList("items",itemsList)
		findNavController().navigate(R.id.createCollectionFragment,bundle)
	}

	override fun disposeRequests() {
		presenter.disposeRequests()
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