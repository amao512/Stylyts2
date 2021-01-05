package kz.eztech.stylyts.domain.usecases

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver

abstract class BaseUseCase<T> {

    var compositeDisposable: CompositeDisposable
    var executorThread: Scheduler
    var uiThread: Scheduler

    constructor(executorThread: Scheduler, uiThread: Scheduler){
        this.executorThread = executorThread
        this.uiThread = uiThread
        compositeDisposable = CompositeDisposable()
    }

    fun execute(disposableSingleObserver: DisposableSingleObserver<T>){
        var observer = this.createSingleObservable()
            .subscribeOn(executorThread)
            .observeOn(uiThread)
            .subscribeWith(disposableSingleObserver)

        compositeDisposable.add(observer)
    }

    fun clear(){
        compositeDisposable.clear()
    }

    fun dispose(){
        if(!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    abstract fun createSingleObservable(): Single<T>
}