package com.apero.realistic.network.action.request

import com.apero.realistic.network.action.request.BaseRequest
import com.google.gson.annotations.SerializedName

class GenerateFormRequest(
//    @SerializedName("width")
//    val width: Int,
//    @SerializedName("height")
//    val height: Int,
    @SerializedName("text")
    val text: String
): BaseRequest()