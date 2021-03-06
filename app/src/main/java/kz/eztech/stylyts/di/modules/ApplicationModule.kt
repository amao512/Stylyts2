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
import kz.eztech.stylyts.data.repository.AuthorizationRepository
import kz.eztech.stylyts.data.repository.main.*
import kz.eztech.stylyts.domain.repository.AuthorizationDomainRepository
import kz.eztech.stylyts.domain.repository.main.*
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */

@Module
class ApplicationModule(internal var mApplication: Application){
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
    fun providesAuthorizationRepository(authorizationDomainRepository: AuthorizationRepository) : AuthorizationDomainRepository{
        return authorizationDomainRepository
    }

    @Provides
    fun providesShopCategoryRepository(shopCategoryRepository: ShopCategoryRepository) : ShopCategoryDomainRepository {
        return shopCategoryRepository
    }
    
    @Provides
    fun providesProfileRepository(profileRepository: ProfileRepository) : ProfileDomainRepository {
        return profileRepository
    }
    
    @Provides
    fun providesMainLentaRepository(mainLentaRepository: MainLentaRepository) : MainLentaDomainRepository {
        return mainLentaRepository
    }

    @Provides
    fun providesItemDetailRepository(itemDetailRepository: ItemDetailRepository) : ItemDetailDomainRepository {
        return itemDetailRepository
    }
    
    @Provides
    fun providesFilteredItemsRepository(filteredItemsRepository: FilteredItemsRepository) : FilteredItemsDomainRepository {
        return filteredItemsRepository
    }
    
    @Provides
    fun providesUserSearchRepository(userSearchRepository: UserSearchRepository) : UserSearchDomainRepository {
        return userSearchRepository
    }

    @Provides
    fun provideDataSource() = LocalDataSource(mApplication)

}