package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.AuthApi
import kz.eztech.stylyts.data.repository.auth.AuthorizationRepository
import kz.eztech.stylyts.domain.repository.auth.AuthorizationDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Asylzhan Seytbek on 22.03.2021.
 */
@Module
class AuthModule {

    @Provides
    @Singleton
    internal fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun providesAuthorizationRepository(authorizationDomainRepository: AuthorizationRepository): AuthorizationDomainRepository {
        return authorizationDomainRepository
    }
}