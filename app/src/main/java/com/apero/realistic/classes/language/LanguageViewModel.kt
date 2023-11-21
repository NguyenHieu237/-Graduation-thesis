package com.apero.realistic.classes.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apero.realistic.R
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.manager.UserSessionManager
import com.apero.realistic.model.LanguageModel
import kotlinx.coroutines.launch

class LanguageViewModel : ViewModel() {
    val successFetchLanguages = MutableLiveData<ArrayList<LanguageModel>>()
    private var chosenLanguage: LanguageModel? = null

    fun startFetchListLanguage() {
        viewModelScope.launch {
            // hard coded list
            val languages = arrayListOf(
                LanguageModel("en", R.string.english, R.raw.ic_english),
                LanguageModel("es", R.string.spanish, R.raw.ic_spanish),
                LanguageModel("fr", R.string.french, R.raw.ic_french),
                LanguageModel("hi", R.string.hindi, R.raw.ic_hindi),
                LanguageModel("pt", R.string.portuguese, R.raw.ic_portuguese),
                LanguageModel("ru", R.string.russian, R.raw.ic_rusian),
            )
            languages.firstOrNull {
                language -> language.code == UserSessionManager.shared.getLanguageCode()
            }?.isSelected = true

            successFetchLanguages.postValue(languages)
        }
    }

    fun chooseLanguage(language: LanguageModel) {
        this.chosenLanguage = language
    }

    /**
     * save language code to shared preference
     * @return true if user chose a language and opposite
     */
    fun saveLanguage(): Boolean {
        val chosenLanguage = chosenLanguage.guardLet { return false }!!
        UserSessionManager.shared.setLanguage(code = chosenLanguage.code)
        UserSessionManager.shared.setStateSelectedLanguage(isSelected = true)
        return true
    }
}
