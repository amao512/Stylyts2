package kz.eztech.stylyts.global.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.UserApi
import kz.eztech.stylyts.global.data.repositories.UserRepository
import kz.eztech.stylyts.global.domain.repositories.UserDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class UserModule {

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    fun provideUserDomainRepository(userRepository: UserRepository): UserDomainRepository {
        return userRepository
    }
}