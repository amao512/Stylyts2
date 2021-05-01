package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.WardrobeApi
import kz.eztech.stylyts.data.repository.WardrobeRepository
import kz.eztech.stylyts.domain.repository.WardrobeDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class WardrobeModule {

    @Provides
    @Singleton
    internal fun provideWardrobeApi(retrofit: Retrofit): WardrobeApi {
        return retrofit.create(WardrobeApi::class.java)
    }

    @Provides
    fun provideWardrobeDomainRepository(wardrobeDomainRepository: WardrobeRepository): WardrobeDomainRepository {
        return wardrobeDomainRepository
    }
}