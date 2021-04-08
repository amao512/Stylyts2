package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.usecases.collection_constructor.GetCategoryTypeDetailUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.main.shop.CategoryTypeDetailContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class CategoryTypeDetailFragmentPresenter @Inject constructor(
    private var errorHelper: ErrorHelper,
    private var getCategoryTypeDetailUseCase: GetCategoryTypeDetailUseCase
) : CategoryTypeDetailContract.Presenter {

    private lateinit var view: CategoryTypeDetailContract.View

    override fun disposeRequests() {
        getCategoryTypeDetailUseCase.clear()
    }

    override fun attach(view: CategoryTypeDetailContract.View) {
        this.view = view
    }

    override fun getCategoryTypeDetail(
        token: String,
        gender: String,
        clothesCategoryId: String
    ) {
        view.displayProgress()

        val data = HashMap<String, Any>()
        data["category_id"] = clothesCategoryId
        data["gender_type"] = gender

        getCategoryTypeDetailUseCase.initParams(token, data)
        getCategoryTypeDetailUseCase.execute(object : DisposableSingleObserver<ResultsModel<ClothesModel>>() {
            override fun onSuccess(t: ResultsModel<ClothesModel>) {
                view.processViewAction {
                    processTypeDetail(resultsModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(msg = errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }
}