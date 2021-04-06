package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import kz.eztech.stylyts.domain.models.shop.ShopCategoryModel
import kz.eztech.stylyts.domain.models.MainResult
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