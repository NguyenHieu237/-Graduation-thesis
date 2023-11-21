package com.apero.realistic.model

class TagCategoryModel(val title: String, val countSelected: Int, var selected: Boolean = false) {
    private fun getPrefix(): String {
        if (countSelected <= 0) return ""
        if (countSelected < 10) return " (0$countSelected)"
        return " ($countSelected)"
    }

    fun getTitlePrefix(): String {
        return title + getPrefix()
    }
}