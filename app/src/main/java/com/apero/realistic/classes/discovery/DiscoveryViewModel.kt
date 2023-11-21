package com.apero.realistic.classes.discovery

import com.apero.realistic.base.BaseViewModel
import com.apero.realistic.model.ArtworkCategoryModel
import com.apero.realistic.model.TrendingModel


class DiscoveryViewModel: BaseViewModel() {
    private var listArtworkCategory: ArrayList<ArtworkCategoryModel> = arrayListOf()
    var onSelectCategoryCompletion: (() -> Unit)? = null

    init {
        this.setupListArtworkCategory()
    }

    fun getListTrending(): ArrayList<TrendingModel> {
        return arrayListOf(
            TrendingModel(),
            TrendingModel(),
            TrendingModel(),
            TrendingModel(),
            TrendingModel(),
            TrendingModel(),
            TrendingModel(),
            TrendingModel()
        )
    }

    private fun setupListArtworkCategory() {
        listArtworkCategory = arrayListOf(
            ArtworkCategoryModel("All", selected = true),
            ArtworkCategoryModel("Trending"),
            ArtworkCategoryModel("Tokyo Ghoul"),
            ArtworkCategoryModel("Made in Abyss"),
        )
    }

    fun getListArtworkCategory(): ArrayList<ArtworkCategoryModel> {
        return listArtworkCategory
    }

    fun setArtworkSelected(position: Int) {
        if (this.listArtworkCategory[position].selected) return

        this.listArtworkCategory.map { it.selected = false }
        this.listArtworkCategory[position].selected = true
        this.onSelectCategoryCompletion?.invoke()
    }
}