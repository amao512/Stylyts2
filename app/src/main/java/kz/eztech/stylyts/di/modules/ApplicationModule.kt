package kz.eztech.stylyts.di.modules

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    internal fun providesApplication(): Application {
        return mApplication
    }

    @Provides
    @Singleton
    internal fun providesSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Named("executor_thread")
    fun providesExecutorThread(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named("ui_thread")
    fun providesUiThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    fun provideDataSource() = LocalDataSource(mApplication)

    @Provides
    fun provideCartDataSource() = CartDataSource(mApplication)

    @Provides
    fun provideCardDataSource() = CardDataSource(mApplication)
}