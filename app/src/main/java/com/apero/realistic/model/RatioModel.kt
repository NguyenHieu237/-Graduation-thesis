package com.apero.realistic.model

enum class RatioMode {
    RATIO_1_1,
    RATIO_9_16,
    RATIO_4_3,
    RATIO_3_4;

    fun getRatioText(): String {
        return when (this) {
            RATIO_1_1 -> "1:1"
            RATIO_9_16 -> "9:16"
            RATIO_4_3 -> "4:3"
            RATIO_3_4 -> "3:4"
        }
    }

    fun getRatioNumber(): Pair<Int, Int> {
        return when (this) {
            RATIO_1_1 -> Pair(14, 14)
            RATIO_9_16 -> Pair(12, 20)
            RATIO_4_3 -> Pair(10, 14)
            RATIO_3_4 -> Pair(14, 10)
        }
    }
}

class RatioModel {
    private var isSelected: Boolean = false
    private var ratioMode: RatioMode = RatioMode.RATIO_1_1

    constructor(ratioMode: RatioMode, selected: Boolean = false) {
        this.ratioMode = ratioMode
        this.isSelected = selected
    }

    fun getMode(): RatioMode {
        return ratioMode
    }

    fun getRatioText(): String {
        return ratioMode.getRatioText()
    }

    fun setSelected(selected: Boolean) {
        this.isSelected = selected
    }

    fun ratioIsSelected(): Boolean {
        return this.isSelected
    }
}