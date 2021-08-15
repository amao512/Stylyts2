package kz.eztech.stylyts.ordering.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.OrderApi
import kz.eztech.stylyts.ordering.data.repositories.OrderRepository
import kz.eztech.stylyts.ordering.domain.repositories.OrderDomainRepository
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