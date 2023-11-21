package com.apero.realistic.classes.generate

import com.apero.realistic.base.BaseViewModel
import com.apero.realistic.manager.GenerateSettingManager
import com.apero.realistic.model.RatioModel

class GenerateViewModel: BaseViewModel() {
    private var listRatio: ArrayList<RatioModel> = GenerateSettingManager.shared.getListRatio()

    var onSelectRatioCompletion: (() -> Unit)? = null

    fun getListRatio(): ArrayList<RatioModel> {
        return this.listRatio
    }

    fun setRatioSelected(position: Int) {
        if (this.listRatio[position].ratioIsSelected()) return

        this.listRatio.map { it.setSelected(selected = false) }
        this.listRatio[position].setSelected(selected = true)
        this.onSelectRatioCompletion?.invoke()
    }
}