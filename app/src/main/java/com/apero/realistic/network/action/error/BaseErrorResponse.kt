package com.apero.realistic.network.action.error

import com.google.gson.annotations.SerializedName

class BaseErrorResponse {
    @SerializedName("code")
    var code: Int? = null
    @SerializedName("msg")
    var msg: String? = null
}