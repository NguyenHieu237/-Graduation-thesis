package com.apero.realistic.network.action.request

import com.apero.realistic.network.api.core.ApiBody
import okhttp3.RequestBody

open class BaseRequest

fun BaseRequest.toRequestBody(): RequestBody {
    return ApiBody.createPostRequest(this)
}