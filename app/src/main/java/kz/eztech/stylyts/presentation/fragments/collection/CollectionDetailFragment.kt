package kz.eztech.stylyts.presentation.fragments.collection

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.collection_constructor.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.base.DialogChooserListener
import kz.eztech.stylyts.presentation.contracts.collection.CollectionDetailContract
import kz.eztech.stylyts.presentation.dialogs.collection.CollectionContextDialog
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.presentation.presenters.collection.CollectionDetailPresenter
import kz.eztech.stylyts.presentation.utils.DateFormatterHelper
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.NumberFormat
import javax.inject.Inject

class CollectionDetailFragment : BaseFragment<MainActivity>(), CollectionDetailContract.View,
    UniversalViewClickListener, View.OnClickListener, DialogChooserListener {

    @Inject lateinit var presenter: CollectionDetailPresenter
    private lateinit var additionalAdapter: MainImagesAdditionalAdapter

    private var currentId: Int = 0
    private var currentMode: Int = OUTFIT_MODE

    companion object {
        const val ID_KEY = "outfit_id"
        const val MODE_KEY = "mode_key"
        const val OUTFIT_MODE = 0
        const val POST_MODE = 1
    }

    override fun getLayoutId(): Int = R.layout.fragment_collection_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_collection_detail) {
            toolbar_title_text_view.show()
            toolbar_title_text_view.text = context.getString(R.string.collection_detail_fragment_publishes)

            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@CollectionDetailFragment)
            toolbar_left_corner_action_image_button.show()
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(ID_KEY)) {
                currentId = it.getInt(ID_KEY)
            }

            if (it.containsKey(MODE_KEY)) {
                currentMode = it.getInt(MODE_KEY)
            }
        }
    }

    override fun initializeViewsData() {
        additionalAdapter = MainImagesAdditionalAdapter()
        additionalAdapter.itemClickListener = this
    }

    override fun initializeViews() {
        recycler_view_fragment_collection_detail_additionals_list.adapter = additionalAdapter
        processPublication()
    }

    override fun initializeListeners() {
        text_view_fragment_collection_detail_comments_count.setOnClickListener(this)
        button_fragment_collection_detail_change_collection.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        when (currentMode) {
            OUTFIT_MODE -> presenter.getOutfitById(
                token = getTokenFromSharedPref(),
                outfitId = currentId.toString()
            )
            POST_MODE -> presenter.getPostById(
                token = getTokenFromSharedPref(),
                postId = currentId
            )
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.frame_layout_item_main_image_holder_container -> {
                item as ClothesModel

                val bundle = Bundle()
                bundle.putInt(ClothesDetailFragment.CLOTHES_ID, item.id)

                findNavController().navigate(
                    R.id.action_collectionDetailFragment_to_itemDetailFragment,
                    bundle
                )
            }
            R.id.button_fragment_collection_detail_change_collection -> {
//                currentModel.clothes?.let {
//                    val itemsList = ArrayList<ClothesTypeDataModel>()
//                    val bundle = Bundle()
//                    it.forEach { clothe ->
//                        itemsList.add(
//                            ClothesTypeDataModel(
//                                id = clothe.id,
//                                title = clothe.title,
//                                cover_photo = clothe.cover_photo,
//                                cost = clothe.cost,
//                                sale_cost = clothe.sale_cost,
//                                currency = clothe.currency,
//                                clothes_types = clothe.clothes_type,
//                                gender = clothe.gender,
//                                constructor_photo = clothe.constructor_photo,
//                                isLocated = true,
//
//                                )
//                        )
//                    }
//                    bundle.putParcelableArrayList("items", itemsList)
//                    findNavController().navigate(R.id.createCollectionFragment, bundle)
//                } ?: run {
//                    findNavController().navigate(R.id.createCollectionFragment)
//                }
            }
            R.id.constraint_layout_fragment_collection_detail_profile_container -> {
                val bundle = Bundle()
                findNavController().navigate(R.id.partnerProfileFragment, bundle)
            }

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_view_fragment_collection_detail_comments_count -> {
                findNavController().navigate(R.id.userCommentsFragment)
            }
            R.id.button_fragment_collection_detail_change_collection -> onChangeCollection()
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
        }

    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {
        fragment_collection_detail_progress_bar.show()
    }

    override fun hideProgress() {
        fragment_collection_detail_progress_bar.hide()
    }

    override fun onChoice(v: View?, item: Any?) {
        when (v?.id) {
            R.id.dialog_bottom_collection_context_delete_text_view -> {
            }
        }
    }

    override fun processOutfit(outfitModel: OutfitModel) {
        additionalAdapter.updateList(list = outfitModel.clothes)
        presenter.getOwner(
            token = getTokenFromSharedPref(),
            userId = outfitModel.author.id.toString()
        )

        Glide.with(image_view_fragment_collection_detail_imageholder.context)
            .load(outfitModel.coverPhoto)
            .into(image_view_fragment_collection_detail_imageholder)

        text_view_fragment_collection_detail_comments_cost.text = getString(
            R.string.price_tenge_text_format,
            NumberFormat.getInstance().format(outfitModel.totalPrice)
        )
        text_view_fragment_collection_detail_comments_count.text = "Показать ${0} коммент."
        text_view_fragment_collection_detail_date.text = DateFormatterHelper.formatISO_8601(
            outfitModel.createdAt,
            DateFormatterHelper.FORMAT_DATE_DD_MMMM
        )
    }

    override fun processPost(postModel: PostModel) {
        presenter.getOwner(
            token = getTokenFromSharedPref(),
            userId = postModel.author.toString()
        )
        presenter.getPostClothesByTag(
            token = getTokenFromSharedPref(),
            clothesTag = postModel.tags.clothesTags
        )

        Glide.with(image_view_fragment_collection_detail_imageholder.context)
            .load(postModel.images[0])
            .into(image_view_fragment_collection_detail_imageholder)
    }

    override fun processOwner(userModel: UserModel) {
        text_view_fragment_collection_detail_partner_name.text = getString(
            R.string.full_name_text_format,
            userModel.firstName,
            userModel.lastName
        )

        if (userModel.avatar.isBlank()) {
            shapeable_image_view_fragment_collection_detail_profile_avatar.hide()
            text_view_text_view_fragment_collection_detail_short_name.show()
            text_view_text_view_fragment_collection_detail_short_name.text = getShortName(
                firstName = userModel.firstName,
                lastName = userModel.lastName
            )
        } else {
            text_view_text_view_fragment_collection_detail_short_name.hide()

            Glide.with(shapeable_image_view_fragment_collection_detail_profile_avatar.context)
                .load(userModel.avatar)
                .centerCrop()
                .into(shapeable_image_view_fragment_collection_detail_profile_avatar)
        }
    }

    override fun processPostClothes(results: List<ClothesModel>) {
        additionalAdapter.updateList(list = results)
    }

    private fun processPublication() {
        processCollectionListeners()
    }

    private fun processCollectionListeners() {
        constraint_layout_fragment_collection_detail_profile_container.setOnClickListener {
//            adapter.itemClickListener?.onViewClicked(thisView, position, item)
        }
        button_fragment_collection_detail_change_collection.setOnClickListener {
            //adapter.itemClickListener?.onViewClicked(thisView,position,item)
        }
        imageButton.setOnClickListener {
            CollectionContextDialog().apply {
                setChoiceListener(listener = this@CollectionDetailFragment)
            }.show(childFragmentManager, EMPTY_STRING)
        }
    }

    private fun onChangeCollection() {
//        currentModel.clothes?.let {
//                    val itemsList = ArrayList<ClothesMainModel>()
//                    itemsList.addAll(it)
//                    val bundle = Bundle()
//                    bundle.putParcelableArrayList("items", itemsList)
//                    bundle.putInt("mainId", currentModel.id ?: 0)
//                    findNavController().navigate(R.id.createCollectionFragment, bundle)
//                } ?: run {
//                    findNavController().navigate(R.id.createCollectionFragment)
//                }
    }

    private fun getTokenFromSharedPref(): String {
        return currentActivity.getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }
}