package com.apero.realistic.network.action.response

import com.google.gson.annotations.SerializedName

open class BaseListResponse<T> : BaseResponse() {
    @SerializedName("data")
    var data: ArrayList<T>? = arrayListOf()
}