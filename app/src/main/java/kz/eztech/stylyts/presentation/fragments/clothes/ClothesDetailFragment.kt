package kz.eztech.stylyts.presentation.fragments.clothes

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
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
import kz.eztech.stylyts.presentation.dialogs.cart.ClothesSizesBottomDialog
import kz.eztech.stylyts.presentation.dialogs.clothes.AddToCartProblemDialog
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.fragments.shop.ShopProfileFragment
import kz.eztech.stylyts.presentation.presenters.clothes.ClothesDetailPresenter
import kz.eztech.stylyts.presentation.utils.ColorUtils
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImageWithCenterCrop
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class ClothesDetailFragment : BaseFragment<MainActivity>(), ClothesDetailContract.View,
    View.OnClickListener, DialogChooserListener {

    @Inject lateinit var presenter: ClothesDetailPresenter
    private lateinit var chooserDialog: ClothesSizesBottomDialog

    private enum class CART_STATE { NONE, EDIT, DONE }

    private var currentState = CART_STATE.NONE
    private var currentSize: ClothesSizeModel? = null

    companion object {
        const val CLOTHES_ID = "clothes_id"
        const val BARCODE_KEY = "barcode_code"
        const val DELETED_STATE_KEY = "deletedState"
        const val INFLUENCER_ID_KEY = "influencerId"
    }

    override fun getLayoutId(): Int = R.layout.fragment_clothes_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_clothes_detail_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setOnClickListener(this@ClothesDetailFragment)

            customizeActionToolBar(toolbar = this, title = EMPTY_STRING)

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
            R.id.toolbar_right_corner_action_image_button -> findNavController().navigate(R.id.action_clothesDetailFragment_to_nav_ordering)
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

        fragment_clothes_detail_toolbar.toolbar_title_text_view.text = clothesModel.owner.username
        fragment_clothes_detail_toolbar.toolbar_title_text_view.textSize = 14f
        fragment_clothes_detail_toolbar.toolbar_title_text_view.show()

        fragment_clothes_detail_add_to_wardrobe_text_view.setOnClickListener {
            presenter.saveClothesToWardrobe(clothesId = clothesModel.id)
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

        fragment_clothes_detail_avatar_holder.setOnClickListener {
            navigateToProfile(user = clothesModel.owner)
        }

        fragment_clothes_detail_shop_name_text_view.setOnClickListener {
            navigateToProfile(user = clothesModel.owner)
        }

        fragment_clothes_detail_delete_text_view.setOnClickListener {
            presenter.deleteClothes(clothesId = clothesModel.id)
        }
    }

    override fun disposeRequests() {
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
        findNavController().navigate(R.id.nav_ordering)
    }

    override fun processDeleting() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(DELETED_STATE_KEY, "success")
        findNavController().popBackStack()
    }

    private fun fillClothesModel(clothesModel: ClothesModel) = with (clothesModel) {
        chooserDialog = ClothesSizesBottomDialog()
        chooserDialog.setChoiceListener(listener = this@ClothesDetailFragment)

        if (owner.username.isNotEmpty()) {
            fragment_clothes_detail_brand_title_text_view.text = owner.username
        } else {
            fragment_clothes_detail_brand_title_text_view.hide()
        }

        if (!owner.isBrand) {
            fragment_clothes_detail_add_to_cart_text_view.backgroundTintList = ColorStateList.valueOf(
                getColor(requireContext(), R.color.disabled_button_color)
            )
        }

        if (owner.id == currentActivity.getUserIdFromSharedPref() && !owner.isBrand) {
            fragment_clothes_detail_delete_text_view.show()
            fragment_clothes_detail_add_to_wardrobe_text_view.hide()
        } else {
            fragment_clothes_detail_delete_text_view.hide()
            fragment_clothes_detail_add_to_wardrobe_text_view.show()
        }

        fragment_clothes_detail_clothes_title_text_view.text = title
        fragment_clothes_detail_shop_name_text_view.text = owner.username

        processImages(clothesModel)
        processPrice(clothesModel)
        processDescription(clothesModel)

        if (owner.id != 0) {
            processClothesOwner(owner = owner)
        }
    }

    private fun processClothesOwner(owner: UserShortModel) = with (owner) {
        fragment_clothes_detail_desc_author_text_view.text = displayFullName

        if (avatar.isBlank()) {
            fragment_clothes_detail_avatar_shapeable_image_view.hide()
            fragment_clothes_detail_user_short_name_text_view.text = displayShortName
        } else {
            fragment_clothes_detail_user_short_name_text_view.hide()

            avatar.loadImageWithCenterCrop(target = fragment_clothes_detail_avatar_shapeable_image_view)
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

    private fun processPrice(clothesModel: ClothesModel) = with (clothesModel) {
        if (salePrice != 0) {
            fragment_clothes_detail_price_text_view.hide()
            fragment_clothes_detail_default_price_text_view.apply {
                text = displayPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            fragment_clothes_detail_sale_price_text_view.text = displaySalePrice
            fragment_clothes_detail_sale_prices_linear_layout.show()
        } else {
            fragment_clothes_detail_sale_prices_linear_layout.hide()

            if (cost != 0) {
                fragment_clothes_detail_price_text_view.text = displayPrice
            } else {
                fragment_clothes_detail_price_text_view.hide()
            }
        }
    }

    private fun processDescription(clothesModel: ClothesModel) = with (clothesModel) {
        fragment_clothes_detail_desc_text_view.text = description
        fragment_clothes_detail_desc_id_text_view.text = id.toString()
        fragment_clothes_detail_desc_color_text_view.text = ColorUtils.getColorTitleFromHex(hex = clothesColor)
    }

    private fun getClothes() {
        if (getBarcodeFromArgs().isNotEmpty()) {
            presenter.getClothesByBarcode(barcode = getBarcodeFromArgs())
        } else {
            presenter.getClothesById(clothesId = getClothesIdFromArgs())
        }
    }

    private fun processState(clothesModel: ClothesModel) {
        if (clothesModel.owner.isBrand) {
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
//                    if (currentSize != null) {
                        processCart(clothesModel)
//                    } else {
//                        displayMessage(msg = getString(R.string.select_size))
//                    }
                }
                else -> {}
            }
        } else {
            AddToCartProblemDialog.getNewInstance(username = clothesModel.owner.username)
                .show(childFragmentManager, EMPTY_STRING)
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

        val influencerId = getInfluencerIdFromArgs()
        if (influencerId != 0) {
            clothesModel.referralUser = influencerId
        }

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

    private fun navigateToProfile(user: UserShortModel) {
        val bundle = Bundle()

        if (user.isBrand && user.id != currentActivity.getUserIdFromSharedPref()) {
            bundle.putInt(ShopProfileFragment.PROFILE_ID_KEY, user.id)
            findNavController().navigate(R.id.nav_shop_profile, bundle)
        } else {
            bundle.putInt(ProfileFragment.USER_ID_BUNDLE_KEY, user.id)
            findNavController().navigate(R.id.nav_profile, bundle)
        }
    }

    private fun getClothesIdFromArgs(): Int = arguments?.getInt(CLOTHES_ID) ?: 0

    private fun getBarcodeFromArgs(): String = arguments?.getString(BARCODE_KEY) ?: EMPTY_STRING

    private fun getInfluencerIdFromArgs(): Int = arguments?.getInt(INFLUENCER_ID_KEY) ?: 0
}