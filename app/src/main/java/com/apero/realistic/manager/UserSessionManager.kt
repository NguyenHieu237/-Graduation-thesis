package com.apero.realistic.manager

import com.apero.realistic.RealisticApplication
import com.apero.realistic.preferences.SharedPrefsApi
import com.apero.realistic.preferences.SharedPrefsKey

class UserSessionManager {
    companion object {
        val shared: UserSessionManager = UserSessionManager()
    }

    private val context = RealisticApplication.shared

    fun isSelectedLanguage(): Boolean {
        return SharedPrefsApi(context).get(SharedPrefsKey.KEY_SELECTED_LANGUAGE, false)
    }

    fun setStateSelectedLanguage(isSelected: Boolean) {
        SharedPrefsApi(context).set(SharedPrefsKey.KEY_SELECTED_LANGUAGE, isSelected)
    }

    fun setLanguage(code: String) {
        SharedPrefsApi(context).set(SharedPrefsKey.KEY_LANGUAGE_CODE, code)
    }

    fun getLanguageCode(): String {
        return SharedPrefsApi(context).get(SharedPrefsKey.KEY_LANGUAGE_CODE, "en")
    }
}