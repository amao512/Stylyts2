package kz.eztech.stylyts.common.domain.usecases

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver

abstract class BaseUseCase<T>(
    var executorThread: Scheduler,
    var uiThread: Scheduler
) {

    var compositeDisposable: CompositeDisposable

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun execute(disposableSingleObserver: DisposableSingleObserver<T>) {
        val observer = this.createSingleObservable()
            .subscribeOn(executorThread)
            .observeOn(uiThread)
            .subscribeWith(disposableSingleObserver)

        compositeDisposable.add(observer)
    }

    fun clear() {
        compositeDisposable.clear()
    }

    fun dispose() {
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    abstract fun createSingleObservable(): Single<T>
}