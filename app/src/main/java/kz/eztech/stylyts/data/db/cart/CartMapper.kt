package kz.eztech.stylyts.data.db.cart

import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
object CartMapper {

    fun mapToEntity(model: ClothesModel): CartEntity {
        var coverImage: String = EMPTY_STRING

        if (model.coverImages.isNotEmpty()) {
            coverImage = model.coverImages[0]
        }

        return CartEntity(
            id = model.id,
            typeId = model.clothesCategory.clothesType.id,
            categoryId = model.clothesCategory.id,
            coverImage = coverImage,
            brandId = model.clothesBrand.id,
            brandTitle = model.clothesBrand.title,
            title = model.title,
            productCode = model.productCode,
            totalCount = 0,
            price = model.cost,
            salePrice = model.salePrice,
            currency = model.currency,
            size = model.selectedSize?.size
        )
    }
}