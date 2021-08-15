package kz.eztech.stylyts.global.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.OutfitsApi
import kz.eztech.stylyts.global.data.repositories.OutfitsRepository
import kz.eztech.stylyts.global.domain.repositories.OutfitsDomainRepository
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