package com.apero.realistic.widget.tag

import android.util.Log

/**
 * Created by kaede on 2016/4/11.
 */
internal object LogUtil {
    const val PREFIX = "[kaede]"
    private var mIsDebug = true
    private var mTag = "NO TAG"

    fun v(TAG: String?, msg: String) {
        if (!Constants.DEBUG) return
        Log.v(TAG, PREFIX + msg)
    }

    fun d(TAG: String?, msg: String) {
        if (!Constants.DEBUG) return
        Log.d(TAG, PREFIX + msg)
    }

    fun w(TAG: String?, msg: String) {
        Log.w(TAG, PREFIX + msg)
    }

    fun e(TAG: String?, msg: String) {
        Log.e(TAG, PREFIX + msg)
    }

    /**
     * Setup Log Util
     *
     * @param isDebug If TRUE all log will print, else FALSE the log will be off
     * @param tag     Tag text using for Log TAG
     */
    fun init(isDebug: Boolean, tag: String) {
        LogUtil.mIsDebug = isDebug
        LogUtil.mTag = tag
    }

    /**
     * Show log with info type
     */
    fun i(`object`: Any) {
        if (mIsDebug) {
            Log.i(mTag, `object`.toString())
        }
    }

    /**
     * Show log with info type
     */
    fun i(vararg objects: Any) {
        if (mIsDebug) {
            val sb = StringBuilder()
            for (`object` in objects) {
                sb.append(`object`).append(" ")
            }
            Log.i(mTag, sb.toString())
        }
    }

    /**
     * Show log with debug type
     */
    fun d(`object`: Any) {
        if (mIsDebug) {
            Log.d(mTag, `object`.toString())
        }
    }

    /**
     * Show log with debug type
     */
    fun d(vararg objects: Any) {
        if (mIsDebug) {
            val sb = StringBuilder()
            for (`object` in objects) {
                sb.append(`object`).append(" ")
            }
            Log.d(mTag, sb.toString())
        }
    }

    /**
     * Show log with error type
     */
    fun e(`object`: Any) {
        if (mIsDebug) {
            Log.e(mTag, `object`.toString())
        }
    }

    /**
     * Show log with error type
     */
    fun e(vararg objects: Any) {
        if (mIsDebug) {
            val sb = StringBuilder()
            for (`object` in objects) {
                sb.append(`object`).append(" ")
            }
            Log.e(mTag, sb.toString())
        }
    }

    /**
     * Show log with warning type
     */
    fun w(`object`: Any) {
        if (mIsDebug) {
            Log.w(mTag, `object`.toString())
        }
    }

    /**
     * Show log with warning type
     */
    fun w(vararg objects: Any) {
        if (mIsDebug) {
            val sb = StringBuilder()
            for (`object` in objects) {
                sb.append(`object`).append(" ")
            }
            Log.w(mTag, sb.toString())
        }
    }

    /**
     * Show log of Exception with error type
     */
    fun e(e: Exception) {
        if (mIsDebug) {
            Log.e(mTag, "Exception: $e")
        }
    }
}