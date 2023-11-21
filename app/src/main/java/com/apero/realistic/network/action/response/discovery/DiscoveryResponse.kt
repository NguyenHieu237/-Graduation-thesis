package com.apero.realistic.network.action.response.discovery

import com.google.gson.annotations.SerializedName

class DiscoveryResponse {
    @SerializedName("category")
    var category: String? = null
    @SerializedName("templates")
    var templates: ArrayList<DiscoveryTemplateResponse>? = null
}

