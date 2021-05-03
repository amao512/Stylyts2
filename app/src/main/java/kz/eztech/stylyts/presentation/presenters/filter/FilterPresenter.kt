package kz.eztech.stylyts.presentation.presenters.filter

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ColorModel
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
import java.util.*
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

    override fun getMyWardrobe(
        token: String,
        filterModel: FilterModel
    ) {
        getClothesUseCase.initParams(token, filterModel)
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

    override fun getClothesTypes(token: String) {
        view.displayProgress()

        getClothesTypesUseCase.initParams(token)
        getClothesTypesUseCase.execute(object :
            DisposableSingleObserver<ResultsModel<ClothesTypeModel>>() {
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

    override fun getColors(token: String) {
        val list: MutableList<FilterCheckModel> = mutableListOf()
        list.add(
            FilterCheckModel(
                id = 1,
                item = ColorModel(
                    id = 1,
                    title = "Белый",
                    color = "#ffffff"
                )
            )
        )
        list.add(
            FilterCheckModel(
                id = 2,
                item = ColorModel(
                    id = 2,
                    title = "Черный",
                    color = "#000000"
                )
            )
        )

        view.processColors(list)
    }
}