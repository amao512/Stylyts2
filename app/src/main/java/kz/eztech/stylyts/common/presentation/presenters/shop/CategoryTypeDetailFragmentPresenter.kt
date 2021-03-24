package kz.eztech.stylyts.common.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.create_outfit.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.create_outfit.domain.usecases.GetCategoryTypeDetailUseCase
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.common.presentation.contracts.main.shop.CategoryTypeDetailContract
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class CategoryTypeDetailFragmentPresenter:CategoryTypeDetailContract.Presenter {
    private var errorHelper: ErrorHelper
    private var getCategoryTypeDetailUseCase: GetCategoryTypeDetailUseCase
    private lateinit var view: CategoryTypeDetailContract.View
    @Inject
    constructor(errorHelper: ErrorHelper,
                getCategoryTypeDetailUseCase: GetCategoryTypeDetailUseCase
    ){
        this.getCategoryTypeDetailUseCase = getCategoryTypeDetailUseCase
        this.errorHelper = errorHelper
    }
    override fun disposeRequests() {
        getCategoryTypeDetailUseCase.clear()
    }

    override fun attach(view: CategoryTypeDetailContract.View) {
        this.view = view
    }

    override fun getShopCategoryTypeDetail(typeId:Int,gender:String) {
        view.displayProgress()
        val map = HashMap<String,Any>()
        map["id"] = typeId
        map["gender_type"] = gender
        getCategoryTypeDetailUseCase.initParams(map)
        getCategoryTypeDetailUseCase.execute(object : DisposableSingleObserver<CategoryTypeDetailModel>(){
            override fun onSuccess(t: CategoryTypeDetailModel) {
                view.processViewAction {
                    view.processTypeDetail(t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                }
            }
        })
    }
}