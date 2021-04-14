package kz.eztech.stylyts.presentation.fragments.collection

import android.animation.ObjectAnimator
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_item_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.LocalDataSource
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ClothesColor
import kz.eztech.stylyts.domain.models.ClothesSize
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection.ItemDetailContract
import kz.eztech.stylyts.presentation.dialogs.ItemDetailChooserDialog
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.presenters.collection.ItemDetailPresenter
import kz.eztech.stylyts.presentation.utils.ColorUtils
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

class ItemDetailFragment : BaseFragment<MainActivity>(), ItemDetailContract.View,
    View.OnClickListener, DialogChooserListener {

    @Inject lateinit var presenter: ItemDetailPresenter
    @Inject lateinit var ds: LocalDataSource

    private lateinit var chooserDialog: ItemDetailChooserDialog

    private var currentClothesModel: ClothesModel? = null
    private var currentClothesId: Int = -1

    private enum class CART_STATE { NONE, EDIT, DONE }

    private var currentState = CART_STATE.NONE

    private var currentColor: ClothesColor? = null
    private var currentSize: ClothesSize? = null
    private var barCode: String? = null

    private var disposables = CompositeDisposable()

    companion object {
        const val CLOTHES_ID = "clothes_id"
    }

    override fun getLayoutId(): Int = R.layout.fragment_item_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_item_detail) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_left_corner_action_image_button.setOnClickListener(this@ItemDetailFragment)

            background = ContextCompat.getDrawable(requireContext(), R.color.toolbar_bg_gray)
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
            if (it.containsKey("clotheModel")) {
                currentClothesModel = it.getParcelable("clotheModel")
            }

            if (it.containsKey(CLOTHES_ID)) {
                currentClothesId = it.getInt(CLOTHES_ID)
            }

            if (it.containsKey("barcode_code")) {
                barCode = it.getString("barcode_code")
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        currentClothesModel?.let {
            fillClothesModel(clothesModel = it)
        } ?: run {
            getClothes()
        }
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun initializeListeners() {
        button_fragment_item_detail_create_collection.setOnClickListener(this)

        button_fragment_item_detail_add_to_cart.setOnClickListener(this)
        frame_layout_fragment_item_detail_text_size.setOnClickListener(this)
        frame_layout_fragment_item_detail_text_color.setOnClickListener(this)
        linear_layout_fragment_item_detail_description_holder.setOnClickListener(this)
        button_fragment_item_detail_add_to_wardrobe.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_fragment_item_detail_create_collection -> createOutfit()
            R.id.button_fragment_item_detail_add_to_cart -> processState()
            R.id.frame_layout_fragment_item_detail_text_size -> showClothesSizes()
            R.id.frame_layout_fragment_item_detail_text_color -> {
//                showClothesColors()
            }
            R.id.linear_layout_fragment_item_detail_description_holder -> showClothesDescription()
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.button_fragment_item_detail_add_to_wardrobe -> presenter.saveClothesToWardrobe(
                token = getTokenFromSharedPref(),
                clothesId = currentClothesModel?.id ?: 0
            )
        }
    }

    override fun onChoice(v: View?, item: Any?) {
        item?.let { currentItem ->
            when (currentItem) {
                is ClothesSize -> {
                    currentSize = currentItem
                    text_view_fragment_item_detail_text_size.text = currentSize?.size
                }
                is ClothesColor -> {
                    currentColor = currentItem
                    text_view_fragment_item_detail_text_color.text = currentColor?.color
                }
            }
        }
    }

    override fun processClothes(clothesModel: ClothesModel) {
        currentClothesId = -2
        currentClothesModel = clothesModel
        initializeViews()
    }

    override fun processPostInitialization() {}

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

    override fun processClothesOwner(userModel: UserModel) {
        fragment_item_detail_description_author_text_view.text = getString(
            R.string.full_name_text_format,
            userModel.firstName,
            userModel.lastName
        )

        if (userModel.avatar.isBlank()) {
            fragment_detail_avatar_shapeable_image_view.hide()
            fragment_detail_user_short_name_text_view.text = getShortName(
                firstName = userModel.firstName,
                lastName = userModel.lastName
            )
        } else {
            fragment_detail_user_short_name_text_view.hide()

            Glide.with(fragment_detail_avatar_shapeable_image_view.context)
                .load(userModel.avatar)
                .centerCrop()
                .into(fragment_detail_avatar_shapeable_image_view)
        }
    }

    private fun fillClothesModel(clothesModel: ClothesModel) {
        chooserDialog = ItemDetailChooserDialog()
        chooserDialog.setChoiceListener(this)

        text_view_fragment_item_detail_brand_name.text = clothesModel.clothesBrand.title
        text_view_fragment_item_detail_item_name.text = clothesModel.title

        fillImages(clothesModel)
        fillPrice(clothesModel)
        fillDescription(clothesModel)

        presenter.getClothesOwner(
            token = getTokenFromSharedPref(),
            userId = clothesModel.owner.id.toString()
        )
    }

    private fun fillImages(clothesModel: ClothesModel) {
        val imageArray = ArrayList<String>()
        clothesModel.coverImages.map { image ->
            imageArray.add(image)
        }

        val imageAdapter = ImagesViewPagerAdapter(imageArray)
        view_pager_fragment_item_detail_photos_holder.adapter = imageAdapter

        page_indicator_fragment_item_detail_pager_selector.show()
        page_indicator_fragment_item_detail_pager_selector.attachToPager(
            view_pager_fragment_item_detail_photos_holder
        )
    }

    private fun fillPrice(clothesModel: ClothesModel) {
        if (clothesModel.salePrice != 0) {
            text_view_fragment_item_detail_item_price.hide()
            text_view_fragment_item_detail_item_default_price.apply {
                text = getString(
                    R.string.price_tenge_text_format,
                    NumberFormat.getInstance().format(clothesModel.cost)
                )
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            fragment_item_details_sale_price_text_view.text = getString(
                R.string.price_tenge_text_format,
                NumberFormat.getInstance().format(clothesModel.salePrice)
            )
            fragment_item_detail_sale_prices_linear_layout.show()
        } else {
            fragment_item_detail_sale_prices_linear_layout.hide()
            text_view_fragment_item_detail_item_price.text = getString(
                R.string.price_tenge_text_format,
                NumberFormat.getInstance().format(clothesModel.cost)
            )
        }
    }

    private fun fillDescription(clothesModel: ClothesModel) {
        text_view_fragment_item_detail_description.text = clothesModel.description
        fragment_item_detail_description_id_text_view.text = clothesModel.id.toString()
        fragment_item_detail_description_color_text_view.text = ColorUtils.getColorTitleFromHex(
            hex = clothesModel.clothesColor
        )
    }

    private fun getClothes() {
        barCode?.let {
            presenter.getItemByBarcode(
                token = getTokenFromSharedPref(),
                value = it
            )
        } ?: run {
            if (currentClothesId != -1) {
                presenter.getClothesById(
                    token = getTokenFromSharedPref(),
                    clothesId = currentClothesId.toString()
                )
            } else {
                displayMessage(msg = getString(R.string.can_not_load_page_error))
                presenter.getClothesById(
                    getTokenFromSharedPref(),
                    clothesId = 43.toString()
                )
            }
        }
    }

    private fun processState() {
        when (currentState) {
            CART_STATE.NONE -> {
                currentState = CART_STATE.EDIT

                button_fragment_item_detail_create_collection.hide()
                button_fragment_item_detail_add_to_wardrobe.hide()
                linear_layout_fragment_item_detail_cart_holder.setBackgroundColor(
                    getColor(currentActivity, R.color.app_very_light_gray)
                )
                linear_layout_fragment_item_detail_choosers_holder.show()
                frame_layout_fragment_item_detail_text_share.show()
            }
            CART_STATE.EDIT -> {
                if (currentColor == null || currentSize == null) {
                    displayMessage(msg = getString(R.string.add_to_cart_error))
                } else {
                    processCart()
                }
            }
            else -> {}
        }
    }

    private fun showClothesSizes() {
        val bundle = Bundle()
        currentClothesModel?.sizeInStock?.let {
            var counter = 0
            val clothesSizes: MutableList<ClothesSize> = mutableListOf()
            it.map { size ->
                clothesSizes.add(ClothesSize(id = counter, size = size.size))
                counter++
            }

            bundle.putParcelableArrayList("sizeItems", ArrayList(clothesSizes))
        } ?: run {
            displayMessage(msg = getString(R.string.there_are_not_sizes))
        }
        chooserDialog.arguments = bundle
        chooserDialog.show(parentFragmentManager, "Chooser")
    }

    private fun showClothesColors() {
        val bundle = Bundle()
        currentClothesModel?.clothesColor?.let {
            var counter = 0
            val clothesColors: MutableList<ClothesColor> = mutableListOf()
            it.map { color ->
//                clothesColors.add(ClothesColor(id = counter, color = color))
                counter++
            }

            bundle.putParcelableArrayList("colorItems", ArrayList(clothesColors))
        } ?: run {
            displayMessage(msg = getString(R.string.there_are_not_colors))
        }
        chooserDialog.arguments = bundle
        chooserDialog.show(parentFragmentManager, "Chooser")
    }

    private fun showClothesDescription() {
        if (fragment_item_detail_description_linear_layout.visibility == View.VISIBLE) {
            fragment_item_detail_description_linear_layout.hide()
            startObjectAnimator(
                view = image_view_fragment_item_detail_description_arrow,
                y = 0.0f
            )

        } else {
            fragment_item_detail_description_linear_layout.show()
            startObjectAnimator(
                view = image_view_fragment_item_detail_description_arrow,
                y = 180.0f
            )
        }
    }

    private fun startObjectAnimator(view: View, y: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, view.rotation, y)

        objectAnimator.duration = 500
        objectAnimator.start()
    }

    private fun processCart() {
//        currentClothesModel?.currentColor = currentColor
//        currentClothesModel?.currentSize = currentSize
//        disposables.clear()
//        disposables.add(
//            ds.insert(CartMapper.mapToEntity(currentClothesModel as ClothesModel))
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
//                    val cartDialog = CartDialog()
//                    cartDialog.show(childFragmentManager, "Cart")
//                }
//        )
    }

    private fun createOutfit() {
        currentClothesModel?.let {
            val itemsList = ArrayList<ClothesModel>()
            val bundle = Bundle()

            itemsList.add(it)
            bundle.putParcelableArrayList(CollectionConstructorFragment.CLOTHES_ITEMS_KEY, itemsList)

            findNavController().navigate(
                R.id.action_itemDetailFragment_to_createCollectionFragment,
                bundle
            )
        }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}