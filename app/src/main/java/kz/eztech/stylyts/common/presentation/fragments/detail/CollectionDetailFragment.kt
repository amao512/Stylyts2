package kz.eztech.stylyts.common.presentation.fragments.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_collection_detail.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.ClothesMainModel
import kz.eztech.stylyts.create_outfit.domain.models.ClothesTypeDataModel
import kz.eztech.stylyts.common.domain.models.MainResult
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.create_outfit.presentation.adapters.MainImagesAdditionalAdapter
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.contracts.main.detail.CollectionDetailContract
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.common.presentation.utils.DateFormatterHelper
import kz.eztech.stylyts.common.presentation.utils.extensions.getShortName
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show
import java.text.NumberFormat

class CollectionDetailFragment : BaseFragment<MainActivity>(), CollectionDetailContract.View,
    UniversalViewClickListener, View.OnClickListener {

    private lateinit var currentModel: MainResult
    private lateinit var additionalAdapter: MainImagesAdditionalAdapter

    override fun getLayoutId(): Int = R.layout.fragment_collection_detail

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_collection_detail) {
            toolbar_left_corner_action_image_button.hide()
            toolbar_back_text_view.show()
            toolbar_title_text_view.show()
            toolbar_right_corner_action_image_button.hide()
            toolbar_right_text_text_view.hide()

            customizeActionToolBar(
                toolbar = this,
                title = context.getString(R.string.collection_detail_fragment_publishes)
            )
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey("model")) {
                currentModel = it.getParcelable("model") ?: MainResult()
            }
        }
    }

    override fun initializeViewsData() {
        additionalAdapter = MainImagesAdditionalAdapter()
        additionalAdapter.itemClickListener = this
    }

    override fun initializeViews() {
        this.recycler_view_fragment_collection_detail_additionals_list.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.recycler_view_fragment_collection_detail_additionals_list.adapter = additionalAdapter

        with(currentModel) {
            clothes?.let {
                additionalAdapter.updateList(it)
            }
            text_view_fragment_collection_detail_partner_name.text =
                "${author?.first_name} ${author?.last_name}"

            author?.avatar?.let {
                text_view_text_view_fragment_collection_detail_short_name.visibility = View.GONE
                Glide.with(requireContext()).load(it)
                    .into(shapeable_image_view_fragment_collection_detail_profile_avatar)
            } ?: run {
                shapeable_image_view_fragment_collection_detail_profile_avatar.visibility =
                    View.GONE
                text_view_text_view_fragment_collection_detail_short_name.visibility = View.VISIBLE
                text_view_text_view_fragment_collection_detail_short_name.text = getShortName(
                    firstName = author?.first_name,
                    lastName = author?.last_name
                )
            }
            constraint_layout_fragment_collection_detail_profile_container.setOnClickListener { thisView ->
                //adapter.itemClickListener?.onViewClicked(thisView,position,item)
            }
            button_fragment_collection_detail_change_collection.setOnClickListener { thisView ->
                //adapter.itemClickListener?.onViewClicked(thisView,position,item)
            }

            text_view_fragment_collection_detail_comments_cost.text =
                "${NumberFormat.getInstance().format(total_price)} $total_price_currency"
            text_view_fragment_collection_detail_comments_count.text =
                "Показать $comments_count коммент."
            text_view_fragment_collection_detail_date.text = "${
                DateFormatterHelper.formatISO_8601(
                    created_at,
                    DateFormatterHelper.FORMAT_DATE_DD_MMMM
                )
            }"
        }

        Glide.with(this).load(currentModel.cover_photo)
            .into(this.image_view_fragment_collection_detail_imageholder)

    }

    override fun initializeListeners() {
        text_view_fragment_collection_detail_comments_count.setOnClickListener(this)
        button_fragment_collection_detail_change_collection.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (view.id) {
            R.id.frame_layout_item_main_image_holder_container -> {
                item as ClothesMainModel
                val bundle = Bundle()
                bundle.putParcelable("clotheModel", item)
                findNavController().navigate(
                    R.id.action_collectionDetailFragment_to_itemDetailFragment,
                    bundle
                )
            }
            R.id.button_fragment_collection_detail_change_collection -> {
                currentModel.clothes?.let {
                    val itemsList = ArrayList<ClothesTypeDataModel>()
                    val bundle = Bundle()
                    it.forEach { clothe ->
                        itemsList.add(
                            ClothesTypeDataModel(
                                id = clothe.id,
                                title = clothe.title,
                                cover_photo = clothe.cover_photo,
                                cost = clothe.cost,
                                sale_cost = clothe.sale_cost,
                                currency = clothe.currency,
                                clothes_types = clothe.clothes_type,
                                gender = clothe.gender,
                                constructor_photo = clothe.constructor_photo,
                                isLocated = true,

                                )
                        )
                    }
                    bundle.putParcelableArrayList("items", itemsList)
                    findNavController().navigate(R.id.createCollectionFragment, bundle)
                } ?: run {
                    findNavController().navigate(R.id.createCollectionFragment)
                }
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
            R.id.button_fragment_collection_detail_change_collection -> {
                currentModel.clothes?.let {
                    val itemsList = ArrayList<ClothesMainModel>()
                    itemsList.addAll(it)
                    val bundle = Bundle()
                    bundle.putParcelableArrayList("items", itemsList)
                    bundle.putInt("mainId", currentModel.id ?: 0)
                    findNavController().navigate(R.id.createCollectionFragment, bundle)
                } ?: run {
                    findNavController().navigate(R.id.createCollectionFragment)
                }
            }
        }

    }

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}