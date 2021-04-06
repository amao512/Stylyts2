package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.FilteredItemsModel
import kz.eztech.stylyts.domain.models.shop.ShopCategoryModel
import kz.eztech.stylyts.domain.usecases.collection_constructor.GetFilteredItemsUseCase
import kz.eztech.stylyts.domain.usecases.collection_constructor.GetCategoryUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection.PhotoChooserContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class PhotoChooserPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var getCategoryUseCase: GetCategoryUseCase,
    private var getFilteredItemsUseCase: GetFilteredItemsUseCase
) : PhotoChooserContract.Presenter {

    private lateinit var view: PhotoChooserContract.View

    override fun disposeRequests() {
        getCategoryUseCase.clear()
        getFilteredItemsUseCase.clear()
    }

    override fun attach(view: PhotoChooserContract.View) {
        this.view = view
    }

    override fun getCategory(token: String) {
        view.displayProgress()

        getCategoryUseCase.execute(object : DisposableSingleObserver<ShopCategoryModel>() {
            override fun onSuccess(t: ShopCategoryModel) {
                view.processViewAction {
                    hideProgress()
                    processShopCategories(t)

                    t.menCategory?.let {
                        if (it.isNotEmpty()) {
                            it[0].clothes_types?.let { clothes ->
                                if (clothes.isNotEmpty()) {
                                    clothes.map { clothesTypes ->
                                        clothesTypes.id
                                    }?.let { it1 ->
                                        val map = view.getFilterMap()
                                        map["clothes_type"] = it1.joinToString()
                                        getShopCategoryTypeDetail(token, map)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(errorHelper.processError(e))
                }
            }
        })
    }

    override fun getShopCategoryTypeDetail(
        token: String,
        map: Map<String, Any>
    ) {
        view.displayProgress()

        getFilteredItemsUseCase.initParams(token, map)
        getFilteredItemsUseCase.execute(object : DisposableSingleObserver<FilteredItemsModel>() {

            override fun onSuccess(t: FilteredItemsModel) {
                view.processViewAction {
                    view.processFilteredItems(t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    displayMessage(errorHelper.processError(e))
                }
            }
        })
    }
}