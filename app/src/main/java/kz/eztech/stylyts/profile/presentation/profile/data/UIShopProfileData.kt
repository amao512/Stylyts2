package kz.eztech.stylyts.profile.presentation.profile.data

import android.content.Context
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.global.domain.models.filter.CollectionFilterModel

interface UIShopProfileData {

    fun getProfileFilter(
        context: Context,
        typesList: List<ClothesTypeModel>
    ): List<CollectionFilterModel>
}

class UIShopProfileDataDelegate : UIShopProfileData {

    override fun getProfileFilter(
        context: Context,
        typesList: List<ClothesTypeModel>
    ): List<CollectionFilterModel> {
        val filterList = ArrayList<CollectionFilterModel>()

        filterList.add(
            CollectionFilterModel(
                id = 1,
                name = context.getString(R.string.filter_list_filter),
                icon = R.drawable.ic_filter,
                isDisabled = true
            )
        )
        filterList.add(
            CollectionFilterModel(
                id = 2,
                name = context.getString(R.string.filter_list_publishes),
                isChosen = true
            )
        )
        filterList.add(
            CollectionFilterModel(
                id = 3,
                name = context.getString(R.string.filter_list_photo_outfits)
            )
        )
        filterList.add(
            CollectionFilterModel(
                id = 4,
                name = context.getString(R.string.filter_list_all_positions)
            )
        )

        var counter = 5

        typesList.map {
            filterList.add(
                CollectionFilterModel(
                    id = counter,
                    name = it.title,
                    item = it
                )
            )
            counter++
        }

        return filterList
    }
}