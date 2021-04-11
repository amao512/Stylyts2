package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.AddressApi
import kz.eztech.stylyts.data.repository.address.AddressRepository
import kz.eztech.stylyts.domain.repository.address.AddressDomainRepository
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