package kz.eztech.stylyts.collection_constructor.presentation.adapters.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_constructor_filter_clothe_items.view.*
import kz.eztech.stylyts.common.domain.models.BrandModel
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.CollectionConstructorSubFilterAdapter
import kz.eztech.stylyts.common.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.common.presentation.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show
import kz.eztech.stylyts.collection_constructor.domain.models.ClothesTypes
import kz.eztech.stylyts.collection_constructor.domain.models.GenderCategory

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(item: Any, position: Int) {
        when (item) {
            is BrandModel -> processBrandModel(
                brandModel = item,
                position = position
            )
            is GenderCategory -> processGenderCategory(genderCategory = item)
            else -> {}
        }
    }

    private fun processBrandModel(
        brandModel: BrandModel,
        position: Int
    ) {
        with (itemView) {
            frame_layout_tem_constructor_filter_clothe_container.setOnClickListener {
                adapter.itemClickListener?.onViewClicked(it, position, brandModel)
            }

            text_view_item_constructor_filter_clothe_item_name.text = brandModel.first_name

            if (brandModel.isChosen) {
                image_view_item_constructor_filter_clothe_items.show()
            } else {
                image_view_item_constructor_filter_clothe_items.hide()
            }

            recycler_view_item_constructor_filter_clothe_items.show()
        }
    }

    private fun processGenderCategory(genderCategory: GenderCategory) {
        with (itemView) {
            recycler_view_item_constructor_filter_clothe_items.hide()

            val filterMap = HashMap<String, Any>()
            val currentIds = ArrayList<Int>()
            val clothesTypes = ArrayList<ClothesTypes>()
            val additionalAdapter = CollectionConstructorSubFilterAdapter()

            text_view_item_constructor_filter_clothe_item_name.text = genderCategory.title

            genderCategory.clothes_types?.let {
                processClothesTypes(
                    clothesTypes = clothesTypes,
                    currentIds = currentIds,
                    genderCategory = genderCategory,
                    additionalAdapter = additionalAdapter,
                    clothesTypesList = it
                )
            }
        }
    }

    private fun processClothesTypes(
        clothesTypes: ArrayList<ClothesTypes>,
        clothesTypesList: List<ClothesTypes>,
        additionalAdapter: CollectionConstructorSubFilterAdapter,
        currentIds: ArrayList<Int>,
        genderCategory: GenderCategory,
    ) {
        with (itemView) {
            clothesTypes.clear()
            clothesTypes.addAll(clothesTypesList)
            additionalAdapter.itemClickListener = object : UniversalViewClickListener {

                override fun onViewClicked(
                    view: View,
                    position: Int,
                    subItem: Any?
                ) {
                    onGenderClicked(
                        clothesTypes = clothesTypes,
                        currentIds = currentIds,
                        genderCategory = genderCategory,
                        additionalAdapter = additionalAdapter,
                        subItem = subItem
                    )
                }
            }

            this.recycler_view_item_constructor_filter_clothe_items.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.recycler_view_item_constructor_filter_clothe_items.adapter = additionalAdapter

            recycler_view_item_constructor_filter_clothe_items.show()
            additionalAdapter.updateList(clothesTypes)
            additionalAdapter.notifyDataSetChanged()

            frame_layout_tem_constructor_filter_clothe_container.setOnClickListener {}
        }
    }

    private fun onGenderClicked(
        clothesTypes: List<ClothesTypes>,
        currentIds: ArrayList<Int>,
        genderCategory: GenderCategory,
        additionalAdapter: CollectionConstructorSubFilterAdapter,
        subItem: Any?
    ) {
        with (itemView) {
            when (subItem) {
                is ClothesTypes -> {
                    val model = clothesTypes[clothesTypes.indexOf(subItem)]

                    if (currentIds.contains(subItem.id)) {
                        model.isChosen = false
                        currentIds.remove(subItem.id)
                    } else {
                        model.isChosen = true
                        currentIds.add(subItem.id!!)
                    }

                    genderCategory.chosenClothesTypes = subItem.id

                    adapter.itemClickListener?.onViewClicked(
                        frame_layout_tem_constructor_filter_clothe_container,
                        position,
                        genderCategory
                    )

                    additionalAdapter.updateList(clothesTypes)
                    additionalAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}