package kz.eztech.stylyts.address.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.address.domain.repository.AddressDomainRepository
import kz.eztech.stylyts.address.data.AddressApi
import kz.eztech.stylyts.address.data.respository.AddressRepository
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