package kz.eztech.stylyts.presentation.dialogs.collection_constructor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_save_clothes_accept.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCreateModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.filter.FilterCheckAdapter
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection_constructor.SaveClothesAcceptContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection_constructor.SaveClothesAcceptPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.extensions.displaySnackBar
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.io.File
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 24.02.2021.
 */
class SaveClothesAcceptDialog(
    private val listener: DialogChooserListener
) : DialogFragment(), View.OnClickListener, UniversalViewClickListener,
    SaveClothesAcceptContract.View {

    @Inject
    lateinit var presenter: SaveClothesAcceptPresenter
    private lateinit var adapter: FilterCheckAdapter
    private lateinit var clothesCreateModel: ClothesCreateModel

    private var listMode: Int = TYPE_LIST_MODE

    companion object {
        private const val TOKEN_KEY = "token"
        private const val PHOTO_URI_KEY = "photoUri"
        private const val PHOTO_BITMAP_KEY = "photoBitmap"

        private const val TYPE_LIST_MODE = 0
        private const val CATEGORY_LIST_MODE = 1
        private const val STYLE_LIST_MODE = 2

        fun getNewInstance(
            token: String,
            listener: DialogChooserListener,
            photoBitmap: Bitmap?,
            photoUri: Uri?
        ): SaveClothesAcceptDialog {
            val dialog = SaveClothesAcceptDialog(listener)
            val bundle = Bundle()

            bundle.putString(TOKEN_KEY, token)
            photoBitmap?.let {
                bundle.putParcelable(PHOTO_BITMAP_KEY, it)
            } ?: run {
                photoUri?.let {
                    bundle.putParcelable(PHOTO_URI_KEY, it)
                }
            }
            dialog.arguments = bundle

            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_save_clothes_accept, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initializeDependency()
        initializePresenter()
        customizeActionBar()
        initializeArguments()
        initializeViewsData()
        initializeViews()
        initializeListeners()
        processPostInitialization()
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> {
                dismiss()
            }
            R.id.toolbar_right_text_text_view -> createClothes()
            R.id.dialog_save_clothes_list_toolbar_back_text_view -> onBackClicked()
            R.id.dialog_save_clothes_list_toolbar_next_text_view -> onNextClicked()
        }
    }

    override fun onViewClicked(
        view: View,
        position: Int,
        item: Any?
    ) {
        when (item) {
            is FilterCheckModel -> onFilterItemClicked(item, position)
        }
    }

    override fun customizeActionBar() {
        with(include_toolbar_dialog_save_clothes) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@SaveClothesAcceptDialog)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.save_item_accept_add_item)

            toolbar_right_text_text_view.show()
            toolbar_right_text_text_view.text = context.getString(R.string.save_item_accept_ready)
            toolbar_right_text_text_view.setOnClickListener(this@SaveClothesAcceptDialog)

            setDoneButtonUnClickable()
        }
    }

    override fun initializeDependency() {
        (activity?.application as StylytsApp).applicationComponent.inject(dialog = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        adapter = FilterCheckAdapter()
        adapter.setOnClickListener(listener = this)

        clothesCreateModel = ClothesCreateModel()
    }

    override fun initializeViews() {
        dialog_save_clothes_recycler_view.adapter = adapter

        getPhotoBitmapFromArgs()?.let {
            clothesCreateModel.coverPhoto = FileUtils.createPngFileFromBitmap(requireContext(), it)

        }
        getPhotoUriFromArgs()?.path?.let {
            clothesCreateModel.coverPhoto = File(it)
        }

        clothesCreateModel.title = edit_text_view_dialog_save_clothes_accept_sign.text.toString()
    }

    override fun initializeListeners() {
        dialog_save_clothes_list_toolbar_back_text_view.setOnClickListener(this)
        dialog_save_clothes_list_toolbar_next_text_view.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        getPhotoUriFromArgs()?.let {
            Glide.with(image_view_dialog_save_clothes_accept.context)
                .load(it)
                .into(image_view_dialog_save_clothes_accept)
        }
        getPhotoBitmapFromArgs()?.let {
            Glide.with(image_view_dialog_save_clothes_accept.context)
                .load(it)
                .into(image_view_dialog_save_clothes_accept)
        }

        presenter.getTypes(token = getTokenFromArgs())
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        view?.let {
            displaySnackBar(it.context, it, msg)
        }
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        dialog_save_clothes_small_progress_bar.show()
        dialog_save_clothes_accept_list_toolbar.hide()
        dialog_save_clothes_recycler_view.hide()
    }

    override fun hideProgress() {
        dialog_save_clothes_small_progress_bar.hide()
        dialog_save_clothes_accept_list_toolbar.show()
        dialog_save_clothes_recycler_view.show()
    }

    override fun processTypes(resultsModel: ResultsModel<ClothesTypeModel>) {
        dialog_save_clothes_accept_list_title_text_view.text = getString(R.string.choose_clothes_type)

        val preparedTypes: MutableList<FilterCheckModel> = mutableListOf()

        resultsModel.results.map {
            preparedTypes.add(
                FilterCheckModel(
                    id = it.id,
                    item = it
                )
            )
        }

        adapter.updateList(list = preparedTypes)
        listMode = TYPE_LIST_MODE

        setDoneButtonUnClickable()
        setListButtonsCondition()
    }

    override fun processCategories(resultsModel: ResultsModel<ClothesCategoryModel>) {
        dialog_save_clothes_accept_list_title_text_view.text = getString(R.string.choose_clothes_category)

        val preparedTypes: MutableList<FilterCheckModel> = mutableListOf()

        resultsModel.results.map {
            preparedTypes.add(
                FilterCheckModel(
                    id = it.id,
                    item = it
                )
            )
        }

        adapter.updateList(list = preparedTypes)
        listMode = CATEGORY_LIST_MODE

        setDoneButtonUnClickable()
        setListButtonsCondition()
    }

    override fun processStyles(resultsModel: ResultsModel<ClothesStyleModel>) {
        dialog_save_clothes_accept_list_title_text_view.text = getString(R.string.choose_clothes_style)

        val preparedTypes: MutableList<FilterCheckModel> = mutableListOf()

        resultsModel.results.map {
            preparedTypes.add(
                FilterCheckModel(
                    id = it.id,
                    item = it
                )
            )
        }

        adapter.updateList(list = preparedTypes)
        listMode = STYLE_LIST_MODE

        setListButtonsCondition()
    }

    override fun processSuccessCreating(wardrobeModel: ClothesModel) {
        Log.d("TAG4", "$wardrobeModel")
        listener.onChoice(null, wardrobeModel)
        dismiss()
    }

    private fun onNextClicked() {
        when (listMode) {
            TYPE_LIST_MODE -> presenter.getCategories(
                token = getTokenFromArgs(),
                typeId = clothesCreateModel.clothesType
            )
            CATEGORY_LIST_MODE -> presenter.getStyles(token = getTokenFromArgs())
            STYLE_LIST_MODE -> setListButtonsCondition()
        }
    }

    private fun createClothes() {
        clothesCreateModel.owner = (activity as MainActivity).getUserIdFromSharedPref()
        clothesCreateModel.title = edit_text_view_dialog_save_clothes_accept_sign.text.toString()

        presenter.createClothes(
            token = getTokenFromArgs(),
            clothesCreateModel = clothesCreateModel
        )
    }

    private fun onBackClicked() {
        when (listMode) {
            STYLE_LIST_MODE -> presenter.getCategories(
                token = getTokenFromArgs(),
                typeId = clothesCreateModel.clothesType
            )
            CATEGORY_LIST_MODE -> presenter.getTypes(token = getTokenFromArgs())
            TYPE_LIST_MODE -> setListButtonsCondition()
        }
    }

    private fun setListButtonsCondition() {
        when (listMode) {
            STYLE_LIST_MODE -> {
                hideNextButton()
                showBackButton()
            }
            CATEGORY_LIST_MODE -> {
                hideNextButton()
                showBackButton()
            }
            TYPE_LIST_MODE -> {
                hideNextButton()
                hideBackButton()
            }
        }
    }

    private fun showNextButton() {
        dialog_save_clothes_list_toolbar_next_text_view.show()
        dialog_save_clothes_list_toolbar_next_text_view.isClickable = true
        dialog_save_clothes_list_toolbar_next_text_view.isFocusable = true
    }

    private fun hideNextButton() {
        dialog_save_clothes_list_toolbar_next_text_view.visibility = View.INVISIBLE
        dialog_save_clothes_list_toolbar_next_text_view.isClickable = false
        dialog_save_clothes_list_toolbar_next_text_view.isFocusable = false
    }

    private fun showBackButton() {
        dialog_save_clothes_list_toolbar_back_text_view.show()
        dialog_save_clothes_list_toolbar_back_text_view.isClickable = true
        dialog_save_clothes_list_toolbar_back_text_view.isFocusable = true
    }

    private fun hideBackButton() {
        dialog_save_clothes_list_toolbar_back_text_view.visibility = View.INVISIBLE
        dialog_save_clothes_list_toolbar_back_text_view.isClickable = false
        dialog_save_clothes_list_toolbar_back_text_view.isFocusable = false
    }

    private fun onFilterItemClicked(
        item: FilterCheckModel,
        position: Int
    ) {
        when (item.item) {
            is ClothesTypeModel -> onTypeItemClicked(item.item, position)
            is ClothesCategoryModel -> onCategoryItemClicked(item.item, position)
            is ClothesStyleModel -> onStyleItemClicked(item.item, position)
        }
    }

    private fun onTypeItemClicked(item: ClothesTypeModel, position: Int) {
        adapter.onSingleCheckItem(position)
        clothesCreateModel.clothesType = item.id

        showNextButton()
    }

    private fun onCategoryItemClicked(
        item: ClothesCategoryModel,
        position: Int
    ) {
        adapter.onSingleCheckItem(position)
        clothesCreateModel.clothesCategory = item.id

        showNextButton()
    }

    private fun onStyleItemClicked(item: ClothesStyleModel, position: Int) {
        adapter.onSingleCheckItem(position)
        clothesCreateModel.clothesStyle = item.id

        setDoneButtonClickable()
    }

    private fun setDoneButtonClickable() {
        with (include_toolbar_dialog_save_clothes) {
            toolbar_right_text_text_view.isClickable = true
            toolbar_right_text_text_view.isFocusable = true
            toolbar_right_text_text_view.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_light_orange))
        }
    }

    private fun setDoneButtonUnClickable() {
        with (include_toolbar_dialog_save_clothes) {
            toolbar_right_text_text_view.isClickable = false
            toolbar_right_text_text_view.isFocusable = false
            toolbar_right_text_text_view.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_dark_blue_gray))
        }
    }

    private fun getTokenFromArgs(): String = arguments?.getString(TOKEN_KEY) ?: EMPTY_STRING

    private fun getPhotoBitmapFromArgs(): Bitmap? = arguments?.getParcelable(PHOTO_BITMAP_KEY)

    private fun getPhotoUriFromArgs(): Uri? = arguments?.getParcelable(PHOTO_URI_KEY)
}