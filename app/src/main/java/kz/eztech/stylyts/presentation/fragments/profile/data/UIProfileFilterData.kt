package kz.eztech.stylyts.presentation.fragments.profile.data

import android.content.Context
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.filter.CollectionFilterModel

interface UIProfileFilterData {

    fun getProfileFilter(
        context: Context,
        isOwnProfile: Boolean
    ): List<CollectionFilterModel>
}

class UIProfileFilterDataDelegate : UIProfileFilterData {

    override fun getProfileFilter(
        context: Context,
        isOwnProfile: Boolean
    ): List<CollectionFilterModel> {
        val filterList = mutableListOf<CollectionFilterModel>()

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
        filterList.add(CollectionFilterModel(
            id = 4,
            name = context.getString(R.string.filter_list_wardrobe))
        )

        if (isOwnProfile) {
            filterList.add(CollectionFilterModel(
                id = 5,
                name = context.getString(R.string.filter_list_my_data))
            )
            filterList.add(
                CollectionFilterModel(
                    id = 6,
                    name = context.getString(R.string.profile_add_to_wardrobe_by_barcode),
                    icon = R.drawable.ic_baseline_qr_code_2_24
                )
            )
            filterList.add(
                CollectionFilterModel(
                    id = 7,
                    name = context.getString(R.string.profile_add_to_wardrobe_by_photo),
                    icon = R.drawable.ic_camera
                )
            )
        }

        return filterList
    }
}