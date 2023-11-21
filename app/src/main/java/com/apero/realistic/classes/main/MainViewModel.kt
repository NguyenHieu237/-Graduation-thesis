package com.apero.realistic.classes.main

import com.apero.realistic.base.BaseViewModel
import com.apero.realistic.base.ext.disposedBy
import com.apero.realistic.manager.GenerateSettingManager
import com.apero.realistic.network.action.response.BaseListResponse
import com.apero.realistic.network.action.response.InspirationResponse
import com.apero.realistic.network.action.response.TagKeywordResponse
import com.apero.realistic.network.action.response.discovery.DiscoveryResponse
import com.apero.realistic.network.action.usecase.UseCase
import com.apero.realistic.utils.DelayUtil
import com.apero.realistic.utils.Event
import io.reactivex.Observable

class MainViewModel : BaseViewModel() {
    private val useCase: UseCase = UseCase.instance()
    private val generateSettingManager: GenerateSettingManager = GenerateSettingManager.shared

    fun startFetchAllData() {
        DelayUtil.runAfterDelay(3000, completion = {
            progress.onNext(Event(false))
        })
//        makeApi()
//            .doOnSubscribe {
//
//            }
//            .doOnTerminate {
//                progress.onNext(Event(false))
//            }
//            .subscribe {
//
//            }
//            .disposedBy(bag)
    }

    private fun makeApi(): Observable<Boolean> {
        val fetchListInspiration = useCase.getListInspiration()
        val fetchListTagKeyword = useCase.getListTagKeyword()
        val fetchListDiscoveryArtwork = useCase.getListDiscoveryArtwork()

        return Observable.zip(
            fetchListInspiration,
            fetchListTagKeyword,
            fetchListDiscoveryArtwork
        ) { s1, s2, s3 -> convert(s1, s2, s3) }
    }

    private fun convert(
        inspirationsResponse: BaseListResponse<InspirationResponse>,
        tagKeywordsResponse: BaseListResponse<TagKeywordResponse>,
        discoveryArtworksResponse: BaseListResponse<DiscoveryResponse>
    ): Boolean {
        /// Save all response to cache
        generateSettingManager.setListInspiration(list = inspirationsResponse.data ?: arrayListOf())
        generateSettingManager.setListTagKeyword(list = tagKeywordsResponse.data ?: arrayListOf())
        generateSettingManager.setListDiscovery(list = discoveryArtworksResponse.data ?: arrayListOf())

        return true
    }
}