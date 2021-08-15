package kz.eztech.stylyts.global.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.AddressApi
import kz.eztech.stylyts.global.data.repositories.AddressRepository
import kz.eztech.stylyts.global.domain.repositories.AddressDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AddressModule {

    @Provides
    @Singleton
    internal fun provideAddressApi(retrofit: Retrofit): AddressApi {
        return retrofit.create(AddressApi::class.java)
    }

    @Provides
    fun providesAddressRepository(addressRepository: AddressRepository): AddressDomainRepository {
        return addressRepository
    }
}