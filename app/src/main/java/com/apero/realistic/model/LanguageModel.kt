package com.apero.realistic.model

data class LanguageModel(
    val code: String,
    val nameRes: Int,
    val imgRes: Int,
    var isSelected: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (other !is LanguageModel) return false
        return code == other.code &&
            nameRes == other.nameRes &&
            imgRes == other.imgRes &&
            isSelected == other.isSelected
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + nameRes
        result = 31 * result + imgRes
        result = 31 * result + isSelected.hashCode()
        return result
    }
}
