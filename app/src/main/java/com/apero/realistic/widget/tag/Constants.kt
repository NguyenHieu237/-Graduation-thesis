package com.apero.realistic.widget.tag

import android.graphics.Color

internal object Constants {
    var DEBUG = true

    // the dimens unit is dp or sp, not px
    const val DEFAULT_LINE_MARGIN = 5f
    const val DEFAULT_TAG_MARGIN = 5f
    const val DEFAULT_TAG_TEXT_PADDING_LEFT = 8f
    const val DEFAULT_TAG_TEXT_PADDING_TOP = 5f
    const val DEFAULT_TAG_TEXT_PADDING_RIGHT = 8f
    const val DEFAULT_TAG_TEXT_PADDING_BOTTOM = 5f
    const val LAYOUT_WIDTH_OFFSET = 2f
    const val DEFAULT_TAG_TEXT_SIZE = 14f
    const val DEFAULT_TAG_DELETE_INDICATOR_SIZE = 14f
    const val DEFAULT_TAG_LAYOUT_BORDER_SIZE = 0f
    const val DEFAULT_TAG_RADIUS = 100f
    val DEFAULT_TAG_LAYOUT_COLOR = Color.parseColor("#333333")
    val DEFAULT_TAG_LAYOUT_COLOR_PRESS = Color.parseColor("#C5FEEA")
    val DEFAULT_TAG_TEXT_COLOR = Color.parseColor("#999999")
    val DEFAULT_TAG_DELETE_INDICATOR_COLOR = Color.parseColor("#ffffff")
    val DEFAULT_TAG_LAYOUT_BORDER_COLOR = Color.parseColor("#C5FEEA")
    const val DEFAULT_TAG_DELETE_ICON = "×"
    const val DEFAULT_TAG_IS_DELETABLE = false
}