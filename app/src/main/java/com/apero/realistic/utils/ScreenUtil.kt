package com.apero.realistic.utils

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.util.Size
import android.view.WindowInsets
import android.view.WindowMetrics

object ScreenUtil {
    fun getScreenWidth(activity: Activity): Size {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics: WindowMetrics = activity.windowManager.currentWindowMetrics
            // Gets all excluding insets
            // Gets all excluding insets
            val windowInsets = metrics.windowInsets
            val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars()
                        or WindowInsets.Type.displayCutout()
            )

            val insetsWidth: Int = insets.right + insets.left
            val insetsHeight: Int = insets.top + insets.bottom

            // Legacy size that Display#getSize reports

            // Legacy size that Display#getSize reports
            val bounds = metrics.bounds
            return Size(
                bounds.width() - insetsWidth,
                bounds.height() - insetsHeight
            )
        } else {
            val display = activity.windowManager.defaultDisplay

            return Size(
                display.width,
                display.height
            )
        }
    }
}