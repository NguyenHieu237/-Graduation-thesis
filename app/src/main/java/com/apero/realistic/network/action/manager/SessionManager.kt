package com.apero.realistic.network.action.manager

import com.apero.realistic.network.api.core.ApiConstant

class SessionManager {
    companion object {
        val instance = SessionManager()
    }

    private var token: String? = null

    fun getToken(): String? {
        return token
    }
    
    fun getTokenAuth(): String {
        return ApiConstant.RequestParamKey.BEARER + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwbGF0Zm9ybSI6ImlvcyIsImlhdCI6MTUxNjIzOTAyMn0.x6IpwVBb5g1bNLEsFjfGoghj0RVIhIXp2EGJaShka3k"
    }

    fun saveToken(token: String?) {
        this.token = token
    }
}