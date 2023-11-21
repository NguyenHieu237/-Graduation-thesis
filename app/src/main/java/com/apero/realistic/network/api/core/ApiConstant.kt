package com.apero.realistic.network.api.core

class ApiConstant {
    interface HttpStatusCode {
        companion object {
            const val UNKNOWN = -1
            const val OK = 200
            const val CREATED = 201
            const val NONE = 204
            const val CUSTOM_AUTH = 302
            const val BAD_REQUEST = 400
            const val UNAUTHORIZED = 401
            const val FORBIDDEN = 403
            const val NOT_FOUND = 404
            const val PRECONDITION_FAILED = 412
            const val UNPROCESSABLE = 422
            const val CART_CHECK_OUT = 473
            const val INTERNAL_SERVER = 500
            const val BAD_GATEWAY_SERVER = 502
        }
    }

    interface RequestParamKey {
        companion object {
            const val AUTHORIZATION_HEADER = "Authorization"
            const val BEARER = "Bearer "
            const val TYPE_MULTI_PART = "multipart/form-data"
            const val PARAM_FILE = "file"
            const val TYPE_TEXT_PLAIN = "text/plain"
        }
    }
}