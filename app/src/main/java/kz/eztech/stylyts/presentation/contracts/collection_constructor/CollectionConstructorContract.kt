package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesStyleModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 21.12.2020.
 */
interface CollectionConstructorContract {

    interface View : MotionViewContract {

        fun isItems(): Boolean

        fun isStyles(): Boolean

        fun getClothesFilter(): ClothesFilterModel

        fun renderPaginatorState(state: Paginator.State)

        fun processTypesResults(resultsModel: ResultsModel<ClothesTypeModel>)

        fun processList(list: List<Any?>)

        fun processClothes(clothes: List<ClothesModel>)

        fun processStyles(styles: List<ClothesStyleModel>)
    }

    interface Presenter : BasePresenter<View> {

        fun loadPage(page: Int)

        fun loadMorePage()

        fun loadClothes(page: Int)

        fun loadStyles(page: Int)

        fun getTypes()

        fun getClothesAndStyles()
    }
}