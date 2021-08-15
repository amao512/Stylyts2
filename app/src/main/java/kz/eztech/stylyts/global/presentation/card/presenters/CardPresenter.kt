package kz.eztech.stylyts.global.presentation.card.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.global.data.db.card.CardDataSource
import kz.eztech.stylyts.global.data.exception.ErrorHelper
import kz.eztech.stylyts.global.presentation.base.processViewAction
import kz.eztech.stylyts.global.presentation.card.contracts.CardContract
import javax.inject.Inject

class CardPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val cardDataSource: CardDataSource
) : CardContract.Presenter {

    private lateinit var view: CardContract.View

    private val disposable = CompositeDisposable()

    override fun disposeRequests() {
        disposable.clear()
    }

    override fun attach(view: CardContract.View) {
        this.view = view
    }

    override fun getCardList() {
        view.displayProgress()

        disposable.clear()
        disposable.add(
            cardDataSource.allCards
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.processViewAction {
                        processCards(list = it)
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