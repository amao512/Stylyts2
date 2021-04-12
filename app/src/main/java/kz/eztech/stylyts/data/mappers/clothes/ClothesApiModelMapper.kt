package kz.eztech.stylyts.data.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesApiModel
import kz.eztech.stylyts.data.mappers.OwnerApiModelMapper
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class ClothesApiModelMapper @Inject constructor(
    private val clothesStyleApiModelMapper: ClothesStyleApiModelMapper,
    private val clothesCategoryApiModelMapper: ClothesCategoryApiModelMapper,
    private val ownerApiModelMapper: OwnerApiModelMapper,
    private val clothesBrandApiModelMapper: ClothesBrandApiModelMapper
) {

    fun map(data: List<ClothesApiModel>?): List<ClothesModel> {
        data ?: return emptyList()

        return data.map {
            ClothesModel(
                id = it.id ?: 0,
                clothesStyle = clothesStyleApiModelMapper.map(it.clothesStyle),
                clothesCategory = clothesCategoryApiModelMapper.map(it.clothesCategory),
                constructorImage = it.constructorImage ?: EMPTY_STRING,
                coverImages = it.coverImages ?: emptyList(),
                clothesSizes = it.clothesSizes ?: emptyList(),
                clothesColors = it.clothesColors ?: emptyList(),
                title = it.title ?: EMPTY_STRING,
                description = it.description ?: EMPTY_STRING,
                gender = it.description ?: "M",
                cost = it.cost ?: 0,
                saleCost = it.saleCost ?: 0,
                currency = it.currency ?: EMPTY_STRING,
                productCode = it.productCode ?: EMPTY_STRING,
                createdAt = it.createdAt ?: EMPTY_STRING,
                modifiedAt = it.modifiedAt ?: EMPTY_STRING,
                owner = ownerApiModelMapper.map(it.owner),
                clothesBrand = clothesBrandApiModelMapper.map(it.clothesBrand),
            )
        }
    }

    fun map(data: ClothesApiModel?): ClothesModel {
        return ClothesModel(
            id = data?.id ?: 0,
            clothesStyle = clothesStyleApiModelMapper.map(data?.clothesStyle),
            clothesCategory = clothesCategoryApiModelMapper.map(data?.clothesCategory),
            constructorImage = data?.constructorImage ?: EMPTY_STRING,
            coverImages = data?.coverImages ?: emptyList(),
            clothesSizes = data?.clothesSizes ?: emptyList(),
            clothesColors = data?.clothesColors ?: emptyList(),
            title = data?.title ?: EMPTY_STRING,
            description = data?.description ?: EMPTY_STRING,
            gender = data?.description ?: "M",
            cost = data?.cost ?: 0,
            saleCost = data?.saleCost ?: 0,
            currency = data?.currency ?: EMPTY_STRING,
            productCode = data?.productCode ?: EMPTY_STRING,
            createdAt = data?.createdAt ?: EMPTY_STRING,
            modifiedAt = data?.modifiedAt ?: EMPTY_STRING,
            owner = ownerApiModelMapper.map(data?.owner),
            clothesBrand = clothesBrandApiModelMapper.map(data?.clothesBrand),
        )
    }
}