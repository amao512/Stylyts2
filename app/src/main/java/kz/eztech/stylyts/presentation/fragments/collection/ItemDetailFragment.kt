package kz.eztech.stylyts.presentation.fragments.collection

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_item_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.LocalDataSource
import kz.eztech.stylyts.data.db.entities.CartMapper
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.ClothesColor
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.models.ClothesSize
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.ImagesViewPagerAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection.ItemDetailContract
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.dialogs.ItemDetailChooserDialog
import kz.eztech.stylyts.presentation.presenters.collection.ItemDetailPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
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
    private var currentClotheId: Int = -1

    private enum class CART_STATE { NONE, EDIT, DONE }

    private var currentState = CART_STATE.NONE

    private var currentColor: ClothesColor? = null
    private var currentSize: ClothesSize? = null
    private var barCode: String? = null

    private var disposables = CompositeDisposable()

    companion object {
        const val CLOTHE_ID = "clothe_id"
    }

    override fun getLayoutId(): Int = R.layout.fragment_item_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_item_detail) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_title_text_view.show()

            toolbar_right_corner_action_image_button.show()
            toolbar_right_corner_action_image_button.setImageResource(R.drawable.ic_shop)
            toolbar_right_corner_action_image_button.setOnClickListener {
                val cartDialog = CartDialog()
                cartDialog.show(childFragmentManager, "Cart")
            }

            customizeActionToolBar(toolbar = this, title = "zara")
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

            if (it.containsKey(CLOTHE_ID)) {
                currentClotheId = it.getInt(CLOTHE_ID)
            }

            if (it.containsKey("barcode_code")) {
                barCode = it.getString("barcode_code")
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        currentClothesModel?.let {
            chooserDialog = ItemDetailChooserDialog()
            chooserDialog.setChoiceListener(this)

            val imageArray = ArrayList<String>()
            it.images?.map { clothesImage ->
                imageArray.add(clothesImage.image ?: EMPTY_STRING)
            }

            val imageAdapter = ImagesViewPagerAdapter(imageArray)
            view_pager_fragment_item_detail_photos_holder.adapter = imageAdapter

            page_indicator_fragment_item_detail_pager_selector.show()
            page_indicator_fragment_item_detail_pager_selector.attachToPager(
                view_pager_fragment_item_detail_photos_holder
            )


//            text_view_fragment_item_detail_brand_name.text =
//                "${it.brand?.first_name} ${it.brand?.last_name}"
            text_view_fragment_item_detail_item_name.text = it.title
            text_view_fragment_item_detail_item_price.text =
                "${NumberFormat.getInstance().format(it.cost)} ${it.currency}"

            text_view_fragment_item_detail_description.text = it.description


        } ?: run {
            barCode?.let {
                presenter.getItemByBarcode(
                    token = getTokenFromSharedPref(),
                    value = it
                )
            } ?: run {
                if (currentClotheId != -1) {
                    presenter.getClothesById(
                        token = getTokenFromSharedPref(),
                        clothesId = currentClotheId.toString()
                    )
                } else {
                    displayMessage("Не удалось прогрузить страницу")
                    presenter.getClothesById(
                        getTokenFromSharedPref(),
                        clothesId = 43.toString()
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        currentActivity.displayBottomNavigationView()
    }

    override fun initializeListeners() {
        button_fragment_item_detail_create_collection.setOnClickListener(this)

        button_fragment_item_detail_add_to_cart.setOnClickListener(this)
        frame_layout_fragment_item_detail_text_size.setOnClickListener(this)
        frame_layout_fragment_item_detail_text_color.setOnClickListener(this)
        linear_layout_fragment_item_detail_description_holder.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_fragment_item_detail_create_collection -> createOutfit()
            R.id.button_fragment_item_detail_add_to_cart -> processState()
            R.id.frame_layout_fragment_item_detail_text_size -> {
                val bundle = Bundle()
                currentClothesModel?.clothesSizes?.let {
                    bundle.putStringArrayList("sizeItems", ArrayList(it))
                } ?: run {
                    displayMessage("Нет размеров")
                }
                chooserDialog.arguments = bundle
                chooserDialog.show(parentFragmentManager, "Chooser")
            }
            R.id.frame_layout_fragment_item_detail_text_color -> {
                val bundle = Bundle()
                currentClothesModel?.clothesColors?.let {
                    bundle.putStringArrayList("colorItems", ArrayList(it))
                } ?: run {
                    displayMessage("Нет цветов")
                }
                chooserDialog.arguments = bundle
                chooserDialog.show(parentFragmentManager, "Chooser")
            }
            R.id.linear_layout_fragment_item_detail_description_holder -> {
                if (text_view_fragment_item_detail_description.visibility == View.VISIBLE) {
                    text_view_fragment_item_detail_description.visibility = View.GONE
                    val objectAnimator = ObjectAnimator.ofFloat(
                        image_view_fragment_item_detail_description_arrow, View.ROTATION,
                        image_view_fragment_item_detail_description_arrow.rotation, 0.0f
                    )
                    objectAnimator.duration = 500
                    objectAnimator.start()

                } else {
                    text_view_fragment_item_detail_description.visibility = View.VISIBLE
                    val objectAnimator = ObjectAnimator.ofFloat(
                        image_view_fragment_item_detail_description_arrow,
                        View.ROTATION,
                        image_view_fragment_item_detail_description_arrow.rotation, 180.0f
                    )
                    objectAnimator.duration = 500
                    objectAnimator.start()
                }
            }
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

    private fun processState() {
        when (currentState) {
            CART_STATE.NONE -> {
                currentState = CART_STATE.EDIT
                linear_layout_fragment_item_detail_cart_holder.setBackgroundColor(
                    getColor(
                        currentActivity,
                        R.color.app_very_light_gray
                    )
                )
                linear_layout_fragment_item_detail_choosers_holder.visibility = View.VISIBLE
                frame_layout_fragment_item_detail_text_share.visibility = View.VISIBLE
            }
            CART_STATE.EDIT -> {
                if (currentColor == null || currentSize == null) {
                    displayMessage("Вы не выбрали цвет или размер")
                } else {
//                    processCart()
                }
            }
        }
    }

    override fun processClothes(clothesModel: ClothesModel) {
        currentClotheId = -2
        currentClothesModel = clothesModel
        initializeViews()
    }

    private fun processCart() {
//        currentClothesModel?.currentColor = currentColor
//        currentClothesModel?.currentSize = currentSize
        disposables.clear()
        disposables.add(
            ds.insert(CartMapper.mapToEntity(currentClothesModel as ClothesMainModel))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    val cartDialog = CartDialog()
                    cartDialog.show(childFragmentManager, "Cart")
                }
        )

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

    private fun createOutfit() {
        currentClothesModel?.let {
            val itemsList = ArrayList<ClothesModel>()
            val bundle = Bundle()
            itemsList.add(it)
            bundle.putParcelableArrayList("items", itemsList)
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