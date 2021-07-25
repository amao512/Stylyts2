package kz.eztech.stylyts.presentation.dialogs.collection_constructor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_save_clothes_accept.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.clothes.*
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.filter.FilterCheckAdapter
import kz.eztech.stylyts.presentation.base.BaseActivity
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection_constructor.SaveClothesAcceptContract
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection_constructor.SaveClothesAcceptPresenter
import kz.eztech.stylyts.presentation.utils.FileUtils
import kz.eztech.stylyts.presentation.utils.Paginator
import kz.eztech.stylyts.presentation.utils.extensions.displaySnackBar
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImage
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

        const val TYPE_LIST_MODE = 0
        const val CATEGORY_LIST_MODE = 1
        const val STYLE_LIST_MODE = 2
        const val BRAND_LIST_MODE = 3
        const val COST_MODE = 4

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
        getPhotoUriFromArgs()?.loadImage(target = image_view_dialog_save_clothes_accept)
        getPhotoBitmapFromArgs()?.loadImage(target = image_view_dialog_save_clothes_accept)

        presenter.getList()
        handleRecyclerView()
    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        view?.let {
            displaySnackBar(it.context, it, msg)
        }
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        dialog_save_clothes_progress_bar.show()
    }

    override fun hideProgress() {
        dialog_save_clothes_progress_bar.hide()
    }

    override fun displaySmallProgress() {
        dialog_save_clothes_small_progress_bar.show()
        dialog_save_clothes_accept_list_toolbar.hide()
        dialog_save_clothes_recycler_view.hide()
    }

    override fun hideSmallProgress() {
        dialog_save_clothes_small_progress_bar.hide()
        dialog_save_clothes_accept_list_toolbar.show()
        dialog_save_clothes_recycler_view.show()
    }

    override fun getCurrentMode(): Int = listMode

    override fun getClothesCreateModel(): ClothesCreateModel = clothesCreateModel

    override fun renderPaginatorState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Data<*> -> processList(state.data)
            is Paginator.State.NewPageProgress<*> -> processList(state.data)
            else -> hideSmallProgress()
        }
    }

    override fun processList(list: List<Any?>) {
        list.map { it!! }.let {
            when (it[0]) {
                is ClothesTypeModel -> processTypes(it)
                is ClothesCategoryModel -> processCategories(it)
                is ClothesStyleModel -> processStyles(it)
                is ClothesBrandModel -> processBrands(it)
            }
        }

        hideSmallProgress()
    }

    override fun processTypes(list: List<Any>) {
        dialog_save_clothes_accept_list_title_text_view.text =
            getString(R.string.choose_clothes_type)

        val preparedList: MutableList<FilterCheckModel> = mutableListOf()

        list.map {
            it as ClothesTypeModel

            preparedList.add(
                FilterCheckModel(id = it.id, item = it)
            )
        }

        adapter.updateList(list = preparedList)

        setDoneButtonUnClickable()
        setListButtonsCondition()
    }

    override fun processCategories(list: List<Any>) {
        dialog_save_clothes_accept_list_title_text_view.text =
            getString(R.string.choose_clothes_category)

        val preparedList: MutableList<FilterCheckModel> = mutableListOf()

        list.map {
            it as ClothesCategoryModel

            preparedList.add(
                FilterCheckModel(id = it.id, item = it)
            )
        }

        adapter.updateList(list = preparedList)

        setDoneButtonUnClickable()
        setListButtonsCondition()
    }

    override fun processStyles(list: List<Any>) {
        dialog_save_clothes_accept_list_title_text_view.text =
            getString(R.string.choose_clothes_style)

        val preparedList: MutableList<FilterCheckModel> = mutableListOf()

        list.map {
            it as ClothesStyleModel

            preparedList.add(
                FilterCheckModel(id = it.id, item = it)
            )
        }

        adapter.updateList(list = preparedList)
        setListButtonsCondition()
    }

    override fun processBrands(list: List<Any>) {
        dialog_save_clothes_accept_list_title_text_view.text =
            getString(R.string.choose_clothes_brand)
        dialog_save_clothes_accept_list_title_text_view.show()
        dialog_save_clothes_accept_price_holder_linear_layout.hide()
        dialog_save_clothes_recycler_view.show()

        val preparedList: MutableList<FilterCheckModel> = mutableListOf()

        list.map {
            it as ClothesBrandModel

            preparedList.add(
                FilterCheckModel(id = it.id, item = it)
            )
        }

        adapter.updateList(list = preparedList)
        setListButtonsCondition()
    }

    override fun processSuccessCreating(wardrobeModel: ClothesModel) {
        val bundle = Bundle()
        bundle.putInt(ProfileFragment.MODE_KEY, ProfileFragment.WARDROBE_MODE)

        findNavController().navigate(R.id.nav_profile, bundle)

        dismiss()
    }

    private fun handleRecyclerView() {
        dialog_save_clothes_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!dialog_save_clothes_recycler_view.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    presenter.loadMorePage()
                }
            }
        })
    }

    private fun onNextClicked() {
        when (listMode) {
            TYPE_LIST_MODE -> {
                listMode = CATEGORY_LIST_MODE
                presenter.getList()
            }
            CATEGORY_LIST_MODE -> {
                listMode = STYLE_LIST_MODE
                presenter.getList()
            }
            STYLE_LIST_MODE -> {
                listMode = BRAND_LIST_MODE
                presenter.getList()
            }
            BRAND_LIST_MODE -> {
                listMode = COST_MODE
                presenter.getList()
            }
            COST_MODE -> handleCostEditText()
        }
    }

    private fun createClothes() {
        clothesCreateModel.owner = (activity as MainActivity).getUserIdFromSharedPref()
        clothesCreateModel.title = edit_text_view_dialog_save_clothes_accept_sign.text.toString()

        presenter.createClothes()
    }

    private fun onBackClicked() {
        listMode = when (listMode) {
            COST_MODE -> BRAND_LIST_MODE
            BRAND_LIST_MODE -> STYLE_LIST_MODE
            STYLE_LIST_MODE -> CATEGORY_LIST_MODE
            CATEGORY_LIST_MODE -> {
                setListButtonsCondition()
                TYPE_LIST_MODE
            }
            else -> TYPE_LIST_MODE
        }

        presenter.getList()
    }

    private fun setListButtonsCondition() {
        hideNextButton()
        setDoneButtonUnClickable()

        when (listMode) {
            TYPE_LIST_MODE -> hideBackButton()
            else -> showBackButton()
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
            is ClothesBrandModel -> onBrandItemClicked(item.item, position)
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

        if ((activity as BaseActivity).getIsBrandFromSharedPref()) {
            showNextButton()
        } else {
            setDoneButtonClickable()
        }
    }

    private fun onBrandItemClicked(brand: ClothesBrandModel, position: Int) {
        adapter.onSingleCheckItem(position)
        clothesCreateModel.clothesBrand = brand.id
        listMode = COST_MODE

        showNextButton()
        setDoneButtonClickable()
    }

    private fun handleCostEditText() {
        listMode = COST_MODE

        dialog_save_clothes_accept_list_title_text_view.visibility = View.INVISIBLE
        val costEditText = dialog_save_clothes_accept_price_edit_text
        val salePriceEditText = dialog_save_clothes_accept_sale_price_edit_text

        dialog_save_clothes_recycler_view.hide()
        dialog_save_clothes_accept_price_holder_linear_layout.show()
        setListButtonsCondition()

        costEditText.setOnKeyListener { _, _, _ ->
            if (costEditText.text.isNotBlank()) {
                clothesCreateModel.cost = costEditText.text.toString().toInt()
                setDoneButtonClickable()
            } else {
                clothesCreateModel.cost = 0
                setDoneButtonUnClickable()
            }

            false
        }

        salePriceEditText.setOnKeyListener { _, _, _ ->
            if (salePriceEditText.text.isNotBlank()) {
                clothesCreateModel.salePrice = salePriceEditText.text.toString().toInt()
            } else {
                clothesCreateModel.salePrice = 0
            }

            false
        }
    }

    private fun setDoneButtonClickable() {
        with(include_toolbar_dialog_save_clothes) {
            toolbar_right_text_text_view.isClickable = true
            toolbar_right_text_text_view.isFocusable = true
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.app_light_orange
                )
            )
        }
    }

    private fun setDoneButtonUnClickable() {
        with(include_toolbar_dialog_save_clothes) {
            toolbar_right_text_text_view.isClickable = false
            toolbar_right_text_text_view.isFocusable = false
            toolbar_right_text_text_view.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.app_dark_blue_gray
                )
            )
        }
    }

    private fun getPhotoBitmapFromArgs(): Bitmap? = arguments?.getParcelable(PHOTO_BITMAP_KEY)

    private fun getPhotoUriFromArgs(): Uri? = arguments?.getParcelable(PHOTO_URI_KEY)
}