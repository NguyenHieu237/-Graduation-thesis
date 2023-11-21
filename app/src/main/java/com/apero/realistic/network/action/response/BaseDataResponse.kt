package com.apero.realistic.network.action.response

import com.google.gson.annotations.SerializedName

open class BaseDataResponse<T>: BaseResponse() {
    @SerializedName("data")
    var data: T? = null
}