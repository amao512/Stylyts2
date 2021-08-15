package kz.eztech.stylyts.search.presentation.shop.data

import android.content.Context
import android.net.Uri
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesTypeModel
import javax.inject.Inject

interface UIShopItemData {

    fun getClothesTypes(
        context: Context,
        typesList: List<ClothesTypeModel>
    ): List<ClothesTypeModel>
}

class UISHopItemDataDelegate @Inject constructor() : UIShopItemData {

    override fun getClothesTypes(
        context: Context,
        typesList: List<ClothesTypeModel>
    ): List<ClothesTypeModel> {
        val preparedTypes: MutableList<ClothesTypeModel> = mutableListOf()
        val shopsIcon = Uri.parse("android.resource://${R::class.java.`package`.name}/${R.drawable.ic_shops}")

        preparedTypes.addAll(typesList)
        preparedTypes.add(
            ClothesTypeModel(
                id = 0,
                title = context.getString(R.string.search_item_shops),
                menCoverPhoto = shopsIcon.toString(),
                womenCoverPhoto = shopsIcon.toString(),
            )
        )

        return preparedTypes
    }
}