package kz.eztech.stylyts.presentation.presenters.filter

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesBrandModel
import kz.eztech.stylyts.domain.models.clothes.ClothesCategoryModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesBrandsUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesCategoriesByTypeUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesTypesUseCase
import kz.eztech.stylyts.domain.usecases.clothes.GetClothesUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.filter.FilterContract
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
                    resultsApi = t,
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

    override fun getClothesBrands(token: String) {
        getClothesBrandsUseCase.initParams(token)
        getClothesBrandsUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesBrandModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesBrandModel>) {
                view.processViewAction {
                    processClothesBrands(resultsModel = t)
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
        gender: String,
        clothesTypeId: Int,
        clothesCategoryId: Int,
        clothesBrandId: Int
    ) {
        getClothesUseCase.initParams(
            token = token,
            gender = gender,
            clothesTypeId = clothesTypeId,
            clothesCategoryId = clothesCategoryId,
            clothesBrandId = clothesBrandId
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
        resultsApi: ResultsModel<ClothesTypeModel>,
        onCategories: (List<CategoryFilterSingleCheckGenre>) -> Unit
    ) {
        val list: MutableList<CategoryFilterSingleCheckGenre> = mutableListOf()

        resultsApi.results.map { type ->
            getClothesCategoriesByType(
                token = token,
                typeId = type.id,
                onResults = { resultsModel ->
                    val preparedResults: MutableList<ClothesCategoryModel> = mutableListOf()

                    preparedResults.add(
                        ClothesCategoryModel(
                            id = type.id,
                            title = type.title,
                            clothesType = type,
                            bodyPart = type.id
                        )
                    )

                    resultsModel.results.let {
                        preparedResults.addAll(it)

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