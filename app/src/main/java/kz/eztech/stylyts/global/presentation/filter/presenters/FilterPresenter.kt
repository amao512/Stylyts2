package kz.eztech.stylyts.global.presentation.filter.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.filter.CategoryFilterSingleCheckGenre
import kz.eztech.stylyts.global.domain.models.filter.FilterCheckModel
import kz.eztech.stylyts.global.domain.models.clothes.*
import kz.eztech.stylyts.global.domain.usecases.clothes.*
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.global.presentation.filter.contracts.FilterContract
import kz.eztech.stylyts.utils.EMPTY_STRING
import java.util.*
import javax.inject.Inject

class FilterPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val getClothesTypesUseCase: GetClothesTypesUseCase,
    private val getClothesCategoriesUseCase: GetClothesCategoriesUseCase,
    private val getClothesBrandsUseCase: GetClothesBrandsUseCase,
    private val getClothesUseCase: GetClothesUseCase,
    private val getClothesColorsUseCase: GetClothesColorsUseCase
) : FilterContract.Presenter {

    private lateinit var view: FilterContract.View

    override fun disposeRequests() {
        getClothesTypesUseCase.clear()
        getClothesCategoriesUseCase.clear()
        getClothesBrandsUseCase.clear()
        getClothesUseCase.clear()
        getClothesColorsUseCase.clear()
    }

    override fun attach(view: FilterContract.View) {
        this.view = view
    }

    override fun getMyWardrobe(filterModel: ClothesFilterModel) {
        getClothesUseCase.initParams(filterModel.page, filterModel)
        getClothesUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                val categoryList: MutableList<ClothesCategoryModel> = mutableListOf()

                t.results.map {
                    categoryList.add(it.clothesCategory)
                }

                val set: Set<ClothesCategoryModel> = HashSet(categoryList)
                categoryList.clear()
                categoryList.addAll(set)

                val preparedList: MutableList<FilterCheckModel> = mutableListOf()
                preparedList.add(
                    FilterCheckModel(
                        id = 0,
                        isCustom = true,
                        item = ClothesCategoryModel(
                            id = 0,
                            title = "Мой гардероб",
                            clothesType = ClothesTypeModel(id = 0, title = EMPTY_STRING),
                            bodyPart = 0
                        )
                    )
                )

                categoryList.map {
                    preparedList.add(
                        FilterCheckModel(id = it.id, item = it)
                    )
                }

                view.processWardrobe(preparedList)
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }

    override fun getClothesTypes() {
        view.displayProgress()

        getClothesTypesUseCase.initParams()
        getClothesTypesUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesTypeModel>) {
                getPreparedCategories(
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

    override fun getClothesBrands(title: String) {
        getClothesBrandsUseCase.initParams()
        getClothesBrandsUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<ClothesBrandModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesBrandModel>) {
                val preparedResults: MutableList<FilterCheckModel> = mutableListOf()
                val characterList: MutableList<String> = mutableListOf()

                sortedBrandList(t.results).map { list ->
                    list.map {
                        preparedResults.add(it)
                    }

                    characterList.add(list[0].item as String)
                }

                view.processViewAction {
                    processClothesBrands(list = preparedResults)
                    processBrandCharacters(characters = characterList)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                }
            }
        })
    }

    override fun getClothesResults(filterModel: ClothesFilterModel) {
        getClothesUseCase.initParams(
            filterModel = filterModel,
            page = filterModel.page
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

    override fun getColors() {
        getClothesColorsUseCase.initParams()
        getClothesColorsUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesColorModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesColorModel>) {
                val preparedList: MutableList<FilterCheckModel> = mutableListOf()

                t.results.map {
                    preparedList.add(
                        FilterCheckModel(
                            id = it.id,
                            item = it
                        )
                    )
                }

                view.processColors(list = preparedList)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    private fun getPreparedCategories(
        resultsModel: ResultsModel<ClothesTypeModel>,
        onCategories: (List<CategoryFilterSingleCheckGenre>) -> Unit
    ) {
        val list: MutableList<CategoryFilterSingleCheckGenre> = mutableListOf()

        resultsModel.results.map { type ->
            getClothesCategoriesByType(
                typeId = type.id,
                onResults = { resultsModel ->
                    val preparedResults: MutableList<FilterCheckModel> = mutableListOf()

                    preparedResults.add(
                        FilterCheckModel(
                            id = type.id,
                            isCustom = true,
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
                                filterItems = preparedResults,
                                position = type.id
                            )
                        )

                        onCategories(list)
                    }
                }
            )
        }
    }

    private fun getClothesCategoriesByType(
        typeId: Int,
        onResults: (ResultsModel<ClothesCategoryModel>) -> Unit
    ) {
        getClothesCategoriesUseCase.initParams(typeId)
        getClothesCategoriesUseCase.execute(
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

    private fun sortedBrandList(results: List<ClothesBrandModel>): List<List<FilterCheckModel>> {
        val list: MutableList<MutableList<FilterCheckModel>> = mutableListOf()

        results.map { brand ->
            var item: FilterCheckModel?
            var position: Int = -1
            var counter = 0

            list.map { filterList ->
                item = filterList.find {
                    it.item == brand.title.substring(0, 1)
                        .toUpperCase(Locale.getDefault())
                }

                if (item != null) {
                    position = counter
                }

                counter++
            }

            if (position != -1) {
                list[position].add(
                    FilterCheckModel(
                        id = brand.id,
                        item = brand
                    )
                )
            } else {
                val character = FilterCheckModel(
                    id = 0,
                    item = brand.title.substring(0, 1).toUpperCase(Locale.getDefault())
                )

                val newBrand = FilterCheckModel(
                    id = brand.id,
                    item = brand
                )

                list.add(mutableListOf(character, newBrand))
            }
        }

        list.sortBy { (it[0].item as String) }

        return list
    }
}