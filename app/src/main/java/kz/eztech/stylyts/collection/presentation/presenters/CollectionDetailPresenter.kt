package kz.eztech.stylyts.collection.presentation.presenters

import kz.eztech.stylyts.collection.presentation.contracts.CollectionDetailContract

class CollectionDetailPresenter : CollectionDetailContract.Presenter {

    private lateinit var view: CollectionDetailContract.View

    override fun disposeRequests() {
        view.disposeRequests()
    }

    override fun attach(view: CollectionDetailContract.View) {
        this.view = view
    }
}