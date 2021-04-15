package kz.eztech.stylyts.data.db.cart

import kz.eztech.stylyts.domain.models.clothes.ClothesModel

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
object CartMapper {

    fun mapToEntity(model: ClothesModel): CartEntity {
        return CartEntity(
            id = model.id,
            typeId = model.clothesCategory.clothesType.id,
            categoryId = model.clothesCategory.id,
            coverImage = model.constructorImage,
            brandId = model.clothesBrand.id,
            brandTitle = model.clothesBrand.title,
            title = model.title,
            productCode = model.productCode,
            totalCount = 0,
            price = model.cost,
            salePrice = model.salePrice,
            currency = model.currency
        )
    }
}