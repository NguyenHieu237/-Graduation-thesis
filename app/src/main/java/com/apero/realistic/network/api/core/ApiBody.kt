package com.apero.realistic.network.api.core

import com.apero.realistic.widget.tag.LogUtil
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * Created by Pham Cong Hoan on 16/01/2019.
 */

object ApiBody {
    fun createPostRequest(request: Any): RequestBody {
        val jsonRawString = Gson().toJson(request)
        val body = RequestBody.create(MediaType.parse("text/plain"), jsonRawString)
        LogUtil.i("JsonRawString: ", jsonRawString)
        return body
    }
}