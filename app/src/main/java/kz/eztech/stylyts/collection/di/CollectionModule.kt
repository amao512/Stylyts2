package kz.eztech.stylyts.collection.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.collection.data.CollectionApi
import kz.eztech.stylyts.collection.data.ItemDetailRepository
import kz.eztech.stylyts.collection.domain.repository.ItemDetailDomainRepository
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