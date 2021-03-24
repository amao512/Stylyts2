package kz.eztech.stylyts.common.data.db.entities

import com.google.gson.Gson
import kz.eztech.stylyts.common.domain.models.ClothesMainModel

/**
 * Created by Ruslan Erdenoff on 29.01.2021.
 */
object CartMapper {
    fun mapToEntity(model: ClothesMainModel): CartEntity {
        return(CartEntity(
          item_model = Gson().toJson(model)
        ))
    }

    fun mapToClotheMainModel(entity: CartEntity): ClothesMainModel {
        return Gson().fromJson(entity.item_model, ClothesMainModel::class.java)
    }
}