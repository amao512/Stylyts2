package kz.eztech.stylyts.presentation.presenters.filter

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.domain.models.filter.FilterModel
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesBrandsUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesCategoriesByTypeUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.filter.FilterContract
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class FilterPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getClothesCategoriesByTypeUseCase: GetClothesCategoriesByTypeUseCase,
    private val getClothesBrandsUseCase: GetClothesBrandsUseCase,
    private val getClothesUseCase: GetClothesUseCase
) : FilterContract.Presenter {

    private lateinit var view: FilterContract.View

    override fun disposeRequests() {
        getClothesTypesUseCase.clear()
        getClothesCategoriesByTypeUseCase.clear()
        getClothesBrandsUseCase.clear()
        getClothesUseCase.clear()
    }

    override fun attach(view: FilterContract.View) {
        this.view = view
    }

    override fun getClothesTypes(token: String) {
        view.displayProgress()

        getClothesTypesUseCase.initParams(token)
        getClothesTypesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                getPreparedCategories(
                    token = token,
                    resultsModel = t,
                    onCategories = {
                        view.processViewAction {
                            processClothesCategories(list = it)
                            hideProgress()
                        }
                    }
                )
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun getClothesBrands(
        token: String,
        title: String
    ) {
        getClothesBrandsUseCase.initParams(token)
        getClothesBrandsUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesBrandModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesBrandModel>) {
                view.processViewAction {
                    val preparedResults: MutableList<FilterCheckModel> = mutableListOf()

                    preparedResults.add(
                        FilterCheckModel(
                            id = 0,
                            item = ClothesBrandModel(
                                id = 0,
                                title = title,
                                website = EMPTY_STRING,
                                logo = EMPTY_STRING,
                                createdAt = EMPTY_STRING,
                                modifiedAt = EMPTY_STRING
                            )
                        )
                    )

                    t.results.map {
                        preparedResults.add(
                            FilterCheckModel(
                                id = it.id,
                                item = it
                            )
                        )
                    }

                    val preparedResultsModel = ResultsModel(
                        page = t.page,
                        totalPages = t.totalPages,
                        pageSize = t.pageSize,
                        totalCount = t.totalCount,
                        results = preparedResults
                    )

                    processClothesBrands(resultsModel = preparedResultsModel)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getClothesResults(
        token: String,
        filterModel: FilterModel
    ) {
        getClothesUseCase.initParams(
            token = token,
            filterModel = filterModel
        )
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processViewAction {
                    processClothesResults(resultsModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    private fun getPreparedCategories(
        token: String,
        resultsModel: ResultsModel<ClothesTypeModel>,
        onCategories: (List<CategoryFilterSingleCheckGenre>) -> Unit
    ) {
        val list: MutableList<CategoryFilterSingleCheckGenre> = mutableListOf()

        resultsModel.results.map { type ->
            getClothesCategoriesByType(
                token = token,
                typeId = type.id,
                onResults = { resultsModel ->
                    val preparedResults: MutableList<FilterCheckModel> = mutableListOf()

                    preparedResults.add(
                        FilterCheckModel(
                            id = type.id,
                            item = ClothesCategoryModel(
                                id = type.id,
                                title = type.title,
                                clothesType = type,
                                bodyPart = type.id
                            )
                        )
                    )

                    resultsModel.results.let {
                        it.map { category ->
                            preparedResults.add(
                                FilterCheckModel(
                                    id = category.id,
                                    item = category
                                )
                            )
                        }

                        list.add(
                            CategoryFilterSingleCheckGenre(
                                title = type.title,
                                filterItems = preparedResults
                            )
                        )

                        onCategories(list)
                    }
                }
            )
        }
    }

    private fun getClothesCategoriesByType(
        token: String,
        typeId: Int,
        onResults: (ResultsModel<ClothesCategoryModel>) -> Unit
    ) {
        getClothesCategoriesByTypeUseCase.initParams(token, typeId)
        getClothesCategoriesByTypeUseCase.execute(
            object : DisposableSingleObserver<ResultsModel<ClothesCategoryModel>>() {
                override fun onSuccess(t: ResultsModel<ClothesCategoryModel>) {
                    onResults(t)
                }

                override fun onError(e: Throwable) {
                    view.processViewAction {
                        displayMessage(msg = errorHelper.processError(e))
                    }
                }
            }
        )
    }
}