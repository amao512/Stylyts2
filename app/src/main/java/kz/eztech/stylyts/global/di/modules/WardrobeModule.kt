package kz.eztech.stylyts.global.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.WardrobeApi
import kz.eztech.stylyts.profile.data.repositories.WardrobeRepository
import kz.eztech.stylyts.profile.domain.repositories.WardrobeDomainRepository
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