package kz.eztech.stylyts.presentation.fragments.clothes

import android.animation.ObjectAnimator
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.fragment_clothes_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.domain.models.user.UserShortModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.common.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.clothes.ClothesDetailContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.dialogs.ClothesSizesBottomDialog
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.presenters.clothes.ClothesDetailPresenter
import kz.eztech.stylyts.presentation.utils.ColorUtils
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

class ClothesDetailFragment : BaseFragment<MainActivity>(), ClothesDetailContract.View,
    View.OnClickListener, DialogChooserListener {

    @Inject
    lateinit var presenter: ClothesDetailPresenter
    private lateinit var chooserDialog: ClothesSizesBottomDialog

    private enum class CART_STATE { NONE, EDIT, DONE }

    private var currentState = CART_STATE.NONE
    private var currentSize: ClothesSizeModel? = null

    private var disposables = CompositeDisposable()

    companion object {
        const val CLOTHES_ID = "clothes_id"
        const val BARCODE_KEY = "barcode_code"
    }

    override fun getLayoutId(): Int = R.layout.fragment_clothes_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_clothes_detail_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener(this@ClothesDetailFragment)

            background = ContextCompat.getDrawable(requireContext(), R.color.toolbar_bg_gray)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {}

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun initializeListeners() {
        fragment_clothes_detail_create_collection_text_view.setOnClickListener(this)

        fragment_clothes_detail_add_to_cart_text_view.setOnClickListener(this)
        fragment_clothes_detail_text_size_frame_layout.setOnClickListener(this)
        fragment_clothes_detail_description_holder_linear_layout.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        getClothes()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragment_clothes_detail_description_holder_linear_layout -> showClothesDescription()
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        item?.let { currentItem ->
            when (currentItem) {
                is ClothesSizeModel -> {
                    currentSize = currentItem
                    fragment_clothes_detail_text_size_text_view.text = currentSize?.size
                }
            }
        }
    }

    override fun processClothes(clothesModel: ClothesModel) {
        fillClothesModel(clothesModel)

        fragment_clothes_detail_add_to_wardrobe_text_view.setOnClickListener {
            presenter.saveClothesToWardrobe(
                token = currentActivity.getTokenFromSharedPref(),
                clothesId = clothesModel.id
            )
        }

        fragment_clothes_detail_text_size_frame_layout.setOnClickListener {
            showClothesSizes(clothesModel)
        }

        fragment_clothes_detail_add_to_cart_text_view.setOnClickListener {
            processState(clothesModel)
        }

        fragment_clothes_detail_create_collection_text_view.setOnClickListener {
            createOutfit(clothesModel)
        }
    }

    override fun disposeRequests() {
        disposables.clear()
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun processSuccessSavedWardrobe() {
        displayMessage(msg = getString(R.string.saved))
    }

    override fun processInsertingCart() {
        CartDialog().show(childFragmentManager, "Cart")
    }

    private fun fillClothesModel(clothesModel: ClothesModel) {
        chooserDialog = ClothesSizesBottomDialog()
        chooserDialog.setChoiceListener(listener = this)

        if (clothesModel.clothesBrand.title.isNotEmpty()) {
            fragment_clothes_detail_brand_title_text_view.text = clothesModel.clothesBrand.title
        } else {
            fragment_clothes_detail_brand_title_text_view.hide()
        }
        fragment_clothes_detail_clothes_title_text_view.text = clothesModel.title
        fragment_clothes_detail_shop_name_text_view.text = clothesModel.owner.username

        processImages(clothesModel)
        processPrice(clothesModel)
        processDescription(clothesModel)

        if (clothesModel.owner.id != 0) {
            processClothesOwner(owner = clothesModel.owner)
        }
    }

    private fun processClothesOwner(owner: UserShortModel) {
        fragment_clothes_detail_desc_author_text_view.text = getString(
            R.string.full_name_text_format,
            owner.firstName,
            owner.lastName
        )

        if (owner.avatar.isBlank()) {
            fragment_clothes_detail_avatar_shapeable_image_view.hide()
            fragment_clothes_detail_user_short_name_text_view.text = getShortName(
                firstName = owner.firstName,
                lastName = owner.lastName
            )
        } else {
            fragment_clothes_detail_user_short_name_text_view.hide()

            Glide.with(fragment_clothes_detail_avatar_shapeable_image_view.context)
                .load(owner.avatar)
                .centerCrop()
                .into(fragment_clothes_detail_avatar_shapeable_image_view)
        }
    }

    private fun processImages(clothesModel: ClothesModel) {
        val imageArray = ArrayList<String>()

        clothesModel.coverImages.map { image ->
            imageArray.add(image)
        }

        val imageAdapter = ImagesViewPagerAdapter(
            imageArray,
            withCenterCrop = false
        )
        fragment_clothes_detail_photos_holder_view_pager.adapter = imageAdapter

        fragment_clothes_detail_photos_pager_indicator.show()
        fragment_clothes_detail_photos_pager_indicator.attachToPager(
            fragment_clothes_detail_photos_holder_view_pager
        )
    }

    private fun processPrice(clothesModel: ClothesModel) {
        if (clothesModel.salePrice != 0) {
            fragment_clothes_detail_price_text_view.hide()
            fragment_clothes_detail_default_price_text_view.apply {
                text = getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(clothesModel.cost)
                )
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            fragment_clothes_detail_sale_price_text_view.text = getString(
                R.string.price_tenge_text_format,
                NumberFormat.getInstance().format(clothesModel.salePrice)
            )
            fragment_clothes_detail_sale_prices_linear_layout.show()
        } else {
            fragment_clothes_detail_sale_prices_linear_layout.hide()

            if (clothesModel.cost != 0) {
                fragment_clothes_detail_price_text_view.text = getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(clothesModel.cost)
                )
            } else {
                fragment_clothes_detail_price_text_view.hide()
            }
        }
    }

    private fun processDescription(clothesModel: ClothesModel) {
        fragment_clothes_detail_desc_text_view.text = clothesModel.description
        fragment_clothes_detail_desc_id_text_view.text = clothesModel.id.toString()
        fragment_clothes_detail_desc_color_text_view.text = ColorUtils.getColorTitleFromHex(
            hex = clothesModel.clothesColor
        )
    }

    private fun getClothes() {
        if (getBarcodeFromArgs().isNotEmpty()) {
            presenter.getClothesByBarcode(
                token = currentActivity.getTokenFromSharedPref(),
                barcode = getBarcodeFromArgs()
            )
        } else {
            presenter.getClothesById(
                currentActivity.getTokenFromSharedPref(),
                clothesId = getClothesIdFromArgs()
            )
        }
    }

    private fun processState(clothesModel: ClothesModel) {
        when (currentState) {
            CART_STATE.NONE -> {
                currentState = CART_STATE.EDIT

                fragment_clothes_detail_create_collection_text_view.hide()
                fragment_clothes_detail_add_to_wardrobe_text_view.hide()
                fragment_clothes_detail_cart_holder_linear_layout.setBackgroundColor(
                    getColor(currentActivity, R.color.app_very_light_gray)
                )
                fragment_clothes_detail_text_size_frame_layout.show()
                fragment_clothes_detail_text_share_linear_layout.show()
            }
            CART_STATE.EDIT -> {
                if (currentSize != null) {
                    processCart(clothesModel)
                } else {
                    displayMessage(msg = getString(R.string.select_size))
                }
            }
            else -> {}
        }
    }

    private fun showClothesSizes(clothesModel: ClothesModel) {
        if (clothesModel.sizeInStock.isNotEmpty()) {
            val bundle = Bundle()
            var counter = 0
            val clothesSizes: MutableList<ClothesSizeModel> = mutableListOf()

            clothesModel.sizeInStock.map { size ->
                clothesSizes.add(size)
                counter++
            }

            bundle.putParcelableArrayList(
                ClothesSizesBottomDialog.SIZES_KEY,
                ArrayList(clothesSizes)
            )

            chooserDialog.arguments = bundle
            chooserDialog.show(parentFragmentManager, "Chooser")
        } else {
            displayMessage(msg = getString(R.string.there_are_not_sizes))
        }
    }

    private fun showClothesDescription() {
        if (fragment_clothes_detail_description_linear_layout.visibility == View.VISIBLE) {
            fragment_clothes_detail_description_linear_layout.hide()
            startObjectAnimator(
                view = fragment_clothes_detail_description_arrow_image_view,
                y = 0.0f
            )

        } else {
            fragment_clothes_detail_description_linear_layout.show()
            startObjectAnimator(
                view = fragment_clothes_detail_description_arrow_image_view,
                y = 180.0f
            )
        }
    }

    private fun startObjectAnimator(view: View, y: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, view.rotation, y)

        objectAnimator.duration = 500
        objectAnimator.start()
    }

    private fun processCart(clothesModel: ClothesModel) {
        clothesModel.selectedSize = currentSize
        presenter.insertToCart(clothesModel = clothesModel)
    }

    private fun createOutfit(clothesModel: ClothesModel) {
        val itemsList = ArrayList<ClothesModel>()
        val bundle = Bundle()

        itemsList.add(clothesModel)
        bundle.putParcelableArrayList(
            CollectionConstructorFragment.CLOTHES_ITEMS_KEY,
            itemsList
        )

        findNavController().navigate(
            R.id.action_clothesDetailFragment_to_nav_create_collection,
            bundle
        )
    }

    private fun getClothesIdFromArgs(): Int = arguments?.getInt(CLOTHES_ID) ?: 0

    private fun getBarcodeFromArgs(): String = arguments?.getString(BARCODE_KEY) ?: EMPTY_STRING
}