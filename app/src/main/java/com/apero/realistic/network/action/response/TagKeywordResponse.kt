package com.apero.realistic.network.action.response

import com.apero.realistic.base.ext.guardLet
import com.google.gson.annotations.SerializedName

class TagKeywordResponse {
    @SerializedName("category")
    var category: String? = null
    @SerializedName("keywords")
    private var keywords: ArrayList<String>? = null
    private var keywordsSelector: ArrayList<Keyword> = arrayListOf()

    fun getListKeyword(): ArrayList<Keyword> {
        return keywordsSelector
    }

    fun convertToKeywordSelect() {
        val keywords = keywords.guardLet { return }!!
        keywordsSelector.clear()
        keywords.forEach {
            keywordsSelector.add(Keyword(key = it))
        }
    }
}

class Keyword(val key: String, var selected: Boolean = false)