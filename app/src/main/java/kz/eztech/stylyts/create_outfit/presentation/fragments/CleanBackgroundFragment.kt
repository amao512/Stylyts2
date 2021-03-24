package kz.eztech.stylyts.create_outfit.presentation.fragments

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
import kz.eztech.stylyts.common.data.models.SharedConstants
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.base.DialogChooserListener
import kz.eztech.stylyts.create_outfit.presentation.contracts.CleanBackgroundContract
import kz.eztech.stylyts.create_outfit.presentation.dialogs.SaveItemAcceptDialog
import kz.eztech.stylyts.common.presentation.presenters.CleanBackgroundPresenter
import kz.eztech.stylyts.common.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.common.presentation.utils.FileUtils
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show
import kz.eztech.stylyts.common.presentation.utils.removebg.ErrorResponse
import kz.eztech.stylyts.common.presentation.utils.removebg.RemoveBg
import java.io.File
import javax.inject.Inject

class CleanBackgroundFragment : BaseFragment<MainActivity>(), CleanBackgroundContract.View,
    View.OnClickListener,
    DialogChooserListener {

    @Inject lateinit var presenter: CleanBackgroundPresenter
    private lateinit var saveItemAcceptDialog: SaveItemAcceptDialog

    private val hashMap = HashMap<String, String>()

    private var photoUri: Uri? = null
    private var outputBitmap: Bitmap? = null

    override fun getLayoutId(): Int = R.layout.fragment_clean_background

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_clean_background) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()

            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.clean_background_title)

            toolbar_right_text_text_view.hide()

            customizeActionToolBar(toolbar = this)
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
            if (it.containsKey("uri")) {
                photoUri = it.getParcelable("uri")
            }
        }
        photoUri?.let {
            Glide.with(this)
                .load(it)
                .into(this.image_view_fragment_clean_background_image)
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
        when (v?.id) {
			R.id.button_fragment_clean_background_image_clear_bg -> cleanImageBackground()
			R.id.button_fragment_clean_background_image_add_to_wardrobe -> addToWardrobe()
        }
    }

    override fun onChoice(
        v: View?,
        item: Any?
    ) {
        when (v?.id) {
			R.id.toolbar_right_text_text_view -> choiceImage(item)
        }
    }

    override fun processPostInitialization() {}

    override fun processItemDetail(model: ClothesMainModel) {
        val itemsList = ArrayList<ClothesMainModel>()
        val bundle = Bundle()

        itemsList.add(model)

        bundle.putParcelableArrayList("items", itemsList)
        findNavController().navigate(R.id.createCollectionFragment, bundle)
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        base_progress_clean_background.show()
    }

    override fun hideProgress() {
        base_progress_clean_background.hide()
    }

    private fun cleanImageBackground() {
        photoUri?.let {
            val imageFile = File(it.path)

            displayProgress()
            RemoveBg.from(imageFile, object : RemoveBg.RemoveBgCallback {

                override fun onProcessing() {}

                override fun onUploadProgress(progress: Float) {}

                override fun onError(errors: List<ErrorResponse.Error>) {
                    currentActivity.runOnUiThread {
                        hideProgress()
                        displayToast("Не удалось убрать фон")
                    }
                }

                override fun onSuccess(bitmap: Bitmap) {
                    outputBitmap = bitmap
                    currentActivity.runOnUiThread {
                        image_view_fragment_clean_background_image.setImageBitmap(
                            outputBitmap
                        )
                        hideProgress()
                    }
                }
            })
        }
    }

    private fun addToWardrobe() {
        val bundle = Bundle()

        outputBitmap?.let {
            bundle.putParcelable("photoBitmap", it)
        } ?: run {
            photoUri?.let {
                bundle.putParcelable("photoUri", it)
            }
        }

        saveItemAcceptDialog.arguments = bundle
        saveItemAcceptDialog.show(childFragmentManager, "SaveItemDialog")
    }

    private fun choiceImage(item: Any?) {
        item?.let { description ->
            description as String

            hashMap["description"] = description
            outputBitmap?.let {
                val file = FileUtils.createPngFileFromBitmap(requireContext(), it)

                file?.let { inpFile ->
                    processImage(inpFile)
                } ?: run {
                    displayMessage(msg = getString(R.string.clean_background_convert_error))
                }
            } ?: run {
                photoUri?.let {
                    val imageFile = File(it.path)
                    processImage(imageFile)
                }
            }
        }
    }

    private fun processImage(file: File) {
        presenter.saveItem(
            token =getTokeFromSharedPref(),
            hashMap = hashMap,
            data = file
        )
    }

    private fun getTokeFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY) ?: EMPTY_STRING
    }
}