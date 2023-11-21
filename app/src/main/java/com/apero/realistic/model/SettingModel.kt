package com.apero.realistic.model

enum class Type {
    LANGUAGE, PRIVACY_POLICY, TERM_OF_SERVICES
}

data class SettingModel(
    val icon : Int,
    val name : String,
    val type: Type
) {
}
