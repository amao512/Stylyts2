package kz.eztech.stylyts.presentation.contracts.collection_constructor

import android.net.Uri
import kz.eztech.stylyts.domain.models.clothes.ClothesFilterModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.common.SearchFilterModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.utils.Paginator

/**
 * Created by Ruslan Erdenoff on 23.12.2020.
 */
interface TagChooserContract {
    interface View : MotionViewContract {

        fun updatePhoto(path: Uri?)

        fun processTypesResults(resultsModel: ResultsModel<ClothesTypeModel>)

        fun processList(list: List<Any?>)

        fun getToken(): String

        fun getClothesFilter(): ClothesFilterModel

        fun getSearchFilter(): SearchFilterModel

        fun renderPaginatorState(state: Paginator.State)

        fun getFilterMap(): HashMap<String, Any>
    }

    interface Presenter : BasePresenter<View> {

        fun getCategory()

        fun loadPage(page: Int)

        fun loadMorePage()

        fun getList()

        fun getClothes(page: Int)

        fun searchClothes(page: Int)
    }
}