package com.apero.realistic.network.action.response.discovery

import com.google.gson.annotations.SerializedName

class DiscoveryTemplateResponse {
    @SerializedName("name")
    var name: String? = null
    @SerializedName("prompt")
    var prompt: String? = null
    @SerializedName("native_prompt")
    var nativePrompt: String? = null
    @SerializedName("guidance_scale")
    var guidanceScale: Double? = null
    @SerializedName("step")
    var step: Double? = null
    @SerializedName("seed")
    var seed: Long? = null
    @SerializedName("thumbnail")
    var thumbnail: String? = null
}