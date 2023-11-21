package com.apero.realistic.network.api.core

import com.apero.realistic.network.action.response.BaseListResponse
import com.apero.realistic.network.action.response.InspirationResponse
import com.apero.realistic.network.action.response.TagKeywordResponse
import com.apero.realistic.network.action.response.discovery.DiscoveryResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by Pham Cong Hoan on 16/01/2019.
 */

interface ApiService {
    @GET("a")
    @Headers("Content-Type: application/json")
    fun getListTagKeyword(): Observable<BaseListResponse<TagKeywordResponse>>

    @GET("b")
    @Headers("Content-Type: application/json")
    fun getListDiscoveryArtwork(): Observable<BaseListResponse<DiscoveryResponse>>

    @GET("c")
    @Headers("Content-Type: application/json")
    fun getListInspiration(): Observable<BaseListResponse<InspirationResponse>>

    @Multipart
    @POST
    fun generateForm(
        @Url url: String = "BuildConfig.BASE_URL_GENERATE",
        @Part file: MultipartBody.Part?,
        @Part("styleId") styleId: RequestBody?,
        @Part("prompt") prompt: RequestBody
    ): Observable<ResponseBody>
}