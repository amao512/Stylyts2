package kz.eztech.stylyts.common.presentation.contracts.main.collections

import android.net.Uri
import kz.eztech.stylyts.collection_constructor.domain.models.FilteredItemsModel
import kz.eztech.stylyts.collection_constructor.domain.models.ShopCategoryModel
import kz.eztech.stylyts.common.presentation.base.BasePresenter
import kz.eztech.stylyts.collection_constructor.presentation.contracts.MotionViewContract

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface PhotoChooserContract {
    interface View: MotionViewContract {
        fun updatePhoto(path: Uri?)
        fun processFilteredItems(model: FilteredItemsModel)
        fun processShopCategories(shopCategoryModel: ShopCategoryModel)
        fun getFilterMap():HashMap<String,Any>
    }
    interface Presenter: BasePresenter<View> {
        fun getCategory(token:String)
        fun getShopCategoryTypeDetail(token:String, map:Map<String,Any>)
    }
}