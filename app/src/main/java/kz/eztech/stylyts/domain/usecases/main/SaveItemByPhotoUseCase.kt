package kz.eztech.stylyts.domain.usecases.main

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ClothesMainModel
import kz.eztech.stylyts.domain.repository.main.ItemDetailDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 24.02.2021.
 */
class SaveItemByPhotoUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var itemDetailDomainRepository: ItemDetailDomainRepository
) : BaseUseCase<ClothesMainModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var data: ArrayList<MultipartBody.Part>

    override fun createSingleObservable(): Single<ClothesMainModel> {
        return itemDetailDomainRepository.saveItemByPhoto(token, data)
    }

    fun initParams(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ) {
        this.token = "Bearer $token"
        this.data = data
    }
}