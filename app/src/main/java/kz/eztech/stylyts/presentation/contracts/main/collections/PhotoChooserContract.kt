package kz.eztech.stylyts.presentation.contracts.main.collections

import android.net.Uri
import kz.eztech.stylyts.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface PhotoChooserContract {
    interface View:BaseView{
        fun updatePhoto(path: Uri?)
        fun processFilteredItems(model: FilteredItemsModel)
        fun processShopCategories(shopCategoryModel: ShopCategoryModel)
    }
    interface Presenter:BasePresenter<View>{
        fun getCategory(token:String)
        fun getShopCategoryTypeDetail(token:String, map:Map<String,Any>)
    }
}