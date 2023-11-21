package com.apero.realistic.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.LocaleList
import com.apero.realistic.R
import com.apero.realistic.model.LanguageModel
import java.util.*

class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {
        fun updateLocale(ctx: Context, localeToSwitchTo: Locale): ContextWrapper {
            var context = ctx
            val resources = context.resources
            val configuration = resources.configuration
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            context = context.createConfigurationContext(configuration)
            return ContextUtils(context)
        }
    }
}
