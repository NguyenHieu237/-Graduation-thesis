package com.apero.realistic.network.action.usecase

import com.apero.realistic.network.action.response.BaseListResponse
import com.apero.realistic.network.action.response.InspirationResponse
import com.apero.realistic.network.action.response.TagKeywordResponse
import com.apero.realistic.network.action.response.discovery.DiscoveryResponse
import com.apero.realistic.network.api.core.ApiClient
import com.apero.realistic.network.api.core.ApiService
import com.apero.realistic.network.api.core.rxDefaultRequest
import io.reactivex.Observable

class UseCase {
    companion object {
        private var apiService: ApiService = ApiClient.build()
        private var mMyApi: UseCase? = null

        fun instance(): UseCase {
            if (mMyApi == null) {
                mMyApi = UseCase()
            }
            return mMyApi!!
        }
    }

    fun getListTagKeyword(): Observable<BaseListResponse<TagKeywordResponse>> {
        return apiService.getListTagKeyword().rxDefaultRequest()
    }

    fun getListDiscoveryArtwork(): Observable<BaseListResponse<DiscoveryResponse>> {
        return apiService.getListDiscoveryArtwork().rxDefaultRequest()
    }

    fun getListInspiration(): Observable<BaseListResponse<InspirationResponse>> {
        return apiService.getListInspiration().rxDefaultRequest()
    }
}