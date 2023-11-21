package com.apero.realistic.classes.generate.settings.keyword

import com.apero.realistic.base.BaseViewModel
import com.apero.realistic.model.TagCategoryModel


class TagKeywordViewModel: BaseViewModel() {
    private var listKeywordCategory: ArrayList<TagCategoryModel> = arrayListOf()

    var onLoadListKeywordCategoryCompletion: ((ArrayList<TagCategoryModel>) -> Unit)?= null
    var onSelectKeywordCategoryCompletion: (() -> Unit)?= null

    fun getListKeywordCategory(): ArrayList<TagCategoryModel> {
        return listKeywordCategory
    }

    fun selectKeywordCategory(position: Int) {
        if (position >= listKeywordCategory.count()) return

        if (listKeywordCategory[position].selected) return

        listKeywordCategory.forEach { it.selected = false }
        listKeywordCategory[position].selected = true
        onSelectKeywordCategoryCompletion?.invoke()
    }

    fun startFetchDataCategory() {
        this.listKeywordCategory = arrayListOf(
            TagCategoryModel(title = "Quality", countSelected = 1, selected = true),
            TagCategoryModel(title = "Style", countSelected = 10, selected = false),
            TagCategoryModel(title = "Colors", countSelected = 0, selected = false),
        )

        this.onLoadListKeywordCategoryCompletion?.invoke(listKeywordCategory)
    }
}