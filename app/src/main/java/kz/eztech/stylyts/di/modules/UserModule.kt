package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.UserApi
import kz.eztech.stylyts.data.repository.user.UserRepository
import kz.eztech.stylyts.domain.repository.user.UserDomainRepository
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