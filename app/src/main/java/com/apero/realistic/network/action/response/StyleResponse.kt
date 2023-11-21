package com.apero.realistic.network.action.response

import com.google.gson.annotations.SerializedName

class StyleResponse {
    @SerializedName("items")
    var items: ArrayList<StyleItemResponse>? = null
}

class StyleItemResponse {
    @SerializedName("_id")
    var id: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("prompt")
    var prompt: String? = null
    @SerializedName("key")
    var key: String? = null
    @SerializedName("config")
    var config: StyleConfigResponse? = null

    var isNone: Boolean? = false
    var selected: Boolean? = false

    fun setSelected(selected: Boolean) {
        this.selected = selected
    }

    fun isSelected(): Boolean {
        return selected ?: false
    }

    fun isNone(): Boolean {
        return isNone == true
    }
}

class StyleConfigResponse {
    @SerializedName("cfgScale")
    var cfgScale: Double? = null
    @SerializedName("steps")
    var steps: Double? = null
    @SerializedName("stepScheduleStart")
    var stepScheduleStart: Double? = null
    @SerializedName("algorithym")
    var algorithym: String? = null
    @SerializedName("positivePrompt")
    var positivePrompt: String? = null
}