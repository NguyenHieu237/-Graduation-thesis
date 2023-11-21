package com.apero.realistic.base.ext

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun View.setBackground(context: Context, drawableId: Int) {
    this.background = ContextCompat.getDrawable(
        context,
        drawableId
    )
}

fun View.setBackgroundTint(context: Context, colorId: Int) {
    this.backgroundTintList = ContextCompat.getColorStateList(
        context,
        colorId
    )
}

fun View.show() : View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

fun View.hide() : View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

fun View.gone() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

inline fun View.showIf(condition: () -> Boolean) : View {
    if (visibility != View.VISIBLE && condition()) {
        visibility = View.VISIBLE
    }
    return this
}

inline fun View.hideIf(condition: () -> Boolean) : View {
    visibility = if (visibility != View.INVISIBLE && condition()) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
    return this
}

inline fun View.hiddenIf(condition: () -> Boolean) : View {
    visibility = if (condition()) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
}

inline fun View.goneIf(condition: () -> Boolean) : View {
    if (visibility != View.GONE && condition()) {
        visibility = View.GONE
    }
    return this
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}