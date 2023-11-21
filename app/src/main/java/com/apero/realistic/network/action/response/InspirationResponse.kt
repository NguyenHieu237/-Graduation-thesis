package com.apero.realistic.network.action.response

import com.google.gson.annotations.SerializedName

class InspirationResponse {
    @SerializedName("image_name")
    var imageName: String? = null
    @SerializedName("positive")
    var positive: String? = null
    @SerializedName("negative")
    var negative: String? = null
    @SerializedName("alpha")
    var alpha: Double? = null
    @SerializedName("guidance_scale")
    var guidanceScale: Double? = null
    @SerializedName("step")
    var step: Double? = null
    @SerializedName("seed")
    var seed: Double? = null
    @SerializedName("style")
    var style: String? = null
    @SerializedName("thumbnail")
    var thumbnail: String? = null
}