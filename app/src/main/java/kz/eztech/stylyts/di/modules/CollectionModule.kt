package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.CollectionApi
import kz.eztech.stylyts.data.repository.collection.ItemDetailRepository
import kz.eztech.stylyts.domain.repository.collection.ItemDetailDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CollectionModule {

    @Provides
    @Singleton
    fun provideCollectionApi(retrofit: Retrofit): CollectionApi {
        return retrofit.create(CollectionApi::class.java)
    }

    @Provides
    fun providesItemDetailRepository(itemDetailRepository: ItemDetailRepository): ItemDetailDomainRepository {
        return itemDetailRepository
    }
}