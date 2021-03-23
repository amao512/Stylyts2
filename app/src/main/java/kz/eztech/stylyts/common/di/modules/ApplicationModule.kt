package kz.eztech.stylyts.common.di.modules

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kz.eztech.stylyts.data.db.LocalDataSource
import kz.eztech.stylyts.data.repository.main.FilteredItemsRepository
import kz.eztech.stylyts.data.repository.main.ItemDetailRepository
import kz.eztech.stylyts.data.repository.main.MainLentaRepository
import kz.eztech.stylyts.data.repository.main.ShopCategoryRepository
import kz.eztech.stylyts.domain.repository.main.FilteredItemsDomainRepository
import kz.eztech.stylyts.domain.repository.main.ItemDetailDomainRepository
import kz.eztech.stylyts.domain.repository.main.MainLentaDomainRepository
import kz.eztech.stylyts.domain.repository.main.ShopCategoryDomainRepository
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
    fun providesShopCategoryRepository(shopCategoryRepository: ShopCategoryRepository): ShopCategoryDomainRepository {
        return shopCategoryRepository
    }

    @Provides
    fun providesMainLentaRepository(mainLentaRepository: MainLentaRepository): MainLentaDomainRepository {
        return mainLentaRepository
    }

    @Provides
    fun providesItemDetailRepository(itemDetailRepository: ItemDetailRepository): ItemDetailDomainRepository {
        return itemDetailRepository
    }

    @Provides
    fun providesFilteredItemsRepository(filteredItemsRepository: FilteredItemsRepository): FilteredItemsDomainRepository {
        return filteredItemsRepository
    }

    @Provides
    fun provideDataSource() = LocalDataSource(mApplication)
}