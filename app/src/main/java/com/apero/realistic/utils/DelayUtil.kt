package com.apero.realistic.utils

import android.os.Handler

object DelayUtil {
    fun runAfterDelay(time: Long = 1000, completion: (() -> Unit)? = null) {
        Handler().postDelayed({
            completion?.invoke()
        }, time)
    }
}