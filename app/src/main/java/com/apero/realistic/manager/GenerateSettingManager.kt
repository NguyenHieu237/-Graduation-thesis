package com.apero.realistic.manager

import com.apero.realistic.model.RatioMode
import com.apero.realistic.model.RatioModel
import com.apero.realistic.network.action.response.InspirationResponse
import com.apero.realistic.network.action.response.StyleItemResponse
import com.apero.realistic.network.action.response.StyleResponse
import com.apero.realistic.network.action.response.TagKeywordResponse
import com.apero.realistic.network.action.response.discovery.DiscoveryResponse
import io.reactivex.subjects.BehaviorSubject

class GenerateSettingManager {
    companion object {
        val shared: GenerateSettingManager = GenerateSettingManager()
    }

    private var aspectRatios: ArrayList<RatioModel> = arrayListOf()
//    private var tagKeywords: ArrayList<TagKeywordResponse> = arrayListOf()
    private var styles: StyleResponse = StyleResponse()
//    private var inspirations: ArrayList<InspirationResponse> = arrayListOf()
//    private var listDiscovery: ArrayList<DiscoveryResponse> = arrayListOf()

    private var tagKeywordsObserver: BehaviorSubject<ArrayList<TagKeywordResponse>> = BehaviorSubject.create()
    private var inspirationObserver: BehaviorSubject<ArrayList<InspirationResponse>> = BehaviorSubject.create()
    private var listDiscoveryObserver: BehaviorSubject<ArrayList<DiscoveryResponse>> = BehaviorSubject.create()
    private var stylesObserver: BehaviorSubject<ArrayList<StyleItemResponse>> = BehaviorSubject.create()

    init {
        this.setupAspectRatios()
    }

    private fun setupAspectRatios() {
        aspectRatios.add(RatioModel(RatioMode.RATIO_1_1, selected = true))
        aspectRatios.add(RatioModel(RatioMode.RATIO_9_16, selected = false))
        aspectRatios.add(RatioModel(RatioMode.RATIO_3_4, selected = false))
        aspectRatios.add(RatioModel(RatioMode.RATIO_4_3, selected = false))
    }

    fun getListRatio(): ArrayList<RatioModel> {
        return this.aspectRatios
    }

    // TODO Keywords
    fun setListTagKeyword(list: ArrayList<TagKeywordResponse>) {
        tagKeywordsObserver.onNext(list)
    }

    fun getListTagKeywordObservable(): BehaviorSubject<ArrayList<TagKeywordResponse>> {
        return tagKeywordsObserver
    }

    // TODO Inspiration
    fun setListInspiration(list: ArrayList<InspirationResponse>) {
        inspirationObserver.onNext(list)
    }

    fun getListInspirationObservable(): BehaviorSubject<ArrayList<InspirationResponse>> {
        return inspirationObserver
    }

    // TODO Discovery
    fun setListDiscovery(list: ArrayList<DiscoveryResponse>) {
        listDiscoveryObserver.onNext(list)
    }

    fun getListDiscoveryObservable(): BehaviorSubject<ArrayList<DiscoveryResponse>> {
        return listDiscoveryObserver
    }

    // TODO Style
    fun setListStyle(list: ArrayList<StyleItemResponse>) {
        stylesObserver.onNext(list)
    }

    fun getListStyle(): ArrayList<StyleItemResponse> {
        return stylesObserver.value
    }

    fun getListStyleObservable(): BehaviorSubject<ArrayList<StyleItemResponse>> {
        return stylesObserver
    }
}