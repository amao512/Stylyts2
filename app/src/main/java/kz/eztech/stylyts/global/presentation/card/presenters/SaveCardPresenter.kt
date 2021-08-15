package kz.eztech.stylyts.global.presentation.card.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.global.data.db.card.CardDataSource
import kz.eztech.stylyts.global.data.db.card.CardEntity
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.ordering.presentation.order_constructor.contracts.SaveCardContract
import javax.inject.Inject

class SaveCardPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val cardDataSource: CardDataSource
) : SaveCardContract.Presenter {

    private lateinit var view: SaveCardContract.View

    private val disposable = CompositeDisposable()

    override fun disposeRequests() {
        disposable.clear()
    }

    override fun attach(view: SaveCardContract.View) {
        this.view = view
    }

    override fun getCardById(cardId: Int) {
        view.displayProgress()

        disposable.clear()
        disposable.add(
            cardDataSource.allCards
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.find { card -> card.id == cardId }
                }
                .subscribe({
                    view.processViewAction {
                        it?.let { processCard(it) }
                        hideProgress()
                    }
                }, {
                    view.processViewAction {
                        hideProgress()
                        displayMessage(msg = errorHelper.processError(it))
                    }
                })
        )
    }

    override fun saveCard(cardEntity: CardEntity) {
        view.displayProgress()

        disposable.clear()
        disposable.add(
            cardDataSource.insertCard(cardEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.processViewAction {
                        processSuccessSaving()
                        hideProgress()
                    }
                }, {
                    view.processViewAction {
                        hideProgress()
                        displayMessage(msg = errorHelper.processError(it))
                    }
                })
        )
    }
}