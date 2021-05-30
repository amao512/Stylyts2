package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.api.network.OrderApi
import kz.eztech.stylyts.data.repository.OrderRepository
import kz.eztech.stylyts.domain.repository.OrderDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class OrderModule {

    @Provides
    fun provideOrderApi(retrofit: Retrofit): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOrderDomainRepository(orderDomainRepository: OrderRepository): OrderDomainRepository {
        return orderDomainRepository
    }
}