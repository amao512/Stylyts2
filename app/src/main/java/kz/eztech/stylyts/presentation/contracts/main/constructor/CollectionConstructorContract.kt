package kz.eztech.stylyts.presentation.contracts.main.constructor

import android.widget.ImageView
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.stick.MotionEntity
import kz.eztech.stylyts.presentation.utils.stick.MotionView

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
class CollectionConstructorContract {
    interface View:BaseView{
        fun processTypeDetail(model: CategoryTypeDetailModel)
        fun processShopCategories(shopCategoryModel: ShopCategoryModel)
        fun processStyles(list:List<Style>)
        fun processSuccess()
        fun deleteSelectedView(motionEntity: MotionEntity)
    }
    interface Presenter:BasePresenter<View>{
        fun getCategory()
        fun getShopCategoryTypeDetail(typeId:Int,gender:String)
        fun getStyles(token:String)
        fun saveCollection(token:String,model: CollectionPostCreateModel)
    }
}