package com.apero.realistic.widget.tag

import android.graphics.Color
import android.graphics.drawable.Drawable
import com.apero.realistic.R

/**
 * Tag Entity
 */
class Tag {
    var id = 0
    var text: String? = null
    var tagTextColor = R.color.color_999999
    var tagTextSize = 0f
    var layoutColor = R.color.color_333333
    var layoutColorPress = 0
    var isDeletable = false
    var deleteIndicatorColor = 0
    var deleteIndicatorSize = 0f
    var radius = 0f
    var deleteIcon: String? = null
    var layoutBorderSize = 0f
    var layoutBorderColor = 0
    var background: Drawable? = null

    constructor(text: String?) {
        init(
            0,
            text,
            Constants.DEFAULT_TAG_TEXT_COLOR,
            Constants.DEFAULT_TAG_TEXT_SIZE,
            Constants.DEFAULT_TAG_LAYOUT_COLOR,
            Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
            Constants.DEFAULT_TAG_IS_DELETABLE,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE,
            Constants.DEFAULT_TAG_RADIUS,
            Constants.DEFAULT_TAG_DELETE_ICON,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR
        )
    }

    constructor(text: String?, color: Int) {
        init(
            0,
            text,
            Constants.DEFAULT_TAG_TEXT_COLOR,
            Constants.DEFAULT_TAG_TEXT_SIZE,
            color,
            Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
            Constants.DEFAULT_TAG_IS_DELETABLE,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE,
            Constants.DEFAULT_TAG_RADIUS,
            Constants.DEFAULT_TAG_DELETE_ICON,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR
        )
    }

    constructor(text: String?, color: String?) {
        init(
            0,
            text,
            Constants.DEFAULT_TAG_TEXT_COLOR,
            Constants.DEFAULT_TAG_TEXT_SIZE,
            Color.parseColor(color),
            Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
            Constants.DEFAULT_TAG_IS_DELETABLE,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE,
            Constants.DEFAULT_TAG_RADIUS,
            Constants.DEFAULT_TAG_DELETE_ICON,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR
        )
    }

    constructor(id: Int, text: String?) {
        init(
            id,
            text,
            Constants.DEFAULT_TAG_TEXT_COLOR,
            Constants.DEFAULT_TAG_TEXT_SIZE,
            Constants.DEFAULT_TAG_LAYOUT_COLOR,
            Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
            Constants.DEFAULT_TAG_IS_DELETABLE,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE,
            Constants.DEFAULT_TAG_RADIUS,
            Constants.DEFAULT_TAG_DELETE_ICON,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR
        )
    }

    constructor(id: Int, text: String?, color: Int) {
        init(
            id,
            text,
            Constants.DEFAULT_TAG_TEXT_COLOR,
            Constants.DEFAULT_TAG_TEXT_SIZE,
            color,
            Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
            Constants.DEFAULT_TAG_IS_DELETABLE,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE,
            Constants.DEFAULT_TAG_RADIUS,
            Constants.DEFAULT_TAG_DELETE_ICON,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR
        )
    }

    constructor(id: Int, text: String?, color: String?) {
        init(
            id,
            text,
            Constants.DEFAULT_TAG_TEXT_COLOR,
            Constants.DEFAULT_TAG_TEXT_SIZE,
            Color.parseColor(color),
            Constants.DEFAULT_TAG_LAYOUT_COLOR_PRESS,
            Constants.DEFAULT_TAG_IS_DELETABLE,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_COLOR,
            Constants.DEFAULT_TAG_DELETE_INDICATOR_SIZE,
            Constants.DEFAULT_TAG_RADIUS,
            Constants.DEFAULT_TAG_DELETE_ICON,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_SIZE,
            Constants.DEFAULT_TAG_LAYOUT_BORDER_COLOR
        )
    }

    private fun init(
        id: Int,
        text: String?,
        tagTextColor: Int,
        tagTextSize: Float,
        layout_color: Int,
        layout_color_press: Int,
        isDeletable: Boolean,
        deleteIndicatorColor: Int,
        deleteIndicatorSize: Float,
        radius: Float,
        deleteIcon: String?,
        layoutBorderSize: Float,
        layoutBorderColor: Int
    ) {
        this.id = id
        this.text = text
        this.tagTextColor = tagTextColor
        this.tagTextSize = tagTextSize
        layoutColor = layout_color
        layoutColorPress = layout_color_press
        this.isDeletable = isDeletable
        this.deleteIndicatorColor = deleteIndicatorColor
        this.deleteIndicatorSize = deleteIndicatorSize
        this.radius = radius
        this.deleteIcon = deleteIcon
        this.layoutBorderSize = layoutBorderSize
        this.layoutBorderColor = layoutBorderColor
    }
}