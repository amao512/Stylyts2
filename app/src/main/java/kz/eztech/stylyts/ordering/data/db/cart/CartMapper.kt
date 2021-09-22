package kz.eztech.stylyts.ordering.data.db.cart

import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.utils.EMPTY_STRING

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
object CartMapper {

    fun map(list: List<ClothesModel>): List<CartEntity> {
        return list.map {
            mapToEntity(model = it)
        }
    }

    fun mapToEntity(
        model: ClothesModel
    ): CartEntity {
        var coverImage: String = EMPTY_STRING

        if (model.constructorImage.isNotEmpty()) {
            coverImage = model.constructorImage
        } else if (model.coverImages.isNotEmpty()) {
            coverImage = model.coverImages[0]
        }

        var size: String = "M"
        model.selectedSize?.size?.let {
            if (it.isNotBlank()) {
                size = it
            }
        }

        return CartEntity(
            id = model.id,
            typeId = model.clothesCategory.clothesType.id,
            categoryId = model.clothesCategory.id,
            coverImage = coverImage,
            brandId = model.clothesBrand.id,
            brandTitle = model.clothesBrand.title,
            ownerId = model.owner.id,
            title = model.title,
            productCode = model.productCode,
            totalCount = 0,
            count = 1,
            price = model.cost,
            salePrice = model.salePrice,
            currency = model.currency,
            size = size,
            sizeList = getSizeList(model),
            referralUser = model.referralUser
        )
    }

    private fun getSizeList(clothesModel: ClothesModel): ArrayList<String> {
        val list = ArrayList<String>()

        clothesModel.sizeInStock.map {
            list.add(it.size)
        }

        return list
    }
}