package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.clothes.ClothesCreateModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.utils.Paginator

interface SaveClothesAcceptContract {

    interface View : BaseView {

        fun getCurrentMode(): Int

        fun getClothesCreateModel(): ClothesCreateModel

        fun renderPaginatorState(state: Paginator.State)

        fun processList(list: List<Any?>)

        fun processTypes(list: List<Any>)

        fun processCategories(list: List<Any>)

        fun processStyles(list: List<Any>)

        fun processBrands(list: List<Any>)

        fun processSuccessCreating(wardrobeModel: ClothesModel)

        fun displaySmallProgress()

        fun hideSmallProgress()
    }

    interface Presenter: BasePresenter<View> {

        fun loadPage(page: Int)

        fun loadTypes(page: Int)

        fun loadCategories(page: Int)

        fun loadStyles(page: Int)

        fun loadBrands(page: Int)

        fun getList()

        fun loadMorePage()

        fun createClothes()
    }
}