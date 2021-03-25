package kz.eztech.stylyts.presentation.presenters.collection

import kz.eztech.stylyts.presentation.contracts.collection.CollectionDetailContract

class CollectionDetailPresenter : CollectionDetailContract.Presenter {

    private lateinit var view: CollectionDetailContract.View

    override fun disposeRequests() {
        view.disposeRequests()
    }

    override fun attach(view: CollectionDetailContract.View) {
        this.view = view
    }
}