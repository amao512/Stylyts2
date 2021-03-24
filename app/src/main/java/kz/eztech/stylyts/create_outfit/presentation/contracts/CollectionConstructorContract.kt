package kz.eztech.stylyts.create_outfit.presentation.contracts

import kz.eztech.stylyts.common.domain.models.*
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.create_outfit.domain.models.FilteredItemsModel
import kz.eztech.stylyts.create_outfit.domain.models.ShopCategoryModel
import kz.eztech.stylyts.common.domain.models.MainResult
import java.io.File

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface CollectionConstructorContract {
    interface View : MotionViewContract {

        fun processShopCategories(shopCategoryModel: ShopCategoryModel)

        fun processStyles(list: List<Style>)

        fun processSuccess(result: MainResult?)

        fun processFilteredItems(model: FilteredItemsModel)
    }

    interface Presenter : BasePresenter<View> {

        fun getCategory()

        fun getShopCategoryTypeDetail(
            token: String,
            map: Map<String, Any>
        )

        fun getStyles(token: String)

        fun saveCollection(
            token: String,
            model: CollectionPostCreateModel,
            data: File
        )

        fun updateCollection(
            token: String,
            id: Int,
            model: CollectionPostCreateModel,
            data: File
        )

        fun saveCollectionToMe(
            token: String,
            id: Int
        )
    }
}