package kz.eztech.stylyts.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.db.LocalDataSource
import kz.eztech.stylyts.data.db.card.CardDataSource
import kz.eztech.stylyts.data.db.cart.CartDataSource
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */

@Module
class ApplicationModule(private val mApplication: Application) {

    @Singleton
    @Provides
    internal fun providesApplication(): Application = mApplication

    @Provides
    @Named("executor_thread")
    fun providesExecutorThread(): Scheduler = Schedulers.io()

    @Provides
    @Named("ui_thread")
    fun providesUiThread(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Singleton
    fun providesSharedPreferences(): SharedPreferences =
        mApplication.applicationContext
            .getSharedPreferences(
                mApplication.applicationContext.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )

    @Provides
    fun provideDataSource() = LocalDataSource(mApplication)

    @Provides
    fun provideCartDataSource() = CartDataSource(mApplication)

    @Provides
    fun provideCardDataSource() = CardDataSource(mApplication)
}