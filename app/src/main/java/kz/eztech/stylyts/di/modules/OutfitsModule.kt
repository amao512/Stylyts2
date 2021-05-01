package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.OutfitsApi
import kz.eztech.stylyts.data.repository.OutfitsRepository
import kz.eztech.stylyts.domain.repository.OutfitsDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class OutfitsModule {

    @Provides
    fun provideOutfitsApi(retrofit: Retrofit): OutfitsApi {
        return retrofit.create(OutfitsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOutfitsDomainRepository(outfitsDomainRepository: OutfitsRepository): OutfitsDomainRepository {
        return outfitsDomainRepository
    }
}