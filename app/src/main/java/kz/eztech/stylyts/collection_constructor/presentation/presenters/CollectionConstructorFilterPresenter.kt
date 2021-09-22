package kz.eztech.stylyts.collection_constructor.presentation.presenters

import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.collection_constructor.presentation.contracts.CollectionConstructorFilterContract
import kz.eztech.stylyts.utils.EMPTY_STRING
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterPresenter @Inject constructor(
    private var errorHelper: ErrorHelper
) : CollectionConstructorFilterContract.Presenter {

    private lateinit var view: CollectionConstructorFilterContract.View
    private lateinit var token: String

    override fun getCategories() {}

    override fun getBrands() {
        view.displayProgress()
    }

    override fun getColors() {}

    override fun getPriceRange(range: Pair<Int, Int>) {}

    override fun getDiscount() {}

    override fun disposeRequests() {}

    override fun attach(view: CollectionConstructorFilterContract.View) {
        this.view = view
    }

    fun initToken(token: String?) {
        this.token = token ?: EMPTY_STRING
    }
}