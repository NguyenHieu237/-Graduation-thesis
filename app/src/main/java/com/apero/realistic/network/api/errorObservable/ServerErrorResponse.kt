package com.apero.realistic.network.api.errorObservable

import com.google.gson.annotations.SerializedName

class ServerErrorResponse {
    @SerializedName("code")
    var code: Int? = null
    @SerializedName("msg")
    var msg: String? = null
}