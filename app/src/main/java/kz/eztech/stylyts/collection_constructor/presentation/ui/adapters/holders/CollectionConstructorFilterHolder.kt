package kz.eztech.stylyts.collection_constructor.presentation.ui.adapters.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_constructor_filter_clothe_items.view.*
import kz.eztech.stylyts.global.presentation.common.ui.adapters.BaseAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.CollectionConstructorSubFilterAdapter
import kz.eztech.stylyts.global.presentation.common.ui.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.search.domain.models.ClothesTypes
import kz.eztech.stylyts.search.domain.models.GenderCategory
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterHolder(
    itemView: View,
    adapter: BaseAdapter
) : BaseViewHolder(itemView, adapter) {

    override fun bindData(item: Any, position: Int) {
        when (item) {
            is GenderCategory -> processGenderCategory(genderCategory = item)
            else -> {}
        }
    }

    private fun processGenderCategory(genderCategory: GenderCategory) {
        with (itemView) {
            recycler_view_item_constructor_filter_clothe_items.hide()

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